package com.smartoffice.service;

import com.smartoffice.dto.IssueHistoryResponse;
import com.smartoffice.model.Issue;
import com.smartoffice.model.IssueHistory;
import com.smartoffice.model.IssueStatus;
import com.smartoffice.repository.IssueHistoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IssueHistoryService {
    private final IssueHistoryRepository repository;
    public IssueHistoryService(IssueHistoryRepository repository) { this.repository=repository; }
    public void record(Issue issue, IssueStatus oldStatus, IssueStatus newStatus, String changedBy) {
        repository.save(new IssueHistory(issue, oldStatus, newStatus, changedBy));
    }
    public List<IssueHistoryResponse> get(Long issueId) {
        return repository.findByIssueIdOrderByChangedAtAsc(issueId).stream()
                .map(h -> new IssueHistoryResponse(h.getOldStatus(), h.getNewStatus(), h.getChangedBy(), h.getChangedAt())).toList();
    }
}
