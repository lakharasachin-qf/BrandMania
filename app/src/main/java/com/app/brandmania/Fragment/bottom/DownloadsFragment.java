package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentDownloadsBinding;
import com.google.android.material.tabs.TabLayout;

public class DownloadsFragment extends BaseFragment {
    Activity act;
    private FragmentDownloadsBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloads, parent, false);
        if (prefManager.getActiveBrand() != null) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("DOWNLOAD"));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FAVOURITE"));
            binding.tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ad2753"));

            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            final TabAdapter adapter = new TabAdapter(act, getChildFragmentManager(),
                    binding.tabLayout.getTabCount());
            binding.viewPager.setAdapter(adapter);
            binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    binding.viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
        return binding.getRoot();
    }
}