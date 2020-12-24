package com.app.brandmania.Adapter;

public class FooterModel {
    public static final int LAYOUT_FRAME_ONE=1;
    public static final int LAYOUT_FRAME_TWO=2;
    public static final int LAYOUT_FRAME_THREE=3;
    public static final int LAYOUT_FRAME_FOUR=4;
    public static final int LAYOUT_FRAME_FIVE=5;
    public static final int LAYOUT_FRAME_SIX=6;
    public static final int LAYOUT_LOADING = 33;
    private int layoutType;
    private String emailId;
    private String contactNo;
    private String website;
    private String address;

    public FooterModel() {
    }

    public static int getLayoutLoading() {
        return LAYOUT_LOADING;
    }
    public static int getLayoutFrameOne() {
        return LAYOUT_FRAME_ONE;
    }
    public static int getLayoutFrameTwo() {
        return LAYOUT_FRAME_TWO;
    }
    public static int getLayoutFrameThree() {
        return LAYOUT_FRAME_THREE;
    }
    public static int getLayoutFrameFour() {
        return LAYOUT_FRAME_FOUR;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public static int getLayoutFrameFive() {
        return LAYOUT_FRAME_FIVE;
    }
    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
