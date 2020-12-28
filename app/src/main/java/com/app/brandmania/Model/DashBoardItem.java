package com.app.brandmania.Model;

import java.util.ArrayList;

public class DashBoardItem {

    private ArrayList<DashBoardItem> dashBoardItems;


    private String id;
    private String Cat_id;
    private String name;
    private String description;
    private String Tag;
    ArrayList<ImageList> imageLists;
    private Links links;

    private boolean isImageFree=false;

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
}
