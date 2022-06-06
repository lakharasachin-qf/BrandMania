package com.app.brandmania.Adapter;

import static com.app.brandmania.Model.DashBoardItem.BUSINESS_DETAIL_IMAGES;
import static com.app.brandmania.Model.DashBoardItem.DAILY_IMAGES;
import static com.app.brandmania.Model.DashBoardItem.FESTIVAL_IMAGES;
import static com.app.brandmania.utils.Utility.Log;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.details.BusinessCategoryListActivity;
import com.app.brandmania.Activity.details.DailyCategoryListActivity;
import com.app.brandmania.Activity.details.ImageCategoryDetailActivity;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DashboardItemLayoutBinding;
import com.app.brandmania.utils.SpacesItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class DasboardAddaptor extends RecyclerView.Adapter {
    private ArrayList<DashBoardItem> dashBoardItemList;
    private final Gson gson;
    Activity activity;
    RecyclerView.RecycledViewPool viewPool;
    public DasboardAddaptor(ArrayList<DashBoardItem> dashBoardItemList, Activity activity) {
        this.dashBoardItemList = dashBoardItemList;
        this.activity = activity;
        gson = new Gson();
          viewPool = new RecyclerView.RecycledViewPool();

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case DAILY_IMAGES:
                DashboardItemLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dashboard_item_layout, viewGroup, false);

                return new DasboardViewHolder(layoutBinding);
            case FESTIVAL_IMAGES:
            case BUSINESS_DETAIL_IMAGES:
                layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dashboard_item_layout, viewGroup, false);
                return new DasboardViewHolder(layoutBinding);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dashBoardItemList.get(position).getLayout()) {
            case 0:
                return DAILY_IMAGES;
            case 1:
                return FESTIVAL_IMAGES;
            case 2:
                return BUSINESS_DETAIL_IMAGES;
            default:
                return 0;
        }

    }

    @Override
    public int getItemCount() {
        return dashBoardItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false) ;//Disable recycling

        final DashBoardItem model = dashBoardItemList.get(position);
        if (model != null) {
            switch (model.getLayout()) {
                case BUSINESS_DETAIL_IMAGES:
                    ((DasboardViewHolder) holder).binding.viewAll.setVisibility(View.GONE);
                    ((DasboardViewHolder) holder).binding.title.setText(convertFirstUpper(dashBoardItemList.get(position).getName()));
                    ((DasboardViewHolder) holder).binding.title.setSelected(true);
                    ImageCategoryAddaptor menuAddaptor2 = new ImageCategoryAddaptor(dashBoardItemList.get(position).getDailyImages(), activity);
                    menuAddaptor2.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
                    RecyclerView.LayoutManager mLayoutManagder = new GridLayoutManager(activity, 3);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setLayoutManager(mLayoutManagder);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setHasFixedSize(true);
                    menuAddaptor2.setDashBoardItem(dashBoardItemList.get(position));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setRecycledViewPool(viewPool);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.addItemDecoration(new SpacesItemDecoration(activity.getResources().getDimensionPixelSize(R.dimen.space)));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setAdapter(menuAddaptor2);
                    ((DasboardViewHolder) holder).binding.viewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, ImageCategoryDetailActivity.class);
                            i.putExtra("viewAll", "12");
                            i.putExtra("detailsObj", gson.toJson(dashBoardItemList.get(position)));
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(i);
                            activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        }
                    });
                    break;
                case DashBoardItem.FESTIVAL_IMAGES:
                    ((DasboardViewHolder) holder).binding.title.setText(convertFirstUpper(dashBoardItemList.get(position).getName()));
                    ((DasboardViewHolder) holder).binding.title.setSelected(true);
                    ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(dashBoardItemList.get(position).getImageLists(), activity);
                    menuAddaptor.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setHasFixedSize(true);
                    menuAddaptor.setDashBoardItem(dashBoardItemList.get(position));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setRecycledViewPool(viewPool);

                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setAdapter(menuAddaptor);
                    ((DasboardViewHolder) holder).binding.viewAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(activity, ImageCategoryDetailActivity.class);
                            i.putExtra("viewAll", "12");
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
                    menuAddaptor = new ImageCategoryAddaptor(dashBoardItemList.get(position).getDailyImages(), activity);
                    menuAddaptor.setLayoutType(ImageCategoryAddaptor.FROM_HOMEFRAGEMENT);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.addItemDecoration(new SpacesItemDecoration(activity.getResources().getDimensionPixelSize(R.dimen.space)));

                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 3);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setLayoutManager(mLayoutManager);
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setHasFixedSize(true);

                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setRecycledViewPool(viewPool);

                    menuAddaptor.setDashBoardItem(dashBoardItemList.get(position));
                    ((DasboardViewHolder) holder).binding.imageCategoryRecycler.setAdapter(menuAddaptor);

                    if (dashBoardItemList.get(position).getName().contains("Business") || dashBoardItemList.get(position).getName().contains("Daily")) {
                        ((DasboardViewHolder) holder).binding.viewAll.setVisibility(View.VISIBLE);
                        ((DasboardViewHolder) holder).binding.viewAll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dashBoardItemList.get(position).getName().contains("Business")) {
                                    Intent i = new Intent(activity, BusinessCategoryListActivity.class);
                                    i.putExtra("title", dashBoardItemList.get(position).getName());
                                    activity.startActivity(i);
                                } else if (dashBoardItemList.get(position).getName().contains("Daily")) {
                                    Intent i = new Intent(activity, DailyCategoryListActivity.class);
                                    i.putExtra("title", dashBoardItemList.get(position).getName());
                                    activity.startActivity(i);
                                }
                            }
                        });
                    } else {
                        ((DasboardViewHolder) holder).binding.viewAll.setVisibility(View.GONE);
                    }
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
            binding = itemView;
        }


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public static String convertFirstUpper(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}

