package com.iris.OnlineCompilerBackend.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "OC_TBL_ADMIN")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
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

    @Column(name = "LAST_ACCESS_TOKEN")
    private String lastAccesstoken;

    @Column(name = "ACCESS_TOKEN_CREATED_ON")
    private String accessTokenCreatedOn;

    @Column(name = "ACCESS_TOKEN_LAST_ACCESSED_ON")
    private String accessTokenLastAccessedOn;

    @Column(name = "IS_EXPIRED")
    private Boolean accessTokenIsExpired;

    @JoinColumn(name = "CREATED_BY_FK")
    @ManyToOne
    private Admin createdByFk;

    public Admin() {
    }

    public Admin(Long id, String adminId, String adminPassword, String salt, Date lastLogin, String fullName, Boolean isAdmin, String lastAccesstoken, String accessTokenCreatedOn, String accessTokenLastAccessedOn, Boolean accessTokenIsExpired, Admin createdByFk) {
        this.id = id;
        this.adminId = adminId;
        this.adminPassword = adminPassword;
        this.salt = salt;
        this.lastLogin = lastLogin;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
        this.lastAccesstoken = lastAccesstoken;
        this.accessTokenCreatedOn = accessTokenCreatedOn;
        this.accessTokenLastAccessedOn = accessTokenLastAccessedOn;
        this.accessTokenIsExpired = accessTokenIsExpired;
        this.createdByFk = createdByFk;
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

    public String getLastAccesstoken() {
        return lastAccesstoken;
    }

    public void setLastAccesstoken(String lastAccesstoken) {
        this.lastAccesstoken = lastAccesstoken;
    }

    public String getAccessTokenCreatedOn() {
        return accessTokenCreatedOn;
    }

    public void setAccessTokenCreatedOn(String accessTokenCreatedOn) {
        this.accessTokenCreatedOn = accessTokenCreatedOn;
    }

    public String getAccessTokenLastAccessedOn() {
        return accessTokenLastAccessedOn;
    }

    public void setAccessTokenLastAccessedOn(String accessTokenLastAccessedOn) {
        this.accessTokenLastAccessedOn = accessTokenLastAccessedOn;
    }

    public Boolean getAccessTokenIsExpired() {
        return accessTokenIsExpired;
    }

    public void setAccessTokenIsExpired(Boolean accessTokenIsExpired) {
        this.accessTokenIsExpired = accessTokenIsExpired;
    }

    public Admin getCreatedByFk() {
        return createdByFk;
    }

    public void setCreatedByFk(Admin createdByFk) {
        this.createdByFk = createdByFk;
    }
}