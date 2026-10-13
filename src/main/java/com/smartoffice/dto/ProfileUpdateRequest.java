package com.smartoffice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ProfileUpdateRequest {
    @NotBlank private String name;
    @NotBlank @Pattern(regexp="^[0-9]{10}$", message="Phone must contain exactly 10 digits") private String phone;
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getPhone(){return phone;} public void setPhone(String phone){this.phone=phone;}
}
