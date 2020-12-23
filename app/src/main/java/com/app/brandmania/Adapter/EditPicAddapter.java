package com.app.brandmania.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandmania.Fragment.top.FrameTab;
import com.app.brandmania.Fragment.top.ImageTab;
import com.app.brandmania.Fragment.top.TextTab;

public class EditPicAddapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;

    public EditPicAddapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FrameTab frameTab = new FrameTab();
                return frameTab;
            case 1:
                ImageTab imageTab = new ImageTab();
                return imageTab;
            case 2:
                TextTab textTab = new TextTab();
                return textTab;


            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }

}
