package com.app.brandadmin.Model;

import java.util.ArrayList;

public class DashBoardItem   implements Comparable<DashBoardItem> {

    private int filterIndex;

    public int getFilterIndex() {
        return filterIndex;
    }

    public DashBoardItem setFilterIndex(int filterIndex) {
        this.filterIndex = filterIndex;
        return this;
    }

    private ArrayList<DashBoardItem> dashBoardItems;

    public static final int DAILY_IMAGES=0;
    public static final int FESTIVAL_IMAGES=1;

    private int layout;

    public int getLayout() {
        return layout;
    }

    public DashBoardItem setLayout(int layout) {
        this.layout = layout;
        return this;
    }

    private String id;
    private String Cat_id;
    private String name;
    private String description;
    private String Tag;
    ArrayList<ImageList> imageLists;
    private Links links;
    private String x_conrdinate;
    private String y_cordinate;
    private boolean isImageFree=false;
    private String thumbnail;

    ArrayList<ImageList> dailyImages;

    public String getThumbnail() {
        return thumbnail;
    }

    public DashBoardItem setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public ArrayList<ImageList> getDailyImages() {
        return dailyImages;
    }

    public DashBoardItem setDailyImages(ArrayList<ImageList> dailyImages) {
        this.dailyImages = dailyImages;
        return this;
    }

    public boolean isImageFree() {
        return isImageFree;
    }

    public DashBoardItem setImageFree(boolean imageFree) {
        isImageFree = imageFree;
        return this;
    }



    public ArrayList<DashBoardItem> getDashBoardItems() {
        return dashBoardItems;
    }

    public void setDashBoardItems(ArrayList<DashBoardItem> dashBoardItems) {
        this.dashBoardItems = dashBoardItems;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;

    }

    public DashBoardItem() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public ArrayList<ImageList> getImageLists() {
        return imageLists;
    }

    public void setImageLists(ArrayList<ImageList> imageLists) {
        this.imageLists = imageLists;
    }

    public String getCat_id() {
        return Cat_id;
    }

    public void setCat_id(String cat_id) {
        Cat_id = cat_id;
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

    @Override
    public int compareTo(DashBoardItem dashBoardItem) {
        int compareage= ((DashBoardItem)dashBoardItem).getFilterIndex();
        //  For Ascending order
        return this.getFilterIndex() - compareage;
        // For Descending order do like this
        // return compareage-this.studentage;
    }
}
