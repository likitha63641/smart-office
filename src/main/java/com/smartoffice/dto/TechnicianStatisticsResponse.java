package com.smartoffice.dto;

public class TechnicianStatisticsResponse {

    private Long technicianId;
    private String technicianName;
    private String specialization;
    private long issueCount;

    public TechnicianStatisticsResponse() {
    }

    public TechnicianStatisticsResponse(
            Long technicianId,
            String technicianName,
            String specialization,
            long issueCount) {

        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.specialization = specialization;
        this.issueCount = issueCount;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public long getIssueCount() {
        return issueCount;
    }
}