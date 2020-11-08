package com.app.brandmania.Model;

import java.util.ArrayList;

public class BrandListItem {

    public static final int LAYOUT_BRANDLIST=1;

    public static final int LAYOUT_LOADING = 33;
    ArrayList<ImageList> examTimeTables;
    ArrayList<FrameItem> frame;
    private int layoutType;
    private String id;
    private String name;
    private String categoryId;
    private String categoryName;
    private String website;
    private String email;
    private String phonenumber;
    private String address;
    private String logo;
    private String framee;
    private int Image;
    private String description;
    private String tag;
    private String status;


    public BrandListItem() {
    }

    public ArrayList<FrameItem> getFrame() {
        return frame;
    }

    public void setFrame(ArrayList<FrameItem> frame) {
        this.frame = frame;
    }

    public static int getLayoutBrandlist() {
        return LAYOUT_BRANDLIST;
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

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ImageList> getExamTimeTables() {
        return examTimeTables;
    }

    public void setExamTimeTables(ArrayList<ImageList> examTimeTables) {
        this.examTimeTables = examTimeTables;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFramee() {
        return framee;
    }

    public void setFramee(String framee) {
        this.framee = framee;
    }
}
