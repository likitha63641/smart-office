package com.smartoffice.dto;

import com.smartoffice.model.IssuePriority;
import com.smartoffice.model.IssueStatus;

public class IssueResponse {

	private Long id;
	
	private String title;
	
	private String description;
	
	private String category;
	
	private String reportedBy;

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private Long technicianId;
    private IssueStatus status;
    private IssuePriority priority;
    private String attachmentName;
    private String attachmentPath;
	public IssueResponse() {
		
	}

	public IssueResponse(
	        Long id,
	        String title,
	        String description,
	        String category,
	        String reportedBy,
	        Long employeeId,
	        String employeeName,
	        String employeeEmail,
	        Long technicianId,
	        IssueStatus status,
	        IssuePriority priority,
	        String attachmentName,
	        String attachmentPath) {

	    this.id = id;
	    this.title = title;
	    this.description = description;
	    this.category = category;
	    this.reportedBy = reportedBy;
	    this.employeeId = employeeId;
	    this.employeeName = employeeName;
	    this.employeeEmail = employeeEmail;
	    this.technicianId = technicianId;
	    this.status = status;
	    this.priority = priority;
	    this.attachmentName = attachmentName;
	    this.attachmentPath = attachmentPath;
	}
	public Long getId() {
	    return id;
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

	 public IssueStatus getStatus() {
		 return status;
	 }
 
	 public Long getTechnicianId() {
		    return technicianId;
		}
	 public IssuePriority getPriority() {
		    return priority;
		}

	 public String getEmployeeName() {
		 return employeeName;
	 }

	 public void setEmployeeName(String employeeName) {
		 this.employeeName = employeeName;
	 }

	 public String getEmployeeEmail() {
		 return employeeEmail;
	 }

	 public void setEmployeeEmail(String employeeEmail) {
		 this.employeeEmail = employeeEmail;
	 }

	 public String getAttachmentName() {
		 return attachmentName;
	 }

	 public void setAttachmentName(String attachmentName) {
		 this.attachmentName = attachmentName;
	 }

	 public String getAttachmentPath() {
		 return attachmentPath;
	 }

	 public void setAttachmentPath(String attachmentPath) {
		 this.attachmentPath = attachmentPath;
	 }
	 
	 
}
