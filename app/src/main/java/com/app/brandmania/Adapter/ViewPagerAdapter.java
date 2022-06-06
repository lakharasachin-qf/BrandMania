package com.app.brandmania.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.app.brandmania.Activity.details.ImageCategoryDetailActivity;
import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final Activity context;
    private LayoutInflater layoutInflater;
    private final List<ViewPagerItem> sliderImg;
    private ImageLoader imageLoader;


    public ViewPagerAdapter(List sliderImg, Activity context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager, null);

        ViewPagerItem utils = sliderImg.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.pageView);


        if (!utils.getId().equalsIgnoreCase("-1")) {

            Glide.with(context)
                    .load(utils.getSliderImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
            CardView itemLayout = view.findViewById(R.id.itemLayout);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String catId = utils.getImageCategory();
                    String targetLink = utils.getTargetLink();
                    String isActivity = utils.getIsActivity();

                    if (catId == null || catId.equalsIgnoreCase("null"))
                        catId = "";

                    if (targetLink == null || targetLink.equalsIgnoreCase("null"))
                        targetLink = "";

                    if (isActivity == null || isActivity.equalsIgnoreCase("null"))
                        isActivity = "";


                    if (!catId.isEmpty()) {
                        Intent intent = new Intent(context, ImageCategoryDetailActivity.class);
                        intent.putExtra("notification", "1");
                        intent.putExtra("cat_id", catId);
                        intent.putExtra("catName", utils.getCategoryName());
                        context.startActivity(intent);
                    }

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (position == 0) {

                    } else if (position == 1) {

                    } else {

                    }

                }
            });
        } else {
            Glide.with(context)
                    .load(R.drawable.banner_one)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView);
        }
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