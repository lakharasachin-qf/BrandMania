package com.app.brandmania.Model;

import java.util.ArrayList;

public class BackgroundColorsModel {


    int color;
    int objectPosition;


    public int getObjectPosition() {
        return objectPosition;
    }

    public void setObjectPosition(int objectPosition) {
        this.objectPosition = objectPosition;
    }

    boolean isSelected = false;
    ArrayList<Integer> viewId;
    public BackgroundColorsModel(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public  void setColor(int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public ArrayList<Integer> getViewId() {
        return viewId;
    }

    public void setViewId(ArrayList<Integer> viewId) {
        this.viewId = viewId;
    }
}
