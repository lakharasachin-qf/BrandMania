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

import static com.app.brandmania.utils.Utility.Log;

public class DropDownAdpt extends RecyclerView.Adapter<DropDownAdpt.TenamentHolder> {

    private ArrayList<CommonListModel> arrayList;
    private Activity act;
    private int checkedPosition = -1;
    private int calledFlag;

    private HandlerFragmentSelection fragmentSelection = null;

    public DropDownAdpt(Activity act, ArrayList<CommonListModel> arrayList, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
    }

    public void setFragmentSelection(HandlerFragmentSelection fragmentSelection) {
        this.fragmentSelection = fragmentSelection;
    }

    @Override
    public TenamentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new TenamentHolder(view);
    }


    @Override
    public void onBindViewHolder(final TenamentHolder holder, @SuppressLint("RecyclerView") int position) {
        CommonListModel listModel = arrayList.get(position);

        holder.radioButton.setText(convertFirstUpper(listModel.getName()));

        if (position == checkedPosition) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.radioButton.setChecked(true);
                checkedPosition = position;
                notifyDataSetChanged();
                if (fragmentSelection != null) {
                    fragmentSelection.handlerFragmentSelection(calledFlag, position, listModel);
                } else {
                    ((ItemSelectionInterface) act).onItemSelection(calledFlag, position, listModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface HandlerFragmentSelection {
        void handlerFragmentSelection(int calledFlag, int position, CommonListModel listModel);
    }

    public class TenamentHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public TenamentHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButton);
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
