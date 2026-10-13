package com.smartoffice.dto;

public class ProfileResponse {
    private Long userId;
    private Long employeeId;
    private String name;
    private String email;
    private String phone;
    private String role;
    public ProfileResponse(Long userId, Long employeeId, String name, String email, String phone, String role) {
        this.userId=userId; this.employeeId=employeeId; this.name=name; this.email=email; this.phone=phone; this.role=role;
    }
    public Long getUserId(){return userId;} public Long getEmployeeId(){return employeeId;} public String getName(){return name;}
    public String getEmail(){return email;} public String getPhone(){return phone;} public String getRole(){return role;}
}
