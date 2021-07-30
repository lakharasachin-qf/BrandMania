package com.app.brandadmin.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandadmin.Fragment.top.CategoryTab;
import com.app.brandadmin.Fragment.top.ColorTab;
import com.app.brandadmin.Fragment.top.FooterTab;
import com.app.brandadmin.Fragment.top.FrameTab;
import com.app.brandadmin.Fragment.top.TextTab;


public class ViewAllTopTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    boolean isViewAll=false;

    public ViewAllTopTabAdapter setViewAll(boolean viewAll) {
        isViewAll = viewAll;
        return this;
    }

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
                if (isViewAll)
                    categoryTab.setViewAll(isViewAll);

                return categoryTab;
            case 1:
                FooterTab footerTab = new FooterTab();
                return footerTab;


            case 2:
                FrameTab frameTab = new FrameTab();
                return frameTab;

            case 3:
                ColorTab colorTab = new ColorTab();
                return colorTab;


            case 4:
                TextTab textTab = new TextTab();
                textTab.setActivityType(1);
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