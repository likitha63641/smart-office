package com.smartoffice.dto;

public class AuthResponse {
    private String message;
    private String token;
    private String role;
    private Long employeeId;
    private String name;
    private String email;

    public AuthResponse(String message, String token, String role, Long employeeId, String name, String email) {
        this.message = message; this.token = token; this.role = role;
        this.employeeId = employeeId; this.name = name; this.email = email;
    }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
