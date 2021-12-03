package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Activity.basics.RegistrationActivity;
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentDownloadsBinding;
import com.app.brandmania.views.MyBounceInterpolator;
import com.google.android.material.tabs.TabLayout;

public class DownloadsFragment extends BaseFragment {
    Activity act;
    private FragmentDownloadsBinding binding;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloads, parent, false);
        if (prefManager.getActiveBrand() != null) {

            binding.tabLayout.setVisibility(View.VISIBLE);
            binding.view.setVisibility(View.VISIBLE);
            binding.viewPager.setVisibility(View.VISIBLE);

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
        } else {
            binding.tabLayout.setVisibility(View.GONE);
            binding.view.setVisibility(View.GONE);
            binding.viewPager.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);
            animateButton();
            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });


        }

        return binding.getRoot();
    }

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce_two);
        double animationDuration = 4 * 1000;
        myAnim.setRepeatCount(Animation.INFINITE);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.continueBtn.startAnimation(myAnim);


        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }
}