package com.smartoffice.repository;

import com.smartoffice.model.IssueHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
    List<IssueHistory> findByIssueIdOrderByChangedAtAsc(Long issueId);
}
