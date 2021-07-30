package com.app.brandadmin.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.brandadmin.Fragment.top.CategoryFrameTab;
import com.app.brandadmin.Fragment.top.ColorTab;
import com.app.brandadmin.Fragment.top.EditTab;
import com.app.brandadmin.Fragment.top.FooterTab;
import com.app.brandadmin.Fragment.top.FrameTab;
import com.app.brandadmin.Fragment.top.ImageTab;
import com.app.brandadmin.Fragment.top.TextTab;

public class ViewAllTopCustomeFrameTabAdapter  extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    boolean isViewAll=false;
    public ViewAllTopCustomeFrameTabAdapter setViewAll(boolean viewAll) {
        isViewAll = viewAll;
        return this;
    }

    public ViewAllTopCustomeFrameTabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CategoryFrameTab categoryFrameTab = new CategoryFrameTab();
                if (isViewAll)
                    categoryFrameTab.setViewAll(isViewAll);
                return categoryFrameTab;
            case 1:
                FooterTab footerTab = new FooterTab();
                return footerTab;

            case 2:
                ImageTab imageTab = new ImageTab();

                return imageTab;

            case 3:
                FrameTab frameTab = new FrameTab();
                return frameTab;

            case 4:
                ColorTab colorTab = new ColorTab();
                return colorTab;


            case 5:
                TextTab textTab = new TextTab();
                textTab.setActivityType(2);
                return textTab;

            case 6:
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