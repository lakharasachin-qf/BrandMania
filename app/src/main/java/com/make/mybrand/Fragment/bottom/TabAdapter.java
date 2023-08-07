package com.make.mybrand.Fragment.bottom;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.make.mybrand.Fragment.top.DownloadListTab;
import com.make.mybrand.Fragment.top.FavoritListTab;

class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public TabAdapter (Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DownloadListTab dailyFragment = new DownloadListTab();
                return dailyFragment;
            case 1:
                FavoritListTab festivalFragment = new FavoritListTab();
                return festivalFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}