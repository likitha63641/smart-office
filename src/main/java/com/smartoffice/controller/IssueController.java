package com.smartoffice.controller;

import com.smartoffice.dto.*;
import com.smartoffice.model.*;
import com.smartoffice.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;
    private final FileStorageService fileStorageService;
    private final EmailService emailService;
    private final UserContextService userContext;
    private final IssueHistoryService historyService;

    public IssueController(IssueService issueService, FileStorageService fileStorageService, EmailService emailService,
                           UserContextService userContext, IssueHistoryService historyService) {
        this.issueService=issueService; this.fileStorageService=fileStorageService; this.emailService=emailService;
        this.userContext=userContext; this.historyService=historyService;
    }

    @GetMapping
    public List<IssueResponse> getIssues(Authentication authentication) {
        List<Issue> issues = userContext.isAdmin(authentication)
                ? issueService.getAllIssues()
                : issueService.getIssuesByEmployeeId(userContext.currentEmployee(authentication).getId());
        return issues.stream().map(this::toResponse).toList();
    }

    @GetMapping("/employee/{employeeId}")
    public List<IssueResponse> getIssuesByEmployeeId(@PathVariable Long employeeId, Authentication authentication) {
        if (!userContext.isAdmin(authentication) && !userContext.currentEmployee(authentication).getId().equals(employeeId)) throw new AccessDeniedException("You can only view your own issues");
        return issueService.getIssuesByEmployeeId(employeeId).stream().map(this::toResponse).toList();
    }

    @GetMapping("/status/{status}")
    public List<IssueResponse> getIssuesByStatus(@PathVariable IssueStatus status) {
        return issueService.getIssuesByStatus(status).stream().map(this::toResponse).toList();
    }
    @GetMapping("/priority/{priority}")
    public List<IssueResponse> getIssuesByPriority(@PathVariable IssuePriority priority) {
        return issueService.getIssuesByPriority(priority).stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public IssueResponse getIssue(@PathVariable Long id, Authentication authentication) {
        Issue issue=issueService.getIssueById(id); ensureOwnerOrAdmin(issue, authentication); return toResponse(issue);
    }

    @PostMapping
    public IssueResponse createIssue(@Valid @RequestBody IssueRequest request, Authentication authentication) {
        Employee employee;
        if (userContext.isAdmin(authentication) && request.getEmployeeId()!=null) employee=issueService.getEmployeeById(request.getEmployeeId());
        else employee=userContext.currentEmployee(authentication);
        Issue issue=new Issue(request.getTitle(), request.getDescription(), request.getCategory(), employee.getName());
        if (request.getPriority()!=null) issue.setPriority(request.getPriority());
        issue.setEmployee(employee);
        Issue saved=issueService.createIssue(issue);
        historyService.record(saved, null, saved.getStatus(), authentication.getName());
        return toResponse(saved);
    }

    @PutMapping("/{id}")
    public IssueResponse updateIssue(@PathVariable Long id, @Valid @RequestBody IssueRequest request, Authentication authentication) {
        Issue existing=issueService.getIssueById(id); ensureOwnerOrAdmin(existing, authentication);
        Employee employee=userContext.isAdmin(authentication) && request.getEmployeeId()!=null
                ? issueService.getEmployeeById(request.getEmployeeId()) : existing.getEmployee();
        Issue issue=new Issue(request.getTitle(), request.getDescription(), request.getCategory(), employee.getName());
        issue.setEmployee(employee); issue.setPriority(request.getPriority()!=null ? request.getPriority() : existing.getPriority());
        return toResponse(issueService.updateIssue(id, issue));
    }

    @PutMapping("/{id}/status")
    public IssueResponse updateIssueStatus(@PathVariable Long id, @RequestBody IssueStatusRequest request, Authentication authentication) {
        if (!userContext.isAdmin(authentication)) throw new AccessDeniedException("Only administrators can change issue status");
        Issue updated=issueService.updateIssueStatus(id, request.getStatus(), authentication.getName());
        return toResponse(updated);
    }

    @PutMapping("/{issueId}/assign/{technicianId}")
    public IssueResponse assignTechnician(@PathVariable Long issueId, @PathVariable Long technicianId, Authentication authentication) {
        if (!userContext.isAdmin(authentication)) throw new AccessDeniedException("Only administrators can assign technicians");
        Issue assigned=issueService.assignTechnician(issueId, technicianId, authentication.getName());
        return toResponse(assigned);
    }

    @PostMapping("/{issueId}/attachment")
    public String uploadAttachment(@PathVariable Long issueId, @RequestPart("file") MultipartFile file, Authentication authentication) {
        Issue issue=issueService.getIssueById(issueId); ensureOwnerOrAdmin(issue, authentication);
        String fileName=fileStorageService.storeFile(file);
        issueService.updateAttachment(issueId, fileName, "uploads/"+fileName);
        return "File uploaded successfully: " + fileName;
    }

    @DeleteMapping("/{id}")
    public IssueResponse deleteIssue(@PathVariable Long id) { return toResponse(issueService.deleteIssue(id)); }

    @GetMapping("/sorted-by-priority") public List<IssueResponse> getIssuesSortedByPriority(){return issueService.getIssuesSortedByPriority().stream().map(this::toResponse).toList();}
    @GetMapping("/search") public List<IssueResponse> searchIssues(@RequestParam String keyword){return issueService.searchIssues(keyword).stream().map(this::toResponse).toList();}
    @GetMapping("/category/{category}") public List<IssueResponse> getIssuesByCategory(@PathVariable String category){return issueService.getIssuesByCategory(category).stream().map(this::toResponse).toList();}
    @GetMapping("/filter") public List<IssueResponse> getIssuesByFilter(@RequestParam(required=false) String category,@RequestParam(required=false) IssuePriority priority,@RequestParam(required=false) IssueStatus status){return issueService.getIssuesByFilter(category,priority,status).stream().map(this::toResponse).toList();}
    @GetMapping("/statistics") public IssueStatisticsResponse getIssueStatistics(){return issueService.getIssueStatistics();}
    @GetMapping("/category-statistics") public List<CategoryStatisticsResponse> getCategoryStatistics(){return issueService.getCategoryStatistics();}
    @GetMapping("/technician-statistics") public List<TechnicianStatisticsResponse> getTechnicianStatistics(){return issueService.getTechnicianStatistics();}
    @GetMapping("/page") public Page<IssueResponse> getIssuesWithPagination(Pageable pageable){return issueService.getIssuesWithPagination(pageable).map(this::toResponse);}
    @GetMapping("/page/priority") public Page<IssueResponse> getIssuesByPriorityWithPagination(Pageable pageable){return issueService.getIssuesByPriorityWithPagination(pageable).map(this::toResponse);}
    @GetMapping("/filter/page") public Page<IssueResponse> getIssuesByFilterWithPagination(@RequestParam(required=false) String category,@RequestParam(required=false) IssuePriority priority,@RequestParam(required=false) IssueStatus status,Pageable pageable){return issueService.getIssuesByFilterWithPagination(category,priority,status,pageable).map(this::toResponse);}
    @GetMapping("/with-details") public List<IssueResponse> getAllIssuesWithEmployeeAndTechnician(){return issueService.getAllIssuesWithEmployeeAndTechnician().stream().map(this::toResponse).toList();}
    @GetMapping("/with-details/entity-graph") public List<IssueResponse> getAllIssuesWithEntityGraph(){return issueService.getAllIssuesWithEmployeeAndTechnicianUsingEntityGraph().stream().map(this::toResponse).toList();}

    @GetMapping("/{id}/history")
    public List<IssueHistoryResponse> history(@PathVariable Long id, Authentication authentication) {
        Issue issue=issueService.getIssueById(id); ensureOwnerOrAdmin(issue, authentication); return historyService.get(id);
    }

    private void ensureOwnerOrAdmin(Issue issue, Authentication authentication) {
        if (!userContext.isAdmin(authentication) && !userContext.ownsIssue(issue, authentication)) throw new AccessDeniedException("You do not have access to this issue");
    }
    private IssueResponse toResponse(Issue issue) {
        return new IssueResponse(issue.getId(), issue.getTitle(), issue.getDescription(), issue.getCategory(), issue.getReportedBy(),
                issue.getEmployee()!=null?issue.getEmployee().getId():null, issue.getEmployee()!=null?issue.getEmployee().getName():null,
                issue.getEmployee()!=null?issue.getEmployee().getEmail():null, issue.getTechnician()!=null?issue.getTechnician().getId():null,
                issue.getStatus(), issue.getPriority(), issue.getAttachmentName(), issue.getAttachmentPath());
    }
}
