package com.app.brandmania.Model;

import java.util.ArrayList;

public class BrandListItem {

    public static final int LAYOUT_BRANDLIST=1;
    public static final int LAYOUT_NOTIFICATIONlIST=2;
    public static final int LAYOUT_BRANDLISTBYID=3;
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
    private String message;
    private String date;
    private String time;
    private String is_frame;
    private String frame_message;
    private String frambaseyrl;
    private String is_payment_pending;
    private String payment_message;
    private String packagename;
    private String packagemessage;
    private String no_of_total_image;
    private String no_of_used_image;
    private String no_of_frame;
    private String no_of_remaining;
    private String expiery_date;
    private String package_id;
    private String rate;
    private String duration;
    private String subscriptionDate;

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public BrandListItem setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
        return this;
    }

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

    public static int getLayoutNotificationlist() {
        return LAYOUT_NOTIFICATIONlIST;
    }

    public static int getLayoutBrandlistbyid() {
        return LAYOUT_BRANDLISTBYID;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIs_frame() {
        return is_frame;
    }

    public void setIs_frame(String is_frame) {
        this.is_frame = is_frame;
    }

    public String getFrame_message() {
        return frame_message;
    }

    public void setFrame_message(String frame_message) {
        this.frame_message = frame_message;
    }

    public String getFrambaseyrl() {
        return frambaseyrl;
    }

    public void setFrambaseyrl(String frambaseyrl) {
        this.frambaseyrl = frambaseyrl;
    }

    public String getIs_payment_pending() {
        return is_payment_pending;
    }

    public void setIs_payment_pending(String is_payment_pending) {
        this.is_payment_pending = is_payment_pending;
    }

    public String getPayment_message() {
        return payment_message;
    }

    public void setPayment_message(String payment_message) {
        this.payment_message = payment_message;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPackagemessage() {
        return packagemessage;
    }

    public void setPackagemessage(String packagemessage) {
        this.packagemessage = packagemessage;
    }

    public String getNo_of_total_image() {
        return no_of_total_image;
    }

    public void setNo_of_total_image(String no_of_total_image) {
        this.no_of_total_image = no_of_total_image;
    }

    public String getNo_of_used_image() {
        return no_of_used_image;
    }

    public void setNo_of_used_image(String no_of_used_image) {
        this.no_of_used_image = no_of_used_image;
    }

    public String getNo_of_frame() {
        return no_of_frame;
    }

    public void setNo_of_frame(String no_of_frame) {
        this.no_of_frame = no_of_frame;
    }

    public String getNo_of_remaining() {
        return no_of_remaining;
    }

    public void setNo_of_remaining(String no_of_remaining) {
        this.no_of_remaining = no_of_remaining;
    }

    public String getExpiery_date() {
        return expiery_date;
    }

    public void setExpiery_date(String expiery_date) {
        this.expiery_date = expiery_date;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
