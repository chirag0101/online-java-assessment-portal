package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewAdminReqDTO {
    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "ADMIN FULL NAME REQUIRED!")
    private String adminFullName;

    @NotNull(message = "IS ADMIN REQUIRED!")
    private Boolean isAdmin;

    public NewAdminReqDTO() {
    }

    public NewAdminReqDTO(String adminId, String adminFullName, Boolean isAdmin) {
        this.adminId = adminId;
        this.adminFullName = adminFullName;
        this.isAdmin = isAdmin;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
