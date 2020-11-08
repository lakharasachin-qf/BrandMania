package com.app.brandmania.Activity;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandmania.Fragment.top.ColorTab;
import com.app.brandmania.Fragment.top.FrameTab;
import com.app.brandmania.Fragment.top.TextTab;
import com.app.brandmania.Fragment.top.TextureTab;

class EditTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public EditTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ColorTab colorTab = new ColorTab();
                return colorTab;
            case 1:
                FrameTab frameTab = new FrameTab();
                return frameTab;
            case 2:
                TextureTab textureTab = new TextureTab();
                return textureTab;
                case 3:
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