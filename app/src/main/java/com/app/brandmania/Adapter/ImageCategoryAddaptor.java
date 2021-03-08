package com.app.brandmania.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.ViewAllFrameImageActivity;
import com.app.brandmania.Activity.ViewAllImage;
import com.app.brandmania.Activity.ViewOnlyCustomeFrame;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Interface.IBackendFrameSelect;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemLayoutFrameBinding;
import com.app.brandmania.databinding.ItemLayoutHomeBinding;
import com.app.brandmania.databinding.ItemLayoutViewallframeBinding;
import com.app.brandmania.databinding.ItemLayoutViewallimageBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import static com.app.brandmania.Model.ImageList.LAYOUT_FRAME;
import static com.app.brandmania.Model.ImageList.LAYOUT_FRAME_CATEGORY;
import static com.app.brandmania.Model.ImageList.LAYOUT_FRAME_CATEGORY_BY_ID;
import static com.app.brandmania.Model.ImageList.LAYOUT_IMAGE_CATEGORY;
import static com.app.brandmania.Model.ImageList.LAYOUT_IMAGE_CATEGORY_BY_ID;


public class ImageCategoryAddaptor extends RecyclerView.Adapter {
    public static final int FROM_HOMEFRAGEMENT=1;
    public static final int FROM_VIEWALL=2;

    public static final int FROM_CATEGORYFRAGEMENT=1;
    public static final int FROM_VIEWALLFRAME=2;
    private List<ImageList> imageLists;
    Activity activity;
    PreafManager preafManager;
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
        preafManager=new PreafManager(activity);
        // Log.e("menuModels",new Gson().toJson(imageLists));
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
            case LAYOUT_FRAME:
                ItemLayoutViewallimageBinding viewallimageBinding1 = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewallimage, viewGroup, false);
                return new FrameHolder(viewallimageBinding1);
            case LAYOUT_FRAME_CATEGORY:
                ItemLayoutFrameBinding lframeayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_frame, viewGroup, false);
                return new FrameCategoryHolder(lframeayoutBinding);
            case LAYOUT_FRAME_CATEGORY_BY_ID:
                ItemLayoutViewallframeBinding viewallframeBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_layout_viewallframe, viewGroup, false);
                return new FrameCategoryByIdHolder(viewallframeBinding);
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {

        switch (imageLists.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_IMAGE_CATEGORY;
            case 2:
                return LAYOUT_IMAGE_CATEGORY_BY_ID;
            case 3:
                return LAYOUT_FRAME;
            case 4:
                return LAYOUT_FRAME_CATEGORY;
            case 5:
                return LAYOUT_FRAME_CATEGORY_BY_ID;
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
        ImageList model = imageLists.get(position);
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



                    if (!model.isImageFree()){
                        ((ImageCategoryHolder)holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        ((ImageCategoryHolder)holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }

                     if (preafManager.getActiveBrand()!=null && preafManager.getActiveBrand().getIs_payment_pending().equals("0")) {
                            ((ImageCategoryHolder) holder).binding.elementPremium.setVisibility(View.GONE);
                            ((ImageCategoryHolder) holder).binding.freePremium.setVisibility(View.GONE);

                    }
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
                            if (layoutType==FROM_VIEWALL) {

                                ((ImageCateItemeInterFace) activity).ImageCateonItemSelection(position, model);
                            }
                        }
                    });
                    if (!model.isImageFree()){
                        ((ImageCategoryByIdHolder)holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        ((ImageCategoryByIdHolder)holder).binding.freePremium.setVisibility(View.VISIBLE);
                    }
                    if (preafManager.getActiveBrand() !=null) {
                        if (preafManager.getActiveBrand().getIs_payment_pending().equals("0")) {
                            ((ImageCategoryByIdHolder) holder).binding.elementPremium.setVisibility(View.GONE);
                            ((ImageCategoryByIdHolder) holder).binding.freePremium.setVisibility(View.GONE);
                        }
                    }

                    break;
                case LAYOUT_FRAME:

                    Glide.with(activity)
                            .load(model.getFrame1())
                            .placeholder(R.drawable.placeholder)
                            .into((((FrameHolder) holder).binding.image));

                    ((FrameHolder) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((IBackendFrameSelect) activity).onBackendFrameChoose(model, position);
                        }
                    });
                    ((FrameHolder) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((IBackendFrameSelect) activity).onBackendFrameChoose(model, position);
                        }
                    });

                    break;

                case LAYOUT_FRAME_CATEGORY:
                    Glide.with(activity)
                            .load(model.getLogo())
                            .placeholder(R.drawable.placeholder)
                            .into(((FrameCategoryHolder) holder).binding.image);
                    ((FrameCategoryHolder)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(activity, ViewAllFrameImageActivity.class);
                            Gson  gson=new Gson();
                            intent.putExtra("detailsObj", gson.toJson(dashBoardItem));
                            intent.putExtra("selectedimage",gson.toJson(model));
                            intent.putExtra("position",position);
                            activity.startActivity(intent);
                        }
                    });


                    if (!model.isImageFree()){
                        ((FrameCategoryHolder)holder).binding.elementPremium.setVisibility(View.VISIBLE);
                    }
                    if (preafManager.getActiveBrand() !=null) {
                        if (preafManager.getActiveBrand().getIs_payment_pending().equals("0")) {
                            ((FrameCategoryHolder) holder).binding.elementPremium.setVisibility(View.GONE);
                            // ((FrameCategoryHolder)holder).binding.freePremium.setVisibility(View.GONE);
                        }
                    }
                    break;
                case LAYOUT_FRAME_CATEGORY_BY_ID:

                    Glide.with(activity)
                            .load(model.getFrame())
                            .placeholder(R.drawable.placeholder)
                            .into(((FrameCategoryByIdHolder) holder).binding.image);






                    ((FrameCategoryByIdHolder)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (layoutType==FROM_CATEGORYFRAGEMENT) {
                              //  Toast.makeText(activity,"bjhdshdj",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(activity,ViewAllFrameImageActivity.class);
                                Gson  gson=new Gson();
                                intent.putExtra("selectedimage",gson.toJson(model));
                                intent.putExtra("position",position);
                                activity.startActivity(intent);

                            }


                            if (layoutType==FROM_VIEWALLFRAME) {
                             //   Toast.makeText(activity,"birthday",Toast.LENGTH_LONG).show();
                                ((ImageCateItemeInterFace) activity).ImageCateonItemSelection(position, model);
                            }
                        }
                    });

                    break;
            }

        } else {
           // Toast.makeText(activity, "dfgdgdfgfdg", Toast.LENGTH_SHORT).show();
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
    static class FrameHolder extends RecyclerView.ViewHolder {
        ItemLayoutViewallimageBinding binding;

        FrameHolder(ItemLayoutViewallimageBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FrameCategoryHolder extends RecyclerView.ViewHolder {
        ItemLayoutFrameBinding binding;

        FrameCategoryHolder(ItemLayoutFrameBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
    static class FrameCategoryByIdHolder extends RecyclerView.ViewHolder {
        ItemLayoutViewallframeBinding binding;

        FrameCategoryByIdHolder(ItemLayoutViewallframeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }




}

