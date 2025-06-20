package com.iris.OnlineCompilerBackend.dtos;

public class AdminAuthenticationResDTO {

    private String adminId;

    private Boolean isAdmin;

    private String adminFullName;

    public AdminAuthenticationResDTO() {
    }

    public AdminAuthenticationResDTO(String adminId, Boolean isAdmin, String adminFullName) {
        this.adminId = adminId;
        this.isAdmin = isAdmin;
        this.adminFullName = adminFullName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }
}
