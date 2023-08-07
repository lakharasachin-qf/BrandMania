package com.make.mybrand.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.FrameItem;
import com.make.mybrand.R;
import com.make.mybrand.utils.Utility;

import java.util.ArrayList;

public class FrameAddaptor extends RecyclerView.Adapter<FrameAddaptor.FrameAddaptorViewHolder> {
    private final ArrayList<FrameItem> frameItems;
    Activity activity;
    private BrandListItem brandListItem;

    public BrandListItem getBrandListItem() {
        return brandListItem;
    }

    public void setBrandListItem(BrandListItem brandListItem) {
        this.brandListItem = brandListItem;
    }

    public FrameAddaptor(ArrayList<FrameItem> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FrameAddaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.frame_item_view, parent, false);
        return new FrameAddaptor.FrameAddaptorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameAddaptorViewHolder holder, int position) {
        final FrameItem model = frameItems.get(position);
        Glide.with(activity)
                .load(model.getFrame1())
                .placeholder(R.drawable.placeholder)
                .into( holder.imageView);

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.fullScreenImageViewer(activity, model.getFrame1());
            }
        });

    }
    @Override
    public int getItemCount() {
        return frameItems.size();
    }
    public class FrameAddaptorViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CardView itemLayout;
        public FrameAddaptorViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.frame);
            itemLayout=itemView.findViewById(R.id.itemLayout);
        }
    }


}
