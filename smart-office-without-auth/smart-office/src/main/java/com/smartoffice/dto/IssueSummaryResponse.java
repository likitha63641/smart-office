package com.smartoffice.dto;

import com.smartoffice.model.IssueStatus;

public class IssueSummaryResponse {

    private Long id;
    private String title;
    private String category;
    private IssueStatus status;

    public IssueSummaryResponse() {
    }

    public IssueSummaryResponse(
            Long id,
            String title,
            String category,
            IssueStatus status) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public IssueStatus getStatus() {
        return status;
    }
}