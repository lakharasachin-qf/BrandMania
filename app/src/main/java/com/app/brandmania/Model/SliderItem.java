package com.app.brandmania.Model;

public class SliderItem {
   private String priceForPay;
   private String packageTitle;
   private String templateTitle;
   private String imageTitle;
   private String payTitle;


    public SliderItem(String priceForPay, String packageTitle, String templateTitle, String imageTitle, String payTitle) {
        this.priceForPay = priceForPay;
        this.packageTitle = packageTitle;
        this.templateTitle = templateTitle;
        this.imageTitle = imageTitle;
        this.payTitle = payTitle;
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
}
