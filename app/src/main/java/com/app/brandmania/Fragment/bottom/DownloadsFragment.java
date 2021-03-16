package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentDownloadsBinding;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class DownloadsFragment extends Fragment {
    Activity act;
    private FragmentDownloadsBinding binding;

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloads, container, false);
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
        PreafManager preafManager=new PreafManager(act);
        Log.e("pref",preafManager.getActiveBrand().getId());
        return binding.getRoot();
    }
}