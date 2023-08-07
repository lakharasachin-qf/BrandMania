package com.make.mybrand.Model;

public class DownloadFavoriteItemList {
    public static final int LAYOUT_DOWNLOAD = 1;
    public static final int LAYOUT_DOWNLOADGRID = 2;
    public static final int LAYOUT_FAVOURIT = 3;
    public static final int LAYOUT_FAVOURITGRID = 4;
    private int Id;
    private String Image;
    private String Name;
    private String Frame;
    private int Flag;
    private int layoutType;
    private boolean isCustom = false;
    private boolean isImageFree = false;
    private String footerLayout;

    public boolean isImageFree() {
        return isImageFree;
    }

    public DownloadFavoriteItemList setImageFree(boolean imageFree) {
        isImageFree = imageFree;
        return this;
    }

    public String getFooterLayout() {
        return footerLayout;
    }

    public DownloadFavoriteItemList setFooterLayout(String footerLayout) {
        this.footerLayout = footerLayout;
        return this;
    }

    public DownloadFavoriteItemList() {
    }

    public boolean isCustom() {
        return isCustom;
    }

    public DownloadFavoriteItemList setCustom(boolean custom) {
        isCustom = custom;
        return this;
    }

    public static int getLayoutDownload() {
        return LAYOUT_DOWNLOAD;
    }

    public static int getLayoutDownloadgrid() {
        return LAYOUT_DOWNLOADGRID;
    }

    public static int getLayoutFavourit() {
        return LAYOUT_FAVOURIT;
    }

    public static int getLayoutFavouritgrid() {
        return LAYOUT_FAVOURITGRID;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

//    public String getImage() {
//        return Image;
//    }
//    public void setImage(String image) {
//        Image = image;
//    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFrame() {
        return Frame;
    }

    public void setFrame(String frame) {
        Frame = frame;
    }

    public int getId(int anInt) {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }
}