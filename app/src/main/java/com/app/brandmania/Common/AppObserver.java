package com.app.brandmania.Common;

import android.content.Context;


import com.app.brandmania.Model.BrandListItem;

import java.util.Observable;

public class AppObserver extends Observable {
    private final Context context;
    public String emailId;
    private String userName;
    String DefaultName;
    private int nStatusType;

    public AppObserver(Context context) {
        this.context = context;
    }

    public int getValue() {
        return nStatusType;
    }

    public void setValue(int nStatusTyp) {
        this.nStatusType = nStatusTyp;
        setChanged();
        notifyObservers(userName);
    }


}
