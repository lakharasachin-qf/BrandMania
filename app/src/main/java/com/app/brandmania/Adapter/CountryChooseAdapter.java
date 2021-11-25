package com.app.brandmania.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Interface.ItemSelectionInterface;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;

import java.util.ArrayList;

public class CountryChooseAdapter extends RecyclerView.Adapter<CountryChooseAdapter.SelecBrandLIstHolder> {

    private ArrayList<CommonListModel> arrayList;
    private final Activity act;
    private int checkedPosition = -1;
    private final int calledFlag;


    public CountryChooseAdapter(ArrayList<CommonListModel> arrayList, Activity act, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @NonNull
    @Override
    public SelecBrandLIstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new SelecBrandLIstHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelecBrandLIstHolder holder, int position) {
        CommonListModel listModel = arrayList.get(position);

        holder.radioButton.setText(convertFirstUpper(listModel.getName()));
        holder.radioButton.setOnClickListener(v -> holder.itemView.performClick());
        holder.itemView.setOnClickListener(view -> {
            holder.radioButton.setChecked(true);
            // checkedPosition = position;
            ((ItemSelectionInterface) act).onItemSelection(calledFlag, position, listModel);
            notifyDataSetChanged();
        });

        if (!previouslySelectData.isEmpty() && previouslySelectData.equalsIgnoreCase(listModel.getName())) {
            holder.radioButton.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    String previouslySelectData = "";

    public void setPreviouslySelectedData(String previouslySelectData) {
        this.previouslySelectData = previouslySelectData;
    }

    public static class SelecBrandLIstHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;


        public SelecBrandLIstHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<CommonListModel> list) {
        arrayList = list;
        notifyDataSetChanged();
    }
}
