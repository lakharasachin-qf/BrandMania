package com.make.mybrand.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.make.mybrand.Fragment.top.CategoryTab;
import com.make.mybrand.Fragment.top.ColorTab;
import com.make.mybrand.Fragment.top.FooterTab;
import com.make.mybrand.Fragment.top.FrameTab;
import com.make.mybrand.Fragment.top.TextTab;


public class ViewAllTopTabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    boolean isViewAll = false;

    public ViewAllTopTabAdapter setViewAll(boolean viewAll) {
        isViewAll = viewAll;
        return this;
    }

    public ViewAllTopTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }


    @NonNull
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