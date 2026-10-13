package com.smartoffice.dto;


import com.smartoffice.model.IssueStatus;

public class IssueStatusRequest {

    private IssueStatus status;

    public IssueStatusRequest() {
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}