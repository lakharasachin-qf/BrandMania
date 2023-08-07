package com.make.mybrand.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Interface.FrameCateItemeInterFace;
import com.make.mybrand.Model.DownloadFavoriteItemList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DownloadlisItemListBinding;
import com.make.mybrand.databinding.ItemDownloadGridBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.make.mybrand.Model.DownloadFavoriteItemList.LAYOUT_DOWNLOAD;
import static com.make.mybrand.Model.DownloadFavoriteItemList.LAYOUT_DOWNLOADGRID;
import static com.make.mybrand.Model.DownloadFavoriteItemList.LAYOUT_FAVOURIT;
import static com.make.mybrand.Model.DownloadFavoriteItemList.LAYOUT_FAVOURITGRID;
import static com.make.mybrand.Model.ImageList.LAYOUT_LOADING;

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
    private onShareVideoClick onShareVideoClick;


    public DownloadFavoriteAdapter.onShareImageClick getOnShareImageClick() {
        return onShareImageClick;
    }

    public void setOnShareImageClick(DownloadFavoriteAdapter.onShareImageClick onShareImageClick) {
        this.onShareImageClick = onShareImageClick;
    }

    public void setOnShareVideoClick(DownloadFavoriteAdapter.onShareVideoClick onShareVideoClick) {
        this.onShareVideoClick = onShareVideoClick;
    }

    public interface onShareImageClick {
        public void onShareClick(DownloadFavoriteItemList favoriteItemList, int position);
    }

    public interface onShareVideoClick {
        public void onShareClick(DownloadFavoriteItemList favoriteItemList, int position);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final DownloadFavoriteItemList model = downloadFavoriteItemLists.get(position);
        if (model != null) {
            switch (model.getLayoutType()) {
                case LAYOUT_DOWNLOAD:
                    HELPER.print("DOWNLOAD_RESPONSE::", gson.toJson(downloadFavoriteItemLists.get(position)));
                    ((DownloadHolder) holder).binding.downloadListName.setText(downloadFavoriteItemLists.get(position).getName());
                    if (downloadFavoriteItemLists.get(position).getName().contains("custom_default_placeholder")) {
                        String url = downloadFavoriteItemLists.get(position).getImage();
                        URL urlObj = null;
                        try {
                            urlObj = new URL(url);
                            String urlPath = urlObj.getPath();
                            String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
                            ((DownloadHolder) holder).binding.downloadListName.setText(fileName);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }

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
                            if (downloadFavoriteItemLists.get(position).getImage().endsWith(".mp4")) {
                                onShareVideoClick.onShareClick(model, position);
                            } else {
                                onShareImageClick.onShareClick(model, position);
                            }
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
                    ((DownloadHolderGrid) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
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
                            onShareImageClick.onShareClick(model, position);
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
                    ((DownloadHolderGrid) holder).binding.itemLayout.setOnClickListener(new View.OnClickListener() {
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