package com.make.mybrand.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class InnerFragmentAdpaters extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public InnerFragmentAdpaters(FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments= fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return fragments.size();
    }
}
