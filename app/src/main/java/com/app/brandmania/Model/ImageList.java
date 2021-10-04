package com.app.brandmania.Model;

import java.util.ArrayList;

public class ImageList {
    public static final int LAYOUT_IMAGE_CATEGORY = 1;
    public static final int LAYOUT_IMAGE_CATEGORY_BY_ID = 2;
    public static final int LAYOUT_FRAME = 3;
    public static final int LAYOUT_FRAME_CATEGORY = 4;
    public static final int LAYOUT_FRAME_CATEGORY_BY_ID=5;
    public static final int LAYOUT_DAILY_IMAGES=6;
    public static final int LAYOUT_DAILY_ROUND_IMAGES=7;
    public static final int LAYOUT_LOADING = 30;

    private int layoutType;
    private String name;
    private String id;
    private String imagecatid;
    private String imageid;
    private String frame;
    private String frameId;
    private String logo;
    private int index;

    public int getIndex() {
        return index;
    }

    public ImageList setIndex(int index) {
        this.index = index;
        return this;
    }

    private String frame1;
    private String frame1Id;
    private boolean isCustom=true;
    private boolean isImageFree;
    private String brandId;
    private Links links;
    private ArrayList<ImageList> catogaryImagesList;
    private String isFrame;
    private String x_conrdinate;
    private String y_cordinate;
    private String textX_Cordinate;
    private String textY_Cordinate;

    public String getTextX_Cordinate() {
        return textX_Cordinate;
    }

    public ImageList setTextX_Cordinate(String textX_Cordinate) {
        this.textX_Cordinate = textX_Cordinate;
        return this;
    }

    public String getTextY_Cordinate() {
        return textY_Cordinate;
    }

    public ImageList setTextY_Cordinate(String textY_Cordinate) {
        this.textY_Cordinate = textY_Cordinate;
        return this;
    }

    public String getIsFrame() {
        return isFrame;
    }

    public ImageList setIsFrame(String isFrame) {
        this.isFrame = isFrame;
        return this;
    }

    public String getBrandId() {
        return brandId;
    }

    public ImageList setBrandId(String brandId) {
        this.brandId = brandId;
        return this;
    }

    public ArrayList<ImageList> getCatogaryImagesList() {
        return catogaryImagesList;
    }

    public ImageList setCatogaryImagesList(ArrayList<ImageList> catogaryImagesList) {
        this.catogaryImagesList = catogaryImagesList;
        return this;
    }

    public Links getLinks() {
        return links;
    }

    public ImageList setLinks(Links links) {
        this.links = links;
        return this;
    }

    public boolean isImageFree() {
        return isImageFree;
    }

    public ImageList setImageFree(boolean imageFree) {
        isImageFree = imageFree;
        return this;
    }

    /// if
    public boolean isCustom() {
        return isCustom;
    }

    public ImageList setCustom(boolean custom) {
        isCustom = custom;
        return this;
    }

    public ImageList() {
    }
    public static int getLayoutImageCategory() {
        return LAYOUT_IMAGE_CATEGORY;
    }
    public static int getLayoutImageCategoryById() {
        return LAYOUT_IMAGE_CATEGORY_BY_ID;
    }
    public static int getLayoutFrame() {
        return LAYOUT_FRAME;
    }
    public static int getLayoutFrameCategory() {
        return LAYOUT_FRAME_CATEGORY;
    }
    public static int getLayoutFrameByIdCategory() {
        return LAYOUT_FRAME_CATEGORY_BY_ID;
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

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public String getFrame1() {
        return frame1;
    }

    public void setFrame1(String frame1) {
        this.frame1 = frame1;
    }

    public String getFrame1Id() {
        return frame1Id;
    }

    public void setFrame1Id(String frame1Id) {
        this.frame1Id = frame1Id;
    }

    public String getX_conrdinate() {
        return x_conrdinate;
    }

    public void setX_conrdinate(String x_conrdinate) {
        this.x_conrdinate = x_conrdinate;
    }

    public String getY_cordinate() {
        return y_cordinate;
    }

    public void setY_cordinate(String y_cordinate) {
        this.y_cordinate = y_cordinate;
    }
}
