package com.app.brandmania.Model;

public class DownloadFavoriteItemList
{
    public static final int LAYOUT_DOWNLOAD = 1;
    public static final int LAYOUT_DOWNLOADGRID = 2;
    public static final int LAYOUT_FAVOURIT = 3;
    public static final int LAYOUT_FAVOURITGRID= 4;
    private String Image;
    private String Name;
    private String Frame;
    private int layoutType;

    public DownloadFavoriteItemList() {
    }

    public static int getLayoutDownload() {
        return LAYOUT_DOWNLOAD;
    }
    public static int getLayoutDownloadgrid() {return LAYOUT_DOWNLOADGRID; }
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
}
