package com.smartoffice.dto;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id; private Long issueId; private String message; private boolean read; private LocalDateTime createdAt;
    public NotificationResponse(Long id, Long issueId, String message, boolean read, LocalDateTime createdAt) {
        this.id=id; this.issueId=issueId; this.message=message; this.read=read; this.createdAt=createdAt;
    }
    public Long getId(){return id;} public Long getIssueId(){return issueId;} public String getMessage(){return message;}
    public boolean isRead(){return read;} public LocalDateTime getCreatedAt(){return createdAt;}
}
