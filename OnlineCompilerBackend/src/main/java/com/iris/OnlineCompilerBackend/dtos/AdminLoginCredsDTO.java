package com.iris.OnlineCompilerBackend.dtos;

import jakarta.validation.constraints.NotBlank;

public class AdminLoginCredsDTO {
    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "ADMIN PASSWORD REQUIRED!")
    private String adminPassword;

    public AdminLoginCredsDTO() {
    }

    public AdminLoginCredsDTO(String adminId, String adminPassword) {
        this.adminId = adminId;
        this.adminPassword = adminPassword;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
