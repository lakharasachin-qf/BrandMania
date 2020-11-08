package com.app.brandmania.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.brandmania.Activity.ViewAllImage;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemLayoutHomeBinding;
import com.app.brandmania.databinding.ItemLayoutViewallimageBinding;

import java.util.List;

import static com.app.brandmania.Model.ImageList.LAYOUT_IMAGE_CATEGORY;
import static com.app.brandmania.Model.ImageList.LAYOUT_IMAGE_CATEGORY_BY_ID;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;


public class ImageCategoryAddaptor extends RecyclerView.Adapter {
    public static final int FROM_HOMEFRAGEMENT=1;
    public static final int FROM_VIEWALL=2;
    private List<ImageList> imageLists;
    Activity activity;
    private boolean isLoadingAdded = false;
    private DashBoardItem dashBoardItem;
    public DashBoardItem getDashBoardItem() {
        return dashBoardItem;
    }
    public void setDashBoardItem(DashBoardItem dashBoardItem) {
        this.dashBoardItem = dashBoardItem;
    }
    public ImageCategoryAddaptor(List<ImageList> imageLists, Activity activity) {
        this.imageLists = imageLists;
        this.activity = activity;
    }
    int layoutType;
    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case LAYOUT_IMAGE_CATEGORY:
                ItemLayoutHomeBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_home, viewGroup, false);
                return new ImageCategoryHolder(layoutBinding);
            case LAYOUT_IMAGE_CATEGORY_BY_ID:
                ItemLayoutViewallimageBinding viewallimageBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewallimage, viewGroup, false);
                return new ImageCategoryByIdHolder(viewallimageBinding);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == imageLists.size() - 1 && isLoadingAdded)
            return LAYOUT_LOADING;
        switch (imageLists.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_IMAGE_CATEGORY;
            case 2:
                return LAYOUT_IMAGE_CATEGORY_BY_ID;
            default:
                return -1;
        }
    }
    @Override
    public int getItemCount() {
        return imageLists.size();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ImageList model = imageLists.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_IMAGE_CATEGORY:
                    Glide.with(activity)
                            .load(model.getLogo())
                            .placeholder(R.drawable.placeholder)
                            .into(((ImageCategoryHolder) holder).binding.image);
                    ((ImageCategoryHolder)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // if (layoutType==FROM_HOMEFRAGEMENT) {
                                Intent intent=new Intent(activity,ViewAllImage.class);
                                Gson  gson=new Gson();
                            intent.putExtra("detailsObj", gson.toJson(dashBoardItem));
                                intent.putExtra("selectedimage",gson.toJson(model));
                                intent.putExtra("position",position);
                                activity.startActivity(intent);
                        }
                    });
                    break;

                case LAYOUT_IMAGE_CATEGORY_BY_ID:

                    Glide.with(activity)
                            .load(model.getFrame())
                            .placeholder(R.drawable.placeholder)
                            .into(((ImageCategoryByIdHolder) holder).binding.image);
                    ((ImageCategoryByIdHolder)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (layoutType==FROM_HOMEFRAGEMENT) {
                                Intent intent=new Intent(activity,ViewAllImage.class);
                                Gson  gson=new Gson();
                                intent.putExtra("selectedimage",gson.toJson(model));
                                intent.putExtra("position",position);
                                activity.startActivity(intent);

                            }
                            if (layoutType==FROM_VIEWALL)
                            {
                                ((ImageCateItemeInterFace) activity).ImageCateonItemSelection( position, model);
                            }
                        }
                    });

                    break;
            }

        }


    }

    static class ImageCategoryHolder extends RecyclerView.ViewHolder {
        ItemLayoutHomeBinding binding;

        ImageCategoryHolder(ItemLayoutHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class ImageCategoryByIdHolder extends RecyclerView.ViewHolder {
        ItemLayoutViewallimageBinding binding;

        ImageCategoryByIdHolder(ItemLayoutViewallimageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
}

