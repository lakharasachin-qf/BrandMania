package com.app.brandmania.Model;

public class ImageList {
    public static final int LAYOUT_IMAGE_CATEGORY = 1;
    public static final int LAYOUT_IMAGE_CATEGORY_BY_ID = 2;
    public static final int LAYOUT_LOADING = 30;
    private int layoutType;
    private String name;
    private String id;
    private String imagecatid;
    private String imageid;
    private String frame;
    private String logo;

    public ImageList() {
    }
    public static int getLayoutImageCategory() {
        return LAYOUT_IMAGE_CATEGORY;
    }
    public static int getLayoutImageCategoryById() {
        return LAYOUT_IMAGE_CATEGORY_BY_ID;
    }
    public static int getLayoutLoading() {
        return LAYOUT_LOADING;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagecatid() {
        return imagecatid;
    }

    public void setImagecatid(String imagecatid) {
        this.imagecatid = imagecatid;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
