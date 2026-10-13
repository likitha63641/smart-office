package com.smartoffice.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank private String name;
    private boolean active = true;
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
}
