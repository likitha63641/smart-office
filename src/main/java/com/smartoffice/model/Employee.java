package com.smartoffice.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;



@Entity
@Table(name = "employees")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@NotBlank(message = "Name must not be blank")
	private String name;
	
	@NotBlank(message = "Email must not be blank")
	@Email(message = "Email must be valid")
	private String email;
	
	@NotBlank(message = "Phone must not be blank")
	@Pattern(
		    regexp = "^[0-9]{10}$",
		    message = "Phone must contain exactly 10 digits"
		)
	private String phone;
	
	@OneToMany(mappedBy = "employee")
	private List<Issue> issues;
	public Employee() {
	
	}

	public Employee(String name, String email, String phone) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<Issue> getIssues() {
	    return issues;
	}

	public void setIssues(List<Issue> issues) {
	    this.issues = issues;
	}
	
}
