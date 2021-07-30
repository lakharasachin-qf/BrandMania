package com.app.brandadmin.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandadmin.Activity.details.ImageCategoryDetailActivity;
import com.app.brandadmin.Model.DashBoardItem;
import com.app.brandadmin.R;
import com.app.brandadmin.databinding.DashboardItemLayoutBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.app.brandadmin.Model.DashBoardItem.DAILY_IMAGES;
import static com.app.brandadmin.Model.DashBoardItem.FESTIVAL_IMAGES;
import static com.app.brandadmin.Utils.Utility.Log;


public class DasboardAddaptor extends RecyclerView.Adapter {
    private ArrayList<DashBoardItem> dashBoardItemList;
    private final Gson gson;
    Activity activity;
    public DasboardAddaptor(ArrayList<DashBoardItem> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson=new Gson();
    }


    @NonNull
    @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case DAILY_IMAGES:
                DashboardItemLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dashboard_item_layout, viewGroup, false);
                return new DasboardViewHolder(layoutBinding);
            case FESTIVAL_IMAGES:
                layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dashboard_item_layout, viewGroup, false);
                return new DasboardViewHolder(layoutBinding);
        }
        return null;
    }
    @Override public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayout()) {
            case 0:
                return DAILY_IMAGES;
            case 1:
                return FESTIVAL_IMAGES;
            default:
                return -1;
        }

    }
    @Override public int getItemCount() {
        return dashBoardItemList.size();
    }
    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DashBoardItem model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayout()) {
                case DashBoardItem.FESTIVAL_IMAGES:
                    ((DasboardViewHolder) holder).binding.title.setText(convertFirstUpper(dashBoardItemList.get(position).getName()));
                    ((DasboardViewHolder) holder).binding.title.setSelected(true);
                    Log.e("LLLLLL", String.valueOf(dashBoardItemList.get(position).getImageLists().size()));
                    ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(dashBoardItemList.get(position).getImageLists(), activity);
                    menuAddaptor.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setHasFixedSize(true);
                    menuAddaptor.setDashBoardItem(dashBoardItemList.get(position));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setAdapter(menuAddaptor);
                    ((DasboardViewHolder) holder).binding.viewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, ImageCategoryDetailActivity.class);
                            i.putExtra("viewAll","12");
                            i.putExtra("detailsObj", gson.toJson(dashBoardItemList.get(position)));
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(i);

                            activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                        }
                    });
                    break;
                case DAILY_IMAGES:

                    ((DasboardViewHolder) holder).binding.title.setText(convertFirstUpper(dashBoardItemList.get(position).getName()));
                    ((DasboardViewHolder) holder).binding.viewAll.setVisibility(View.GONE);
                   // ((DasboardViewHolder) holder).binding.rootBackground.setVisibility(View.GONE);
                    menuAddaptor = new ImageCategoryAddaptor(dashBoardItemList.get(position).getDailyImages(), activity);
                    menuAddaptor.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
                   // int spacingInPixels = activity.getResources().getDimensionPixelSize(R.dimen.space);
                   // ((DasboardViewHolder) holder).binding.imageCategoryRecycler.addItemDecoration(new SpacesItemDecoration(3, spacingInPixels, true, 0));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setHasFixedSize(true);
                    menuAddaptor.setDashBoardItem(dashBoardItemList.get(position));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setAdapter(menuAddaptor);
                    break;

            }
        }
    }


     public void setfilter(List<DashBoardItem> listitem) {
        dashBoardItemList = new ArrayList<>();
        dashBoardItemList.addAll(listitem);
        notifyDataSetChanged();
    }
    public class DasboardViewHolder extends RecyclerView.ViewHolder {
        DashboardItemLayoutBinding binding;
        public DasboardViewHolder(DashboardItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }


    }
    public static String convertFirstUpper(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}

