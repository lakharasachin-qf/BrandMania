package com.app.brandmania.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.R;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<ViewPagerItem> sliderImg;
    private OnColorPickerClickListener onColorPickerClickListener;

    public BannerAdapter(Context context,List<ViewPagerItem> colorPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sliderImg = colorPickerColors;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_banner_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  holder.colorPickerView.setBackgroundColor(sliderImg.get(position));

    }

    @Override
    public int getItemCount() {
        return sliderImg.size();
    }


    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            itemLayout=itemView.findViewById(R.id.itemLayout);

        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }


}
