package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Activity.packages.RazorPayActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DynamicServiceLayoutBinding;
import com.app.brandmania.databinding.ItemPackageLayoutBinding;
import com.app.brandmania.utils.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PackageRecyclerAdapter extends RecyclerView.Adapter<PackageRecyclerAdapter.PackageViewHolder> {
    private ArrayList<SliderItem> packageList;
    private Activity activity;
    private Gson gson;
    private BrandListItem selectedBrand;
    PreafManager preafManager;

    public PackageRecyclerAdapter(ArrayList<SliderItem> packageList, Activity activity, BrandListItem selectedBrand) {
        this.packageList = packageList;
        this.activity = activity;
        this.gson = new Gson();
        preafManager = new PreafManager(activity);
        this.selectedBrand = selectedBrand;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_layout, parent, false);
        ItemPackageLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_package_layout, parent, false);

        return new PackageViewHolder(layoutBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SliderItem model = packageList.get(position);

        holder.binding.price.setText(activity.getString(R.string.Rs) + model.getPriceForPay());
        holder.binding.packageName.setText(model.getPackageTitle());
        holder.binding.planLabel.setText("Plan " + String.valueOf(position + 1));

        holder.binding.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedBrand.getPackagename() != null && !selectedBrand.getPackagename().isEmpty() && selectedBrand.getPackagename().equalsIgnoreCase(model.getPackageTitle())) {
                    if (Utility.isPackageExpired(selectedBrand)) {
                        Intent intent = new Intent(activity, RazorPayActivity.class);
                        intent.putExtra("AmountText", model.getPriceForPay());
                        intent.putExtra("BrandListItem", gson.toJson(selectedBrand));
                        packageList.get(position).setBrandId(selectedBrand.getId());
                        intent.putExtra("detailsObj", gson.toJson(packageList.get(position)));
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                } else {
                    Intent intent = new Intent(activity, RazorPayActivity.class);
                    intent.putExtra("AmountText", model.getPriceForPay());
                    intent.putExtra("BrandListItem", gson.toJson(selectedBrand));
                    packageList.get(position).setBrandId(selectedBrand.getId());
                    intent.putExtra("detailsObj", gson.toJson(packageList.get(position)));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            }
        });


        if (activity.getIntent().hasExtra("Profile") && preafManager.getActiveBrand().getPackage_id().equals(packageList.get(position).getPackageid()) &&
                preafManager.getActiveBrand().getIs_payment_pending().equalsIgnoreCase("0")) {
            holder.binding.rightArrow.setVisibility(View.GONE);
            holder.binding.secondaryTitle.setClickable(false);
            holder.binding.secondaryTitle.setText("Subscribed");
            holder.binding.itemLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.shape_package_outline_active));
            if (Utility.isPackageExpired(activity)) {
                holder.binding.secondaryTitle.setText("Expired");
                holder.binding.alert.setVisibility(View.VISIBLE);
            } else {
                holder.binding.alert.setVisibility(View.GONE);
            }
        } else {
            holder.binding.rightArrow.setVisibility(View.VISIBLE);
            holder.binding.itemLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.shape_package_outline));
            holder.binding.alert.setVisibility(View.GONE);
        }


        if (packageList.get(position).getSlideSubItems() != null && packageList.get(position).getSlideSubItems().get(0) != null && !packageList.get(position).getSlideSubItems().get(0).getDescription().equalsIgnoreCase("")) {
            String description = packageList.get(position).getSlideSubItems().get(0).getDescription();
            String[] serviceArray = description.split(",");
            addDynamicServices(holder.binding.serviceLayout, serviceArray);
        }
    }

    private void addDynamicServices(LinearLayout serviceLayout, String[] serviceArray) {
        for (String s : serviceArray) {
            DynamicServiceLayoutBinding dynamicTextBinding;
            dynamicTextBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dynamic_service_layout, null, false);
            if (s.contains("Video")) {
                dynamicTextBinding.newLabels.setVisibility(View.VISIBLE);
            }
            dynamicTextBinding.label.setText(s);
            serviceLayout.addView(dynamicTextBinding.getRoot());
        }

    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class PackageViewHolder extends RecyclerView.ViewHolder {
        ItemPackageLayoutBinding binding;

        public PackageViewHolder(@NonNull ItemPackageLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }


}
