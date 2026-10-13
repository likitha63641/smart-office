package com.smartoffice.dto;

public class IssueStatisticsResponse {

    private long totalIssues;
    private long newIssues;
    private long reviewedIssues;
    private long assignedIssues;
    private long inProgressIssues;
    private long resolvedIssues;
    private long closedIssues;

    private long lowPriorityIssues;
    private long mediumPriorityIssues;
    private long highPriorityIssues;
    private long criticalIssues;

    public IssueStatisticsResponse() {
    }

    public long getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(long totalIssues) {
        this.totalIssues = totalIssues;
    }

    public long getReviewedIssues() { return reviewedIssues; }

    public void setReviewedIssues(long reviewedIssues) { this.reviewedIssues = reviewedIssues; }

    public long getNewIssues() {
        return newIssues;
    }

    public void setNewIssues(long newIssues) {
        this.newIssues = newIssues;
    }

    public long getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(long assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public long getInProgressIssues() {
        return inProgressIssues;
    }

    public void setInProgressIssues(long inProgressIssues) {
        this.inProgressIssues = inProgressIssues;
    }

    public long getResolvedIssues() {
        return resolvedIssues;
    }

    public void setResolvedIssues(long resolvedIssues) {
        this.resolvedIssues = resolvedIssues;
    }

    public long getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(long closedIssues) {
        this.closedIssues = closedIssues;
    }

    public long getLowPriorityIssues() {
        return lowPriorityIssues;
    }

    public void setLowPriorityIssues(long lowPriorityIssues) {
        this.lowPriorityIssues = lowPriorityIssues;
    }

    public long getMediumPriorityIssues() {
        return mediumPriorityIssues;
    }

    public void setMediumPriorityIssues(long mediumPriorityIssues) {
        this.mediumPriorityIssues = mediumPriorityIssues;
    }

    public long getHighPriorityIssues() {
        return highPriorityIssues;
    }

    public void setHighPriorityIssues(long highPriorityIssues) {
        this.highPriorityIssues = highPriorityIssues;
    }

    public long getCriticalIssues() {
        return criticalIssues;
    }

    public void setCriticalIssues(long criticalIssues) {
        this.criticalIssues = criticalIssues;
    }
}