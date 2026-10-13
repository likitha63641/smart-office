package com.smartoffice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.smartoffice.dto.CategoryStatisticsResponse;
import com.smartoffice.dto.IssueStatisticsResponse;
import com.smartoffice.dto.TechnicianStatisticsResponse;
import com.smartoffice.exception.EmployeeNotFoundException;
import com.smartoffice.exception.IssueNotFoundException;
import com.smartoffice.exception.TechnicianNotFoundException;
import com.smartoffice.model.Employee;
import com.smartoffice.model.Issue;
import com.smartoffice.model.IssuePriority;
import com.smartoffice.model.IssueStatus;
import com.smartoffice.model.Technician;
import com.smartoffice.repository.EmployeeRepository;
import com.smartoffice.repository.IssueRepository;
import com.smartoffice.repository.TechnicianRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IssueService {

    private static final Logger log =
            LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;
    private final EmployeeRepository employeeRepository;
    private final TechnicianRepository technicianRepository;
    private final IssueHistoryService issueHistoryService;
    private final NotificationService notificationService;

    public IssueService(IssueRepository issueRepository, EmployeeRepository employeeRepository, TechnicianRepository technicianRepository) {
        this(issueRepository, employeeRepository, technicianRepository, null, null);
    }

    @Autowired
    public IssueService(IssueRepository issueRepository, EmployeeRepository employeeRepository, TechnicianRepository technicianRepository,
                        IssueHistoryService issueHistoryService, NotificationService notificationService) {
        this.issueRepository = issueRepository;
        this.employeeRepository = employeeRepository;
        this.technicianRepository = technicianRepository;
        this.issueHistoryService = issueHistoryService;
        this.notificationService = notificationService;
    }

    // UPDATED: creates the issue and automatically notifies all admins with a summary
    public Issue createIssue(Issue issue) {

        log.info("Creating new issue: {}", issue.getTitle());

        Issue saved = issueRepository.save(issue);

        if (notificationService != null) {
            String reporter = saved.getEmployee() != null ? saved.getEmployee().getName() : saved.getReportedBy();
            String desc = saved.getDescription() == null ? "" : saved.getDescription().trim();
            if (desc.length() > 150) desc = desc.substring(0, 147) + "...";
            notificationService.notifyAdmins(saved.getId(),
                    "NEW INCIDENT #" + saved.getId() + " | " + saved.getPriority() + " | " + saved.getCategory()
                    + " | Reported by " + reporter + " | Issue: " + saved.getTitle() + " | Summary: " + desc);
        }
        return saved;
    }

    public Employee getEmployeeById(Long employeeId) {

        return employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee with Id " + employeeId + " not found"));
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Page<Issue> getIssuesWithPagination(Pageable pageable) {
        return issueRepository.findAll(pageable);
    }

    public Page<Issue> getIssuesByPriorityWithPagination(Pageable pageable) {
        return issueRepository.findAllByPriorityOrder(pageable);
    }

    public List<Issue> getIssuesByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }

    public List<Issue> getIssuesByPriority(IssuePriority priority) {
        return issueRepository.findByPriority(priority);
    }

    public List<Issue> getIssuesByCategory(String category) {
        return issueRepository.findByCategoryIgnoreCase(category);
    }

    public List<Issue> getAllIssuesWithEmployeeAndTechnicianUsingEntityGraph() {
        return issueRepository.findAllWithEmployeeAndTechnicianUsingEntityGraph();
    }

    public List<Issue> getAllIssuesWithEmployeeAndTechnician() {
        return issueRepository.findAllWithEmployeeAndTechnician();
    }

    public List<Issue> searchIssues(String keyword) {

        return issueRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword);
    }

    public List<Issue> getIssuesByFilter(
            String category,
            IssuePriority priority,
            IssueStatus status) {

        return issueRepository.findIssuesByOptionalFilters(
                category,
                priority,
                status);
    }

    public Page<Issue> getIssuesByFilterWithPagination(
            String category,
            IssuePriority priority,
            IssueStatus status,
            Pageable pageable) {

        return issueRepository.findIssuesByOptionalFiltersWithPagination(
                category,
                priority,
                status,
                pageable);
    }

    public List<Issue> getIssuesSortedByPriority() {

        List<Issue> issues = issueRepository.findAll();

        issues.sort(Comparator.comparingInt(issue ->
                switch (issue.getPriority()) {
                    case CRITICAL -> 1;
                    case HIGH -> 2;
                    case MEDIUM -> 3;
                    case LOW -> 4;
                }
        ));

        return issues;
    }

    public List<Issue> getIssuesByEmployeeId(Long employeeId) {
        return issueRepository.findByEmployeeId(employeeId);
    }

    public Issue getIssueById(Long id) {

        return issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException("Issue with Id " + id + " not found"));
    }

    public Issue updateIssue(Long id, Issue issue) {

        Issue existingIssue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException("Issue with Id " + id + " not found"));

        existingIssue.setTitle(issue.getTitle());
        existingIssue.setDescription(issue.getDescription());
        existingIssue.setCategory(issue.getCategory());
        existingIssue.setReportedBy(issue.getReportedBy());
        existingIssue.setEmployee(issue.getEmployee());
        existingIssue.setPriority(issue.getPriority());
        existingIssue.setAttachmentName(issue.getAttachmentName());
        existingIssue.setAttachmentPath(issue.getAttachmentPath());
        return issueRepository.save(existingIssue);
    }

    @Transactional
    public Issue updateIssueStatus(Long id, IssueStatus status) {
        return updateIssueStatus(id, status, "system");
    }

    @Transactional
    public Issue updateIssueStatus(Long id, IssueStatus status, String changedBy) {
        Issue existingIssue = issueRepository.findById(id)
                .orElseThrow(() -> new IssueNotFoundException("Issue with Id " + id + " not found"));

        IssueStatus currentStatus = existingIssue.getStatus();
        if (currentStatus == IssueStatus.NEW && status != IssueStatus.REVIEWED && status != IssueStatus.ASSIGNED)
            throw new IllegalStateException("NEW issue can move to REVIEWED or ASSIGNED");
        if (currentStatus == IssueStatus.REVIEWED && status != IssueStatus.ASSIGNED)
            throw new IllegalStateException("REVIEWED issue can only move to ASSIGNED");
        if (currentStatus == IssueStatus.ASSIGNED && status != IssueStatus.IN_PROGRESS)
            throw new IllegalStateException("ASSIGNED issue can only move to IN_PROGRESS");
        if (currentStatus == IssueStatus.IN_PROGRESS && status != IssueStatus.RESOLVED)
            throw new IllegalStateException("IN_PROGRESS issue can only move to RESOLVED");
        if (currentStatus == IssueStatus.RESOLVED && status != IssueStatus.CLOSED)
            throw new IllegalStateException("RESOLVED issue can only move to CLOSED");
        if (currentStatus == IssueStatus.CLOSED)
            throw new IllegalStateException("CLOSED issue cannot be changed");

        existingIssue.setStatus(status);
        if (status == IssueStatus.RESOLVED && existingIssue.getTechnician() != null) {
            Technician technician = existingIssue.getTechnician();
            technician.setAvailable(true);
            technicianRepository.save(technician);
        }
        Issue saved = issueRepository.save(existingIssue);
        if (issueHistoryService != null) issueHistoryService.record(saved, currentStatus, status, changedBy);
        if (notificationService != null && saved.getEmployee() != null) {
            notificationService.create(saved.getEmployee().getEmail(), saved.getId(),
                    "Issue #" + saved.getId() + " status changed to " + status);
        }
        return saved;
    }

    public Issue updateAttachment(
            Long id,
            String attachmentName,
            String attachmentPath) {

        Issue existingIssue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException(
                                "Issue with Id " + id + " not found"));

        existingIssue.setAttachmentName(attachmentName);
        existingIssue.setAttachmentPath(attachmentPath);

        return issueRepository.save(existingIssue);
    }

    public Issue assignTechnician(Long issueId, Long technicianId) {
        return assignTechnician(issueId, technicianId, "system");
    }

    @Transactional
    public Issue assignTechnician(Long issueId, Long technicianId, String changedBy) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(
                        "Issue with Id " + issueId + " not found"));

        Technician technician = technicianRepository.findById(technicianId)
                .orElseThrow(() -> new TechnicianNotFoundException(
                        "Technician with Id " + technicianId + " not found"));
        log.info(
                "Assigning technician {} to issue {}",
                technicianId,
                issueId
        );
        if (!technician.isAvailable()) {

            log.warn(
                    "Technician {} is not available for issue {}",
                    technicianId,
                    issueId
            );

            throw new IllegalStateException(
                    "Technician is currently not available");
        }

        IssueStatus oldStatus = issue.getStatus();
        issue.setTechnician(technician);
        issue.setStatus(IssueStatus.ASSIGNED);

        technician.setAvailable(false);

        technicianRepository.save(technician);
        Issue saved = issueRepository.save(issue);
        if (issueHistoryService != null) issueHistoryService.record(saved, oldStatus, IssueStatus.ASSIGNED, changedBy);
        if (notificationService != null && saved.getEmployee() != null) {
            notificationService.create(saved.getEmployee().getEmail(), saved.getId(),
                    "A technician was assigned to issue #" + saved.getId());
        }
        return saved;
    }

    public Issue deleteIssue(Long id) {

        Issue existingIssue = issueRepository.findById(id)
                .orElseThrow(() ->
                        new IssueNotFoundException("Issue with Id " + id + " not found"));

        issueRepository.delete(existingIssue);

        return existingIssue;
    }

    public IssueStatisticsResponse getIssueStatistics() {

        IssueStatisticsResponse response = new IssueStatisticsResponse();

        response.setTotalIssues(issueRepository.count());

        response.setNewIssues(
                issueRepository.countByStatus(IssueStatus.NEW));

        response.setAssignedIssues(
                issueRepository.countByStatus(IssueStatus.ASSIGNED));

        response.setInProgressIssues(
                issueRepository.countByStatus(IssueStatus.IN_PROGRESS));

        response.setResolvedIssues(
                issueRepository.countByStatus(IssueStatus.RESOLVED));

        response.setClosedIssues(
                issueRepository.countByStatus(IssueStatus.CLOSED));

        response.setLowPriorityIssues(
                issueRepository.countByPriority(IssuePriority.LOW));

        response.setMediumPriorityIssues(
                issueRepository.countByPriority(IssuePriority.MEDIUM));

        response.setHighPriorityIssues(
                issueRepository.countByPriority(IssuePriority.HIGH));

        response.setCriticalIssues(
                issueRepository.countByPriority(IssuePriority.CRITICAL));

        return response;
    }

    public List<CategoryStatisticsResponse> getCategoryStatistics() {

        List<Object[]> results = issueRepository.countIssuesByCategory();

        return results.stream()
                .map(result -> new CategoryStatisticsResponse(
                        (String) result[0],
                        ((Number) result[1]).longValue()
                ))
                .toList();
    }

    public List<TechnicianStatisticsResponse> getTechnicianStatistics() {

        List<Object[]> results = issueRepository.countIssuesByTechnician();

        return results.stream()
                .map(result -> new TechnicianStatisticsResponse(
                        ((Number) result[0]).longValue(),
                        (String) result[1],
                        (String) result[2],
                        ((Number) result[3]).longValue()
                ))
                .toList();
    }
}