package com.make.mybrand.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.make.mybrand.Model.ImageList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DashboardItemLayoutBinding;
import com.make.mybrand.databinding.LanguageFilterBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LanguageFilterAdaptor extends RecyclerView.Adapter<LanguageFilterAdaptor.LanguageViewHolder> {
    private final ArrayList<String> filterItems;
    Activity activity;
    public static final int FROM_VIEWALLS = 0;
    String imageList;
    Integer row_index = 0;
    private ImageList selectedObject;
    public onItemSelectListener onItemSelectListener;
    Gson gson = new Gson();

    public void setOnItemSelectListener(LanguageFilterAdaptor.onItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface onItemSelectListener {
        void onItemSelect(String Data, int position);
    }

    int layoutType;

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public LanguageFilterAdaptor(ArrayList<String> filterItems, Activity activity) {
        this.filterItems = filterItems;
        this.activity = activity;

    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguageFilterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.language_filter, parent, false);
        return new LanguageFilterAdaptor.LanguageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        imageList = filterItems.get(position);
        //Log.e("filter data", gson.toJson(imageList));
        holder.binding.filterChip.setText(imageList);
        holder.binding.filterChip.setOnClickListener(v -> {
            row_index = position;
            onItemSelectListener.onItemSelect(imageList, position);
            notifyDataSetChanged();
        });

        if (row_index == position) {
            holder.binding.filterChip.setChecked(true);
            holder.binding.filterChip.setTextColor(ContextCompat.getColor(activity, R.color.white));
            holder.binding.filterChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorPrimary)));
            holder.binding.filterChip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorthird)));

        } else {
            holder.binding.filterChip.setChecked(false);
            holder.binding.filterChip.setTextColor(ContextCompat.getColor(activity, R.color.black));
            holder.binding.filterChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.white)));
            holder.binding.filterChip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorthird)));
        }

    }

    @Override
    public int getItemCount() {
        return filterItems.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder {
        LanguageFilterBinding binding;

        public LanguageViewHolder(LanguageFilterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
