package com.app.brandadmin.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Fragment.bottom.HomeFragment;
import com.app.brandadmin.Interface.ItemMultipleSelectionInterface;
import com.app.brandadmin.Model.BrandListItem;
import com.app.brandadmin.R;

import java.util.ArrayList;

import static com.app.brandadmin.Utils.Utility.Log;

public class SelecBrandLIstAdeptor extends RecyclerView.Adapter<SelecBrandLIstAdeptor.SelecBrandLIstHolder> {

    private ArrayList<BrandListItem> arrayList;
    private Activity act;
    private PreafManager preafManager;
    private int checkedPosition = -1;
    private int calledFlag;
    private HomeFragment selectBrandListBottomFragment;
    private HandlerFragmentSelection fragmentSSelection = null;

    public void setSelectBrandListBottomFragment(HomeFragment selectBrandListBottomFragment) {
        this.selectBrandListBottomFragment = selectBrandListBottomFragment;
    }

    public SelecBrandLIstAdeptor(ArrayList<BrandListItem> arrayList, Activity act, int calledFlag) {
        this.arrayList = arrayList;
        this.act = act;
        this.calledFlag = calledFlag;
        preafManager=new PreafManager(act);
    }

    public void setFragmentSelection(HandlerFragmentSelection fragmentSSelection) {
        this.fragmentSSelection = fragmentSSelection;
    }

    @Override
    public SelecBrandLIstAdeptor.SelecBrandLIstHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);
        return new SelecBrandLIstAdeptor.SelecBrandLIstHolder(view);
    }


    @Override
    public void onBindViewHolder(final SelecBrandLIstAdeptor.SelecBrandLIstHolder holder, int position) {
        BrandListItem listModel = arrayList.get(position);
        if(preafManager.getActiveBrand().getId().equals(listModel.getId()))
        {
            holder.radioButton.setChecked(true);
            checkedPosition = position;

        }

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
                if (selectBrandListBottomFragment != null) {
                    ((ItemMultipleSelectionInterface) selectBrandListBottomFragment).onItemSMultipleelection(calledFlag, position, listModel);


                } else {
                     ((ItemMultipleSelectionInterface) selectBrandListBottomFragment).onItemSMultipleelection(calledFlag, position, listModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface HandlerFragmentSelection {
        void handlerFragmentSSelection(int calledFlag, int position, BrandListItem listModel);
    }

    public class SelecBrandLIstHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public SelecBrandLIstHolder(@NonNull View itemView) {
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
