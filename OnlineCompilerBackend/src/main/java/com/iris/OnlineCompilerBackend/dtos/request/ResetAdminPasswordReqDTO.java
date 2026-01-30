package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;

public class ResetAdminPasswordReqDTO {
    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "OLD PASSWORD REQUIRED!")
    private String adminOldPassword;

    @NotBlank(message = "NEW PASSWORD REQUIRED!")
    private String adminNewPassword;

    @NotBlank(message = "CONFIRM PASSWORD REQUIRED!")
    private String adminNewConfirmPassword;

    public ResetAdminPasswordReqDTO() {
    }

    public ResetAdminPasswordReqDTO(String adminId, String adminOldPassword, String adminNewPassword, String adminNewConfirmPassword) {
        this.adminId = adminId;
        this.adminOldPassword = adminOldPassword;
        this.adminNewPassword = adminNewPassword;
        this.adminNewConfirmPassword = adminNewConfirmPassword;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminOldPassword() {
        return adminOldPassword;
    }

    public void setAdminOldPassword(String adminOldPassword) {
        this.adminOldPassword = adminOldPassword;
    }

    public String getAdminNewPassword() {
        return adminNewPassword;
    }

    public void setAdminNewPassword(String adminNewPassword) {
        this.adminNewPassword = adminNewPassword;
    }

    public String getAdminNewConfirmPassword() {
        return adminNewConfirmPassword;
    }

    public void setAdminNewConfirmPassword(String adminNewConfirmPassword) {
        this.adminNewConfirmPassword = adminNewConfirmPassword;
    }
}
