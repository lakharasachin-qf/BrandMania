package com.app.brandmania.LetterHead.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemLayoutViewallimageBinding;
import com.app.brandmania.databinding.ItemLayoutViewframeBinding;
import com.app.brandmania.utils.Utility;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FrameAdaptor extends RecyclerView.Adapter<FrameAdaptor.FrameAdaptorViewHolder> {
    private final ArrayList<FrameItem> frameItems;
    Activity activity;
    private BrandListItem brandListItem;

    public BrandListItem getBrandListItem() {
        return brandListItem;
    }

    public void setBrandListItem(BrandListItem brandListItem) {
        this.brandListItem = brandListItem;
    }

    public FrameAdaptor(ArrayList<FrameItem> frameItems, Activity activity) {
        this.frameItems = frameItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FrameAdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutViewframeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewframe, parent, false);
        return new FrameAdaptorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameAdaptorViewHolder holder, int position) {
        final FrameItem model = frameItems.get(position);
        Glide.with(activity)
                .load(model.getFrame1())
                .placeholder(R.drawable.placeholder)
                .into(holder.binding.frameImage);

//        holder.binding.itemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utility.fullScreenImageViewer(activity, model.getFrame1());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return frameItems.size();
    }

    public class FrameAdaptorViewHolder extends RecyclerView.ViewHolder {

        ItemLayoutViewframeBinding binding;

        public FrameAdaptorViewHolder(ItemLayoutViewframeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
