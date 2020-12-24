package com.app.brandmania.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandmania.Fragment.top.CategoryTab;
import com.app.brandmania.Fragment.top.ColorTab;
import com.app.brandmania.Fragment.top.FooterTab;
import com.app.brandmania.Fragment.top.FrameTab;
import com.app.brandmania.Fragment.top.TextTab;
import com.app.brandmania.Fragment.top.TextureTab;

public class ViewAllTopTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public ViewAllTopTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CategoryTab categoryTab = new CategoryTab();
                return categoryTab;
            case 1:
                ColorTab colorTab = new ColorTab();
                return colorTab;
            case 2:
                TextTab textTab = new TextTab();
                return textTab;
            case 3:
                FooterTab footerTab = new FooterTab();
                return footerTab;


            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}