package com.app.brandadmin.Activity.admin;

public class CatModel {
    private String imageType;
    private String id;
    private String thumbnailUrl;
    private String categoryName;
    private boolean isFree;
    private boolean isActive;
    private int layoutType;


    public int getlayoutType() {
        return layoutType;
    }

    public void setlayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getImageType() {
        return imageType;
    }

    public CatModel setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

    public String getId() {
        return id;
    }

    public CatModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public CatModel setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public CatModel setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public boolean isFree() {
        return isFree;
    }

    public CatModel setFree(boolean free) {
        isFree = free;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public CatModel setActive(boolean active) {
        isActive = active;
        return this;
    }
}
