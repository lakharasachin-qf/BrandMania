package com.app.brandadmin.Model;

public class VersionListIItem {
    private String id;
    private String appliactionVersion;
    private String message;
    private String forcefullyUpdate;
    private String isNew;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;

    public VersionListIItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppliactionVersion() {
        return appliactionVersion;
    }

    public void setAppliactionVersion(String appliactionVersion) {
        this.appliactionVersion = appliactionVersion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForcefullyUpdate() {
        return forcefullyUpdate;
    }

    public void setForcefullyUpdate(String forcefullyUpdate) {
        this.forcefullyUpdate = forcefullyUpdate;
    }

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
