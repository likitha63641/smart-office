package com.smartoffice.dto;

import com.smartoffice.model.IssuePriority;

import jakarta.validation.constraints.NotBlank;

public class IssueRequest {

	@NotBlank(message = "Title must not be blank")
	private String title;
	
	@NotBlank(message = "Description must not be blank")
	private String description;
	
	@NotBlank(message = "Category must not be blank")
	private String category;
	
	private String reportedBy;
	
	private Long employeeId;
	
	private IssuePriority priority;
	
	public IssueRequest() {
		
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getReportedBy() {
		return reportedBy;
	}


	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}
	
	public Long getEmployeeId() {
	    return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
	    this.employeeId = employeeId;
	}
	
	public IssuePriority getPriority() {
	    return priority;
	}

	public void setPriority(IssuePriority priority) {
	    this.priority = priority;
	}
}
