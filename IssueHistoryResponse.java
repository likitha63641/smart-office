package com.smartoffice.dto;

import com.smartoffice.model.IssueStatus;
import java.time.LocalDateTime;

public class IssueHistoryResponse {
    private IssueStatus oldStatus; private IssueStatus newStatus; private String changedBy; private LocalDateTime changedAt;
    public IssueHistoryResponse(IssueStatus oldStatus, IssueStatus newStatus, String changedBy, LocalDateTime changedAt) {
        this.oldStatus=oldStatus; this.newStatus=newStatus; this.changedBy=changedBy; this.changedAt=changedAt;
    }
    public IssueStatus getOldStatus(){return oldStatus;} public IssueStatus getNewStatus(){return newStatus;}
    public String getChangedBy(){return changedBy;} public LocalDateTime getChangedAt(){return changedAt;}
}
