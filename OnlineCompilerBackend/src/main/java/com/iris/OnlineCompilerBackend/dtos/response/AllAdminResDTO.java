package com.iris.OnlineCompilerBackend.dtos.response;

public class AllAdminResDTO {
    private String adminFullName;
    private String adminId;

    public AllAdminResDTO() {
    }

    public AllAdminResDTO(String adminFullName, String adminId) {
        this.adminFullName = adminFullName;
        this.adminId = adminId;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
