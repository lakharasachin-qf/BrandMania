package com.app.brandmania.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.brandmania.Adapter.SliderAdapter;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPackageBinding;

import java.util.ArrayList;
import java.util.List;

public class PackageActivity extends AppCompatActivity {
    private Activity act;
    private ActivityPackageBinding  binding;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_package);
        List<SliderItem>sliderItems=new ArrayList<>();

        sliderItems.add(new SliderItem("\u20B9 299","Basic","1 - Template / Brand","50 Image Download / Year","299 / Year"));
        sliderItems.add(new SliderItem("\u20B9 399","Standard","3 - Templates / Brand","250 Image Download / Year","499 / Year"));
        sliderItems.add(new SliderItem("\u20B9 999","Enterprise","6 - Templates / Brand","999 Image Download / Year", "999 / Year"));

        binding.viewPagerImageSlider.setAdapter(new SliderAdapter(sliderItems,act));
        binding.viewPagerImageSlider.setClipToPadding(false);
        binding.viewPagerImageSlider.setClipChildren(false);
        binding.viewPagerImageSlider.setOffscreenPageLimit(2);
        binding.viewPagerImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer);
    }


}