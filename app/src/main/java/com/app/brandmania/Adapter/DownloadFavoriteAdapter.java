package com.app.brandmania.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.brandmania.Activity.VIewAllDownloadImage;
import com.app.brandmania.Activity.ViewAllFavouritActivity;
import com.app.brandmania.Model.DownloadFavoriteItemList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DownloadlisItemListBinding;
import com.app.brandmania.databinding.ItemDownloadGridBinding;
import com.app.brandmania.databinding.ItemLayoutGetbrandlistBinding;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.app.brandmania.Model.DownloadFavoriteItemList.LAYOUT_DOWNLOAD;
import static com.app.brandmania.Model.DownloadFavoriteItemList.LAYOUT_DOWNLOADGRID;
import static com.app.brandmania.Model.DownloadFavoriteItemList.LAYOUT_FAVOURIT;
import static com.app.brandmania.Model.DownloadFavoriteItemList.LAYOUT_FAVOURITGRID;
import static com.app.brandmania.Model.ImageList.LAYOUT_LOADING;

public class DownloadFavoriteAdapter extends RecyclerView.Adapter {
    public static final int FROM_DOWNLOD = 1;
    public static final int FROM_VIEWDOWNLOD = 2;
    private boolean isLoadingAdded = false;
    private final List<DownloadFavoriteItemList> downloadFavoriteItemLists;
    Context context;
    Gson gson;

    public DownloadFavoriteAdapter(List<DownloadFavoriteItemList> downloadFavoriteItemLists, Context context) {
        this.downloadFavoriteItemLists = downloadFavoriteItemLists;
        this.context = context;
        gson = new Gson();
    }

    private onShareImageClick onShareImageClick;

    public DownloadFavoriteAdapter.onShareImageClick getOnShareImageClick() {
        return onShareImageClick;
    }

    public void setOnShareImageClick(DownloadFavoriteAdapter.onShareImageClick onShareImageClick) {
        this.onShareImageClick = onShareImageClick;
    }

    public  interface  onShareImageClick{
        public void onShareClick(DownloadFavoriteItemList favoriteItemList,int position);
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

            case LAYOUT_DOWNLOAD:
                DownloadlisItemListBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.downloadlis_item_list, viewGroup, false);

                return new DownloadFavoriteAdapter.DownloadHolder(layoutBinding);
            case LAYOUT_DOWNLOADGRID:
                ItemDownloadGridBinding layoutBinding1 = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_download_grid, viewGroup, false);
                return new DownloadFavoriteAdapter.DownloadHolderGrid(layoutBinding1);
            case LAYOUT_FAVOURIT:
                DownloadlisItemListBinding layoutBindingfavourit = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.downloadlis_item_list, viewGroup, false);
                return new DownloadFavoriteAdapter.DownloadHolder(layoutBindingfavourit);
            case LAYOUT_FAVOURITGRID:
                ItemDownloadGridBinding layoutBindingfavouritgrid = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_download_grid, viewGroup, false);
                return new DownloadFavoriteAdapter.DownloadHolderGrid(layoutBindingfavouritgrid);

        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == downloadFavoriteItemLists.size() - 1 && isLoadingAdded)
            return LAYOUT_LOADING;
        switch (downloadFavoriteItemLists.get(position).getLayoutType()) {
            case 1:
                return LAYOUT_DOWNLOAD;
            case 2:
                return LAYOUT_DOWNLOADGRID;
            case 3:
                return LAYOUT_FAVOURIT;
            case 4:
                return LAYOUT_FAVOURITGRID;

            default:
                return -1;
        }

    }

    @Override
    public int getItemCount() {
        return downloadFavoriteItemLists.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DownloadFavoriteItemList model = downloadFavoriteItemLists.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_DOWNLOAD:
                ((DownloadHolder) holder).binding.downloadListName.setText(downloadFavoriteItemLists.get(position).getName());
                Glide.with(context)
                        .load(downloadFavoriteItemLists.get(position).getImage())
                        .placeholder(R.drawable.placeholder)
                        .into(((DownloadHolder) holder).binding.downloadlistImage);

                if (!downloadFavoriteItemLists.get(position).isCustom()) {
                    Glide.with(context)
                            .load(downloadFavoriteItemLists.get(position).getFrame())
                            .into(((DownloadHolder) holder).binding.downloadlistFrame);

                }
                    ((DownloadHolder) holder).binding.shareIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onShareImageClick.onShareClick(model,position);

                        }
                    });


                break;
                case LAYOUT_DOWNLOADGRID:
                    Glide.with(context)
                            .load(downloadFavoriteItemLists.get(position).getImage())
                            .placeholder(R.drawable.placeholder)
                            .into(((DownloadHolderGrid) holder).binding.image);
                    Glide.with(context)
                            .load(downloadFavoriteItemLists.get(position).getFrame())
                            .into(((DownloadHolderGrid) holder).binding.frame);
                    ((DownloadHolderGrid)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((FrameCateItemeInterFace) context).FrameCateonItemSelection(position, model);
                        }
                    });
                    break;
                    case LAYOUT_FAVOURIT:
                            ((DownloadHolder) holder).binding.downloadListName.setText(downloadFavoriteItemLists.get(position).getName());
                            Glide.with(context)
                                    .load(downloadFavoriteItemLists.get(position).getImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(((DownloadHolder) holder).binding.downloadlistImage);
                            Glide.with(context)
                                    .load(downloadFavoriteItemLists.get(position).getFrame())
                                    .into(((DownloadHolder) holder).binding.downloadlistFrame);

                        ((DownloadHolder) holder).binding.shareIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onShareImageClick.onShareClick(model,position);
                                // activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            }
                        });




//                            ((DownloadHolder) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    Intent i = new Intent(context, ViewAllFavouritActivity.class);
//                                    i.putExtra("detailsObjj", gson.toJson(model));
//                                    context.startActivity(i);
//                                    i.addCategory(Intent.CATEGORY_HOME);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//
//                                    //  activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//                                }
//                            });
                            break;
                        case LAYOUT_FAVOURITGRID:
                            Glide.with(context)
                                    .load(downloadFavoriteItemLists.get(position).getImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(((DownloadHolderGrid) holder).binding.image);
                            Glide.with(context)
                                    .load(downloadFavoriteItemLists.get(position).getFrame())
                                    .into(((DownloadHolderGrid) holder).binding.frame);
                            ((DownloadHolderGrid)holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((FrameCateItemeInterFace) context).FrameCateonItemSelection(position, model);
                                }
                            });
                            break;

            }
        }
    }

        static class DownloadHolder extends RecyclerView.ViewHolder {
            DownloadlisItemListBinding binding;

            DownloadHolder(DownloadlisItemListBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;

            }
        }

        static class DownloadHolderGrid extends RecyclerView.ViewHolder {
            ItemDownloadGridBinding binding;

            DownloadHolderGrid(ItemDownloadGridBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;

            }
        }



}

//    public DownloadFavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.downloadlis_item_list, parent, false);
//        return new DownloadFavoriteAdapter.DownloadFavoritViewHolder(view);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull DownloadFavoritViewHolder holder, int position) {

//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return downloadFavoriteItemLists.size();
//
//    }
//
//    public class DownloadFavoritViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView textView;
//        CardView itemLayout;
//        public DownloadFavoritViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView=itemView.findViewById(R.id.downloadlistImage);
//            textView=itemView.findViewById(R.id.downloadListName);
//            itemLayout=itemView.findViewById(R.id.itemLayout);
//        }
//    }
//
//}
