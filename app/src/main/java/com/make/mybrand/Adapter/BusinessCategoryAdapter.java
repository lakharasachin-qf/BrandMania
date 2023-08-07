package com.make.mybrand.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Activity.details.ImageCategoryDetailActivity;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Model.DashBoardItem;
import com.make.mybrand.Model.ImageList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ItemLayoutDailyImagesBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.ViewHolder> {
    private List<ImageList> imagesList;
    private Activity act;
    private PreafManager preafManager;
    private DashBoardItem dashBoardItem;

    public BusinessCategoryAdapter(DashBoardItem dashBoardItem, Activity context, List<ImageList> imageLists) {
        this.imagesList = imageLists;
        this.act = context;
        this.preafManager = new PreafManager(act);
        this.dashBoardItem = dashBoardItem;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutDailyImagesBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.item_layout_daily_images, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageList model = imagesList.get(position);
        Glide.with(act)
                .load(model.getFrame())
                .placeholder(R.drawable.placeholder)
                .into((holder).binding.image);
        (holder).binding.title.setText(model.getName());
        (holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, ImageCategoryDetailActivity.class);
                Gson gson = new Gson();
                intent.putExtra("dailyImages", "1");
                intent.putExtra("detailsObj", gson.toJson(dashBoardItem));
                intent.putExtra("selectedimage", gson.toJson(model));
                intent.putExtra("position", position);
                act.startActivity(intent);
            }
        });

        if (!model.isImageFree()) {
            (holder).binding.elementPremium.setVisibility(View.VISIBLE);
        } else {
            (holder).binding.freePremium.setVisibility(View.VISIBLE);
        }
        if (preafManager.getActiveBrand() != null) {
            if (preafManager.getActiveBrand().getIs_payment_pending().equals("0")) {
                (holder).binding.elementPremium.setVisibility(View.GONE);
                (holder).binding.freePremium.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemLayoutDailyImagesBinding binding;

        public ViewHolder(ItemLayoutDailyImagesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<ImageList> list) {
        imagesList = list;
        notifyDataSetChanged();
    }

}
