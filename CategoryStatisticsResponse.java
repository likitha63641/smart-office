package com.smartoffice.dto;

public class CategoryStatisticsResponse {

    private String category;
    private long count;

    public CategoryStatisticsResponse() {
    }

    public CategoryStatisticsResponse(String category, long count) {
        this.category = category;
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public long getCount() {
        return count;
    }
}