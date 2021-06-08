package com.app.brandmania.newModule.newFragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    public static int pos = 0;

    private List<Fragment> myFragments = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    public void addFragment(Fragment f) {
        myFragments.add(f);
    }

    @Override
    public int getItemPosition(Object object) {
        //FragmentLeft is the class for the first fragment in the view
        //recreate only FragmentLeft

        return 1;
    }
}