package com.make.mybrand.LetterHead.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.FrameItem;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemLayoutViewallimageBinding;
import com.make.mybrand.databinding.ItemLayoutViewframeBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FrameAdaptor extends RecyclerView.Adapter<FrameAdaptor.FrameAdaptorViewHolder> {
    private final ArrayList<FrameItem> frameItems;
    Activity activity;
    private BrandListItem brandListItem;
    public FrameAdaptor.onFooterListener footerListener;

    public BrandListItem getBrandListItem() {
        return brandListItem;
    }

    public void setBrandListItem(BrandListItem brandListItem) {
        this.brandListItem = brandListItem;
    }

    public FrameAdaptor setFooterListener(FrameAdaptor.onFooterListener footerListener) {
        this.footerListener = footerListener;
        return this;
    }

    public interface onFooterListener {
        void onFooterChoose(String Frames, Integer position);
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
    public void onBindViewHolder(@NonNull FrameAdaptorViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final FrameItem model = frameItems.get(position);

        if (position == 0) {
            Glide.with(activity)
                    .load(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.frameImage);
            holder.binding.itemLayout.setOnClickListener(v -> footerListener.onFooterChoose(model.getFrame1(), position));
        } else {
            Glide.with(activity)
                    .load(model.getFrame1())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.frameImage);
            holder.binding.itemLayout.setOnClickListener(v -> footerListener.onFooterChoose(model.getFrame1(), position));
        }

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
