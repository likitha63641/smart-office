package com.smartoffice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_history", indexes = @Index(name = "idx_history_issue", columnList = "issue_id"))
public class IssueHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Enumerated(EnumType.STRING)
    private IssueStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private IssueStatus newStatus;

    private String changedBy;
    @Column(nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();

    public IssueHistory() {}
    public IssueHistory(Issue issue, IssueStatus oldStatus, IssueStatus newStatus, String changedBy) {
        this.issue = issue;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
    }
    public Long getId() { return id; }
    public IssueStatus getOldStatus() { return oldStatus; }
    public IssueStatus getNewStatus() { return newStatus; }
    public String getChangedBy() { return changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }
}
