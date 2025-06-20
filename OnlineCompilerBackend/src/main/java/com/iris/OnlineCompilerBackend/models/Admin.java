package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OC_TBL_ADMIN_CREDENTIALS")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ADMIN_ID")
    private String adminId;

    @Column(name = "ADMIN_PASSWORD")
    private String adminPassword;

    @Column(name = "SALT")
    private String salt;

    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    @Column(name = "Full_NAME")
    private String fullName;

    @Column(name = "IS_ADMIN")
    private Boolean isAdmin;

    public Admin() {
    }

    public Admin(Long id, String adminId, String adminPassword, String salt, Date lastLogin, String fullName, Boolean isAdmin) {
        this.id = id;
        this.adminId = adminId;
        this.adminPassword = adminPassword;
        this.salt = salt;
        this.lastLogin = lastLogin;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}