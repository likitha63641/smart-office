package com.smartoffice.dto;

import java.util.List;

public class EmployeeResponse {
	 private Long id;
	    private String name;
	    private String email;
	    private String phone;
	    private List<IssueSummaryResponse> issues;
	    
	    public EmployeeResponse() {
	    }

	    public EmployeeResponse(Long id, String name, String email, String phone,  List<IssueSummaryResponse> issues ) {
	        this.id = id;
	        this.name = name;
	        this.email = email;
	        this.phone = phone;
	        this.issues = issues;
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public String getPhone() {
	        return phone;
	    }
	    public List<IssueSummaryResponse> getIssues() {
	        return issues;
	    }

	    public void setIssues(List<IssueSummaryResponse> issues) {
	        this.issues = issues;
	    } 
	    
}
