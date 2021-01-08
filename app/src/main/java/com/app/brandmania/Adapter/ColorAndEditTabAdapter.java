package com.app.brandmania.Adapter;


import android.content.Context;
import android.media.Image;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandmania.Fragment.top.ColorTab;
import com.app.brandmania.Fragment.top.CustomImageTab;
import com.app.brandmania.Fragment.top.EditTab;
import com.app.brandmania.Fragment.top.FooterTab;
import com.app.brandmania.Fragment.top.FrameTab;
import com.app.brandmania.Fragment.top.ImageTab;
import com.app.brandmania.Fragment.top.TextTab;
import com.app.brandmania.Fragment.top.TextureTab;

public class ColorAndEditTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    int tabLayou=0;

    public void setTabLayou(int tabLayou) {
        this.tabLayou = tabLayou;
    }

    public ColorAndEditTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                ImageTab imageTab = new ImageTab();
                return imageTab;
            case 1:
                FooterTab footerTab=new FooterTab();
                return footerTab;
            case 2:
                FrameTab frameTab = new FrameTab();
                return frameTab;
            case 3:
                ColorTab colorTab = new ColorTab();
                colorTab.setTabLayou(tabLayou);
                return colorTab;
            case 4:
                TextTab textTab = new TextTab();
                return textTab;
            case 5:
                EditTab editTab = new EditTab();
                return editTab;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}