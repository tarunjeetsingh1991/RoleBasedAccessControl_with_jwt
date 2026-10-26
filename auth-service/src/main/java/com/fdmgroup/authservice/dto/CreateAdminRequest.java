package com.fdmgroup.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAdminRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public CreateAdminRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}