package com.app.brandadmin.Model;

import android.graphics.drawable.Drawable;

public class ViewPagerItem implements Cloneable {
    private String id;
    private String sliderImageUrl;
    private Drawable Frame;
    private String bannerstatus;
    private String redirection;
    private String position;

    public Object clone() throws
            CloneNotSupportedException {
        return super.clone();
    }

    public ViewPagerItem() {
    }

    public ViewPagerItem (ViewPagerItem c){
        this.id=c.id;
        this.sliderImageUrl=c.sliderImageUrl;
        this.Frame=c.Frame;
        this.bannerstatus=c.bannerstatus;
        this.redirection=c.redirection;
        this.position=c.position;


    }

    public String getSliderImageUrl() {
        return sliderImageUrl;
    }

    public void setSliderImageUrl(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBannerstatus() {
        return bannerstatus;
    }

    public void setBannerstatus(String bannerstatus) {
        this.bannerstatus = bannerstatus;
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Drawable getFrame() {
        return Frame;
    }

    public void setFrame(Drawable frame) {
        Frame = frame;
    }
}