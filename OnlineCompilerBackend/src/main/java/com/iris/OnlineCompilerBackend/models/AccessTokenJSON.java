package com.iris.OnlineCompilerBackend.models;

import java.util.Date;
import java.util.UUID;

public class AccessTokenJSON {
    private String userName;
    private Boolean userType;
    private Date createdOn;
    private UUID uuid;

    public AccessTokenJSON() {
    }

    public AccessTokenJSON(String userName, Boolean userType, Date createdOn, UUID uuid) {
        this.userName = userName;
        this.userType = userType;
        this.createdOn = createdOn;
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getUserType() {
        return userType;
    }

    public void setUserType(Boolean userType) {
        this.userType = userType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
