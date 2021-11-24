package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Model.BackgroundColorsModel;
import com.app.brandmania.Model.TextColorsModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ItemColorsForCardLayoutBinding;

import java.util.ArrayList;

public class TextColorsAdapter extends RecyclerView.Adapter<TextColorsAdapter.ColorHolder> {
    private ArrayList<TextColorsModel> arrayList;
    private Activity activity;

    private onItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(TextColorsAdapter.onItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface onItemSelectListener {
        void onItemSelect(TextColorsModel model, int position);
    }

    public TextColorsAdapter(ArrayList<TextColorsModel> fontModels, Activity activity) {
        this.arrayList = fontModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorsForCardLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_colors_for_card_layout, parent, false);
        return new ColorHolder(layoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.view.setBackgroundTintList(ColorStateList.valueOf(arrayList.get(position).getColor()));

        holder.binding.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((onItemSelectListener)).onItemSelect(arrayList.get(position), position);
            }
        });
        if (arrayList.get(position).isSelected()) {
            holder.binding.itemLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.colorPrimary)));
        } else {
            holder.binding.itemLayout.setBackgroundTintList(ColorStateList.valueOf(arrayList.get(position).getColor()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ColorHolder extends RecyclerView.ViewHolder {
        ItemColorsForCardLayoutBinding binding;

        public ColorHolder(@NonNull ItemColorsForCardLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
