package com.iris.OnlineCompilerBackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResetAdminPasswordReqDTO {
    @NotBlank(message = "ADMIN ID REQUIRED!")
    private String adminId;

    @NotBlank(message = "OLD PASSWORD REQUIRED!")
    private String adminOldPassword;

    @NotBlank(message = "NEW PASSWORD REQUIRED!")
//    @Size(min = 8, message = "Password must be at least 8 characters long")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
//             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
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
