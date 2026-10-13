package com.smartoffice.dto;

public class TechnicianResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private boolean available;

    public TechnicianResponse() {
    }

    public TechnicianResponse(
            Long id,
            String name,
            String email,
            String phone,
            String specialization,
            boolean available) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.available = available;
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

    public String getSpecialization() {
        return specialization;
    }

    public boolean isAvailable() {
        return available;
    }
}