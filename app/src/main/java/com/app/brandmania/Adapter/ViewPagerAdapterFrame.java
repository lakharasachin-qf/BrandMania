package com.app.brandmania.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;

import java.util.List;

public class ViewPagerAdapterFrame extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<FrameItem> sliderImg;
    private ImageLoader imageLoader;
    BrandListItem brandListItem;

    public BrandListItem getBrandListItem() {
        return brandListItem;
    }

    public void setBrandListItem(BrandListItem brandListItem) {
        this.brandListItem = brandListItem;
    }

    public ViewPagerAdapterFrame(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);

        FrameItem utils = sliderImg.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(context)
                .load(utils.getFrame1())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){

                } else if(position == 1){

                } else {

                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}