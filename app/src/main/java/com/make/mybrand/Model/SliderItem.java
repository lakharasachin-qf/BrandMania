package com.make.mybrand.Model;

import java.util.ArrayList;

public class SliderItem {

   private String priceForPay;
   private String packageTitle;
   private String templateTitle;
   private String imageTitle;
   private String payTitle;
   private String packageid;
   private String brandId;
   private String subscriptionDate;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

   private String duration;

    public String getDuration() {
        return duration;
    }

    public SliderItem setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    ArrayList<SlideSubItem> slideSubItems;

    public SliderItem() {
    }

    public String getSubscriptionDate() {
        return subscriptionDate;
    }

    public SliderItem setSubscriptionDate(String subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
        return this;
    }

    public String getPriceForPay() {
        return priceForPay;
    }

    public void setPriceForPay(String priceForPay) {
        this.priceForPay = priceForPay;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }

    public ArrayList<SlideSubItem> getSlideSubItems() {
        return slideSubItems;
    }

    public void setSlideSubItems(ArrayList<SlideSubItem> slideSubItems) {
        this.slideSubItems = slideSubItems;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }
}
