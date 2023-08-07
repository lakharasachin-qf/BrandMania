package com.make.mybrand.Model;

public class CommonListModel {
    public static final int LAYOUT_BLOCK = 0;
    private String id;
    private String name;
    private int layoutType;
    private String imageUrl;
    private String thumbnail;


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CommonListModel() {
    }

    public static int getLayoutBlock() {
        return LAYOUT_BLOCK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
