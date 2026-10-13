package com.smartoffice.service;
import java.util.List;
import com.smartoffice.model.Employee;
import com.smartoffice.dto.CategoryStatisticsResponse;
import com.smartoffice.dto.IssueStatisticsResponse;
import com.smartoffice.dto.TechnicianStatisticsResponse;
import com.smartoffice.exception.EmployeeNotFoundException;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.smartoffice.repository.IssueRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.smartoffice.exception.IssueNotFoundException;
import com.smartoffice.exception.TechnicianNotFoundException;
import com.smartoffice.model.Issue;
import com.smartoffice.model.IssuePriority;
import com.smartoffice.model.IssueStatus;
import com.smartoffice.model.Technician;
import com.smartoffice.repository.EmployeeRepository;
import com.smartoffice.repository.TechnicianRepository;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private TechnicianRepository technicianRepository;
    
    private IssueService issueService;
    @BeforeEach
    void setUp() {
        issueService = new IssueService(
                issueRepository,
                employeeRepository,
                technicianRepository
        );
    }
    
    @Test
    void createIssue_shouldSaveAndReturnIssue() {

        Issue issue = new Issue();

        when(issueRepository.save(issue)).thenReturn(issue);

        Issue result = issueService.createIssue(issue);

        assertSame(issue, result);
        verify(issueRepository).save(issue);
    }
    @Test
    void getIssueById_shouldReturnIssue_whenIssueExists() {

        Long id = 1L;
        Issue issue = new Issue();

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        Issue result = issueService.getIssueById(id);

        assertSame(issue, result);

        verify(issueRepository).findById(id);
    }
    
    @Test
    void getIssueById_shouldThrowException_whenIssueDoesNotExist() {

        Long id = 99L;

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.getIssueById(id)
        );

        verify(issueRepository).findById(id);
    }
    
    @Test
    void getAllIssues_shouldReturnAllIssues() {

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findAll()).thenReturn(issues);

        List<Issue> result = issueService.getAllIssues();

        assertSame(issues, result);

        verify(issueRepository).findAll();
    }
    
    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() {

        Long employeeId = 1L;
        Employee employee = new Employee();

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.of(employee));

        Employee result = issueService.getEmployeeById(employeeId);

        assertSame(employee, result);

        verify(employeeRepository).findById(employeeId);
    }
    
    @Test
    void getEmployeeById_shouldThrowException_whenEmployeeDoesNotExist() {

        Long employeeId = 99L;

        when(employeeRepository.findById(employeeId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> issueService.getEmployeeById(employeeId)
        );

        verify(employeeRepository).findById(employeeId);
    }
    
    @Test
    void deleteIssue_shouldDeleteAndReturnIssue() {

        Long id = 1L;
        Issue issue = new Issue();

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        Issue result = issueService.deleteIssue(id);

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).delete(issue);
    }
    @Test
    void deleteIssue_shouldThrowException_whenIssueDoesNotExist() {

        Long id = 99L;

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.deleteIssue(id)
        );

        verify(issueRepository).findById(id);
    }
    @Test
    void updateAttachment_shouldUpdateAndReturnIssue() {

        Long id = 1L;
        Issue issue = new Issue();

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result = issueService.updateAttachment(
                id,
                "photo.jpg",
                "uploads/photo.jpg"
        );

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateAttachment_shouldThrowException_whenIssueDoesNotExist() {

        Long id = 99L;

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.updateAttachment(
                        id,
                        "photo.jpg",
                        "uploads/photo.jpg"
                )
        );

        verify(issueRepository).findById(id);
    }
    
    @Test
    void getIssuesByStatus_shouldReturnMatchingIssues() {

        IssueStatus status = IssueStatus.NEW;

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findByStatus(status))
                .thenReturn(issues);

        List<Issue> result = issueService.getIssuesByStatus(status);

        assertSame(issues, result);

        verify(issueRepository).findByStatus(status);
    }
    
    @Test
    void getIssuesByPriority_shouldReturnMatchingIssues() {

        IssuePriority priority = IssuePriority.HIGH;

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findByPriority(priority))
                .thenReturn(issues);

        List<Issue> result =
                issueService.getIssuesByPriority(priority);

        assertSame(issues, result);

        verify(issueRepository).findByPriority(priority);
    }
    @Test
    void getIssuesByCategory_shouldReturnMatchingIssues() {

        String category = "Electrical";

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findByCategoryIgnoreCase(category))
                .thenReturn(issues);

        List<Issue> result =
                issueService.getIssuesByCategory(category);

        assertSame(issues, result);

        verify(issueRepository).findByCategoryIgnoreCase(category);
    }
    @Test
    void searchIssues_shouldReturnMatchingIssues() {

        String keyword = "printer";

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword))
                .thenReturn(issues);

        List<Issue> result = issueService.searchIssues(keyword);

        assertSame(issues, result);

        verify(issueRepository)
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword);
    }
    @Test
    void getIssuesByEmployeeId_shouldReturnMatchingIssues() {

        Long employeeId = 1L;

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findByEmployeeId(employeeId))
                .thenReturn(issues);

        List<Issue> result =
                issueService.getIssuesByEmployeeId(employeeId);

        assertSame(issues, result);

        verify(issueRepository).findByEmployeeId(employeeId);
    }
    
    @Test
    void updateIssue_shouldUpdateAndReturnIssue() {

        Long id = 1L;

        Issue existingIssue = new Issue();
        Issue updatedIssue = new Issue();

        updatedIssue.setTitle("Updated issue");
        updatedIssue.setDescription("Updated description");
        updatedIssue.setCategory("Electrical");
        updatedIssue.setPriority(IssuePriority.HIGH);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(existingIssue));

        when(issueRepository.save(existingIssue))
                .thenReturn(existingIssue);

        Issue result = issueService.updateIssue(id, updatedIssue);

        assertSame(existingIssue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(existingIssue);
    }
    @Test
    void updateIssue_shouldThrowException_whenIssueDoesNotExist() {

        Long id = 99L;
        Issue updatedIssue = new Issue();

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.updateIssue(id, updatedIssue)
        );

        verify(issueRepository).findById(id);
    }
    @Test
    void updateIssueStatus_shouldChangeNewToAssigned() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.NEW);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result =
                issueService.updateIssueStatus(id, IssueStatus.ASSIGNED);

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateIssueStatus_shouldThrowException_whenNewIssueMovesToInvalidStatus() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.NEW);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        assertThrows(
                IllegalStateException.class,
                () -> issueService.updateIssueStatus(
                        id,
                        IssueStatus.IN_PROGRESS
                )
        );

        verify(issueRepository).findById(id);
    }
    @Test
    void updateIssueStatus_shouldChangeAssignedToInProgress() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.ASSIGNED);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result =
                issueService.updateIssueStatus(
                        id,
                        IssueStatus.IN_PROGRESS
                );

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateIssueStatus_shouldThrowException_whenAssignedIssueMovesToInvalidStatus() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.ASSIGNED);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        assertThrows(
                IllegalStateException.class,
                () -> issueService.updateIssueStatus(
                        id,
                        IssueStatus.RESOLVED
                )
        );

        verify(issueRepository).findById(id);
    }
    @Test
    void updateIssueStatus_shouldChangeInProgressToResolved() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.IN_PROGRESS);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result =
                issueService.updateIssueStatus(
                        id,
                        IssueStatus.RESOLVED
                );

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateIssueStatus_shouldChangeResolvedToClosed() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.RESOLVED);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result =
                issueService.updateIssueStatus(
                        id,
                        IssueStatus.CLOSED
                );

        assertSame(issue, result);

        verify(issueRepository).findById(id);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateIssueStatus_shouldMakeTechnicianAvailable_whenIssueIsResolved() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.IN_PROGRESS);

        Technician technician = new Technician();
        technician.setAvailable(false);

        issue.setTechnician(technician);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        when(technicianRepository.save(technician))
                .thenReturn(technician);

        when(issueRepository.save(issue))
                .thenReturn(issue);

        Issue result =
                issueService.updateIssueStatus(
                        id,
                        IssueStatus.RESOLVED
                );

        assertSame(issue, result);

        verify(technicianRepository).save(technician);
        verify(issueRepository).save(issue);
    }
    @Test
    void updateIssueStatus_shouldThrowException_whenIssueIsClosed() {

        Long id = 1L;

        Issue issue = new Issue();
        issue.setStatus(IssueStatus.CLOSED);

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.of(issue));

        assertThrows(
                IllegalStateException.class,
                () -> issueService.updateIssueStatus(
                        id,
                        IssueStatus.CLOSED
                )
        );

        verify(issueRepository).findById(id);
    }
    @Test
    void assignTechnician_shouldAssignAvailableTechnician() {

        Long issueId = 1L;
        Long technicianId = 2L;

        Issue issue = new Issue();

        Technician technician = new Technician();
        technician.setAvailable(true);

        when(issueRepository.findById(issueId))
                .thenReturn(java.util.Optional.of(issue));

        when(technicianRepository.findById(technicianId))
                .thenReturn(java.util.Optional.of(technician));

        Issue result =
                issueService.assignTechnician(issueId, technicianId);

        assertSame(issue, result);

        verify(issueRepository).findById(issueId);
        verify(technicianRepository).findById(technicianId);
        verify(technicianRepository).save(technician);
    }
    @Test
    void assignTechnician_shouldThrowException_whenTechnicianIsUnavailable() {

        Long issueId = 1L;
        Long technicianId = 2L;

        Issue issue = new Issue();

        Technician technician = new Technician();
        technician.setAvailable(false);

        when(issueRepository.findById(issueId))
                .thenReturn(java.util.Optional.of(issue));

        when(technicianRepository.findById(technicianId))
                .thenReturn(java.util.Optional.of(technician));

        assertThrows(
                IllegalStateException.class,
                () -> issueService.assignTechnician(
                        issueId,
                        technicianId
                )
        );

        verify(issueRepository).findById(issueId);
        verify(technicianRepository).findById(technicianId);
    }
    @Test
    void assignTechnician_shouldThrowException_whenIssueDoesNotExist() {

        Long issueId = 99L;
        Long technicianId = 2L;

        when(issueRepository.findById(issueId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.assignTechnician(
                        issueId,
                        technicianId
                )
        );

        verify(issueRepository).findById(issueId);
    }
    @Test
    void assignTechnician_shouldThrowException_whenTechnicianDoesNotExist() {

        Long issueId = 1L;
        Long technicianId = 99L;

        Issue issue = new Issue();

        when(issueRepository.findById(issueId))
                .thenReturn(java.util.Optional.of(issue));

        when(technicianRepository.findById(technicianId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                TechnicianNotFoundException.class,
                () -> issueService.assignTechnician(
                        issueId,
                        technicianId
                )
        );

        verify(issueRepository).findById(issueId);
        verify(technicianRepository).findById(technicianId);
    }
    
    @Test
    void getIssuesSortedByPriority_shouldReturnIssuesInPriorityOrder() {

        Issue low = new Issue();
        low.setPriority(IssuePriority.LOW);

        Issue critical = new Issue();
        critical.setPriority(IssuePriority.CRITICAL);

        Issue high = new Issue();
        high.setPriority(IssuePriority.HIGH);

        when(issueRepository.findAll())
                .thenReturn(new java.util.ArrayList<>(
                        List.of(low, critical, high)));

        List<Issue> result = issueService.getIssuesSortedByPriority();

        org.junit.jupiter.api.Assertions.assertEquals(
                IssuePriority.CRITICAL,
                result.get(0).getPriority());

        org.junit.jupiter.api.Assertions.assertEquals(
                IssuePriority.HIGH,
                result.get(1).getPriority());

        org.junit.jupiter.api.Assertions.assertEquals(
                IssuePriority.LOW,
                result.get(2).getPriority());

        verify(issueRepository).findAll();
    }
    @Test
    void getIssuesByFilter_shouldReturnFilteredIssues() {

        String category = "Electrical";
        IssuePriority priority = IssuePriority.HIGH;
        IssueStatus status = IssueStatus.NEW;

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findIssuesByOptionalFilters(
                category,
                priority,
                status
        )).thenReturn(issues);

        List<Issue> result =
                issueService.getIssuesByFilter(
                        category,
                        priority,
                        status
                );

        assertSame(issues, result);

        verify(issueRepository).findIssuesByOptionalFilters(
                category,
                priority,
                status
        );
    }
    
    @Test
    void getIssuesByFilterWithPagination_shouldReturnFilteredPage() {

        String category = "Electrical";
        IssuePriority priority = IssuePriority.HIGH;
        IssueStatus status = IssueStatus.NEW;

        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);

        Page<Issue> page =
                new org.springframework.data.domain.PageImpl<>(
                        List.of(new Issue(), new Issue())
                );

        when(issueRepository.findIssuesByOptionalFiltersWithPagination(
                category,
                priority,
                status,
                pageable
        )).thenReturn(page);

        Page<Issue> result =
                issueService.getIssuesByFilterWithPagination(
                        category,
                        priority,
                        status,
                        pageable
                );

        assertSame(page, result);

        verify(issueRepository).findIssuesByOptionalFiltersWithPagination(
                category,
                priority,
                status,
                pageable
        );
    }
    
    @Test
    void getIssuesWithPagination_shouldReturnPage() {

        Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, 10);

        Page<Issue> page =
                new org.springframework.data.domain.PageImpl<>(
                        List.of(new Issue(), new Issue())
                );

        when(issueRepository.findAll(pageable))
                .thenReturn(page);

        Page<Issue> result =
                issueService.getIssuesWithPagination(pageable);

        assertSame(page, result);

        verify(issueRepository).findAll(pageable);
    }
    
    @Test
    void getIssuesByPriorityWithPagination_shouldReturnPage() {

        Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, 10);

        Page<Issue> page =
                new org.springframework.data.domain.PageImpl<>(
                        List.of(new Issue(), new Issue())
                );

        when(issueRepository.findAllByPriorityOrder(pageable))
                .thenReturn(page);

        Page<Issue> result =
                issueService.getIssuesByPriorityWithPagination(pageable);

        assertSame(page, result);

        verify(issueRepository).findAllByPriorityOrder(pageable);
    }
    @Test
    void getAllIssuesWithEmployeeAndTechnicianUsingEntityGraph_shouldReturnIssues() {

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findAllWithEmployeeAndTechnicianUsingEntityGraph())
                .thenReturn(issues);

        List<Issue> result =
                issueService.getAllIssuesWithEmployeeAndTechnicianUsingEntityGraph();

        assertSame(issues, result);

        verify(issueRepository)
                .findAllWithEmployeeAndTechnicianUsingEntityGraph();
    }
    
    @Test
    void getAllIssuesWithEmployeeAndTechnician_shouldReturnIssues() {

        List<Issue> issues = List.of(
                new Issue(),
                new Issue()
        );

        when(issueRepository.findAllWithEmployeeAndTechnician())
                .thenReturn(issues);

        List<Issue> result =
                issueService.getAllIssuesWithEmployeeAndTechnician();

        assertSame(issues, result);

        verify(issueRepository)
                .findAllWithEmployeeAndTechnician();
    }
    @Test
    void getIssueStatistics_shouldReturnStatistics() {

        when(issueRepository.count()).thenReturn(10L);

        when(issueRepository.countByStatus(IssueStatus.NEW))
                .thenReturn(2L);

        when(issueRepository.countByStatus(IssueStatus.ASSIGNED))
                .thenReturn(3L);

        when(issueRepository.countByStatus(IssueStatus.IN_PROGRESS))
                .thenReturn(2L);

        when(issueRepository.countByStatus(IssueStatus.RESOLVED))
                .thenReturn(2L);

        when(issueRepository.countByStatus(IssueStatus.CLOSED))
                .thenReturn(1L);

        when(issueRepository.countByPriority(IssuePriority.LOW))
                .thenReturn(2L);

        when(issueRepository.countByPriority(IssuePriority.MEDIUM))
                .thenReturn(3L);

        when(issueRepository.countByPriority(IssuePriority.HIGH))
                .thenReturn(4L);

        when(issueRepository.countByPriority(IssuePriority.CRITICAL))
                .thenReturn(1L);

        IssueStatisticsResponse result =
                issueService.getIssueStatistics();

        org.junit.jupiter.api.Assertions.assertEquals(
                10L, result.getTotalIssues());

        org.junit.jupiter.api.Assertions.assertEquals(
                2L, result.getNewIssues());

        org.junit.jupiter.api.Assertions.assertEquals(
                3L, result.getAssignedIssues());

        org.junit.jupiter.api.Assertions.assertEquals(
                2L, result.getInProgressIssues());

        org.junit.jupiter.api.Assertions.assertEquals(
                2L, result.getResolvedIssues());

        org.junit.jupiter.api.Assertions.assertEquals(
                1L, result.getClosedIssues());
    }
    
    @Test
    void getCategoryStatistics_shouldReturnStatistics() {

        List<Object[]> results = List.of(
                new Object[]{"Electrical", 5L},
                new Object[]{"Network", 3L}
        );

        when(issueRepository.countIssuesByCategory())
                .thenReturn(results);

        List<CategoryStatisticsResponse> response =
                issueService.getCategoryStatistics();

        org.junit.jupiter.api.Assertions.assertEquals(
                2, response.size());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Electrical",
                response.get(0).getCategory());

        org.junit.jupiter.api.Assertions.assertEquals(
                5L,
                response.get(0).getCount());

        verify(issueRepository).countIssuesByCategory();
    }
    @Test
    void getTechnicianStatistics_shouldReturnStatistics() {

        List<Object[]> results = List.of(
                new Object[]{1L, "John", "Electrical", 5L},
                new Object[]{2L, "David", "Network", 3L}
        );

        when(issueRepository.countIssuesByTechnician())
                .thenReturn(results);

        List<TechnicianStatisticsResponse> response =
                issueService.getTechnicianStatistics();

        org.junit.jupiter.api.Assertions.assertEquals(
                2, response.size());

        org.junit.jupiter.api.Assertions.assertEquals(
                1L,
                response.get(0).getTechnicianId());

        org.junit.jupiter.api.Assertions.assertEquals(
                "John",
                response.get(0).getTechnicianName());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Electrical",
                response.get(0).getSpecialization());

        org.junit.jupiter.api.Assertions.assertEquals(
                5L,
                response.get(0).getIssueCount());

        verify(issueRepository).countIssuesByTechnician();
    }
    
    @Test
    void getIssuesByStatus_shouldCallRepositoryWithGivenStatus() {

        IssueStatus status = IssueStatus.RESOLVED;

        when(issueRepository.findByStatus(status))
                .thenReturn(List.of());

        List<Issue> result =
                issueService.getIssuesByStatus(status);

        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());

        verify(issueRepository).findByStatus(status);
    }
    @Test
    void getIssuesByCategory_shouldReturnEmptyList_whenNoIssuesMatch() {

        String category = "Hardware";

        when(issueRepository.findByCategoryIgnoreCase(category))
                .thenReturn(List.of());

        List<Issue> result =
                issueService.getIssuesByCategory(category);

        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());

        verify(issueRepository)
                .findByCategoryIgnoreCase(category);
    }
    @Test
    void getIssuesByPriority_shouldReturnEmptyList_whenNoIssuesMatch() {

        IssuePriority priority = IssuePriority.LOW;

        when(issueRepository.findByPriority(priority))
                .thenReturn(List.of());

        List<Issue> result =
                issueService.getIssuesByPriority(priority);

        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());

        verify(issueRepository).findByPriority(priority);
    }
    @Test
    void getAllIssues_shouldReturnEmptyList_whenNoIssuesExist() {

        when(issueRepository.findAll())
                .thenReturn(List.of());

        List<Issue> result =
                issueService.getAllIssues();

        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());

        verify(issueRepository).findAll();
    }
    
    @Test
    void updateIssueStatus_shouldThrowException_whenIssueDoesNotExist() {

        Long id = 99L;

        when(issueRepository.findById(id))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                IssueNotFoundException.class,
                () -> issueService.updateIssueStatus(
                        id,
                        IssueStatus.ASSIGNED
                )
        );

        verify(issueRepository).findById(id);
    }
    
    @Test
    void assignTechnician_shouldSetTechnicianAndAssignedStatus() {

        Long issueId = 1L;
        Long technicianId = 2L;

        Issue issue = new Issue();

        Technician technician = new Technician();
        technician.setAvailable(true);

        when(issueRepository.findById(issueId))
                .thenReturn(java.util.Optional.of(issue));

        when(technicianRepository.findById(technicianId))
                .thenReturn(java.util.Optional.of(technician));

        issueService.assignTechnician(issueId, technicianId);

        org.junit.jupiter.api.Assertions.assertSame(
                technician,
                issue.getTechnician());

        org.junit.jupiter.api.Assertions.assertEquals(
                IssueStatus.ASSIGNED,
                issue.getStatus());

        org.junit.jupiter.api.Assertions.assertFalse(
                technician.isAvailable());

        verify(technicianRepository).save(technician);
    }
}