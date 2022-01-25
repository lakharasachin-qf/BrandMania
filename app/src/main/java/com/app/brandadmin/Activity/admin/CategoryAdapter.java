package com.app.brandadmin.Activity.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Model.BrandListItem;
import com.app.brandadmin.R;
import com.app.brandadmin.databinding.ItemCatagoryListBinding;
import com.app.brandadmin.databinding.ItemNotificationLayoutBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.app.brandadmin.Model.BrandListItem.LAYOUT_BRANDLISTBYID;

public class CategoryAdapter extends RecyclerView.Adapter implements Filterable {
    private ArrayList<CatModel> brandListItems;
    boolean isclick = true;
    Activity activity;
    private Gson gson;
    private boolean isLoadingAdded = false;
    PreafManager preafManager;
    private static final int REQUEST_CALL = 1;
    private BRANDBYIDIF brandbyidif;
    ArrayList<CatModel> tmpArray;
    CustomeFilter cust;
    handleSelectionEvent selectionEvent;

    public interface handleSelectionEvent{
        void selectionEvent(CatModel itemmodel, int position,String flag);
    }

    public void setInteface(handleSelectionEvent inteface){
        selectionEvent=inteface;
    }

    public void setBrandbyidif(BRANDBYIDIF brandbyidif) {
        this.brandbyidif = brandbyidif;
    }

    public interface BRANDBYIDIF {
        void fireBrandList(int position, BrandListItem model);
    }

    public CategoryAdapter(int resource,ArrayList<CatModel> brandListItems, Activity activity) {
        this.activity = activity;
        gson = new Gson();
        preafManager = new PreafManager(activity);
        this.isLoadingAdded = isLoadingAdded;
        tmpArray = brandListItems;
        this.brandListItems = tmpArray;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {

            case 0:
            case LAYOUT_BRANDLISTBYID:
                ItemCatagoryListBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_catagory_list, viewGroup, false);
                return new BrandHolder(layoutBinding);

        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return brandListItems.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CatModel model = brandListItems.get(position);
        if (model != null) {

            ((BrandHolder) holder).binding.catagoryTitle.setText(model.getCategoryName());
         //   Log.e("CurrentBrand", model.getCategoryName());
            Glide.with(activity)
                    .load(brandListItems.get(position).getThumbnailUrl())
                    .placeholder(R.drawable.filter_logo)
                    .into(((BrandHolder) holder).binding.catagoryImage);

          //  Log.e("brandlist" , new Gson().toJson(brandListItems));

            ((BrandHolder) holder).binding.categorylistCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // ((BrandHolder) holder).binding.active.performClick();
                    // ((BrandHolder) holder).binding.inactive.performClick();
                }
            });

//            ((BrandHolder) holder).binding.catagoryDate.setText(model.));
//            ((BrandHolder) holder).binding.catagoryImage.setText(model.getAddress());
//            ((BrandHolder) holder).binding.active.setBackgroundTintList(ColorStateList.valueOf(holo_red_dark));

            /*To change color and state when button is clicked*/
            ((BrandHolder) holder).binding.active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        model.setActive(false);
                        ((BrandHolder) holder).binding.inactive.setVisibility(View.VISIBLE);
                        ((BrandHolder) holder).binding.active.setVisibility(View.GONE);
                        selectionEvent.selectionEvent(model,position,"Inactive");
                }
            });

            ((BrandHolder) holder).binding.inactive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setActive(true);
                    ((BrandHolder) holder).binding.active.setVisibility(View.VISIBLE);
                    ((BrandHolder) holder).binding.inactive.setVisibility(View.GONE);
                    selectionEvent.selectionEvent(model,position,"Active");
                }
            });

            if (model.isActive()){
                ((BrandHolder) holder).binding.active.setVisibility(View.VISIBLE);
                ((BrandHolder) holder).binding.inactive.setVisibility(View.GONE);
            }
            else {
                ((BrandHolder) holder).binding.inactive.setVisibility(View.VISIBLE);
                ((BrandHolder) holder).binding.active.setVisibility(View.GONE);
            }
        }
    }

    static class BrandHolder extends RecyclerView.ViewHolder {
        ItemCatagoryListBinding binding;

        BrandHolder(ItemCatagoryListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    static class NotificationHolder extends RecyclerView.ViewHolder {
        ItemNotificationLayoutBinding binding;

        NotificationHolder(ItemNotificationLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (cust == null) {
            cust = new CustomeFilter();
        }
        return cust;
    }

    class CustomeFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraints) {

            FilterResults filterResults = new FilterResults();
            ArrayList<CatModel> searchArray = new ArrayList<>();

            if (constraints != null && constraints.length() > 0) {
                constraints = constraints.toString().toUpperCase();
                for (int i = 0; i < tmpArray.size(); i++) {
                    if (tmpArray.get(i).getCategoryName().toUpperCase().contains(constraints)) {
                        searchArray.add(tmpArray.get(i));
                 //       Log.e("CategoryList", new Gson().toJson(searchArray.size()));
                    }
                }
                filterResults.values = searchArray;
                filterResults.count = searchArray.size();

            }else{
                filterResults.count = tmpArray.size();
                filterResults.values = tmpArray;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            brandListItems = (ArrayList<CatModel>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}