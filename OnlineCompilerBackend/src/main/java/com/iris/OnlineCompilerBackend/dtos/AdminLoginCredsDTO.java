package com.iris.OnlineCompilerBackend.dtos;

public class AdminLoginCredsDTO {
    private String adminId;

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
