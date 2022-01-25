package com.app.brandadmin.Fragment.bottom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app.brandadmin.Adapter.DropDownAdpt;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Model.CommonListModel;
import com.app.brandadmin.R;
import com.app.brandadmin.databinding.FragmentListBottomListBinding;

import java.util.ArrayList;

public class ListBottomFragment  extends BottomSheetDialogFragment {
    ArrayList<CommonListModel> listModels;
    private Activity act;
    private View view;
    private DropDownAdpt adpt;
    private PreafManager preafManager;
    private FragmentListBottomListBinding binding;
    private int calledFlag;
    private setOnFilterSelection onFilterSelection;
    private ListBottomFragment fragment;
    private String titleStr;

    public ListBottomFragment() {

    }
    public void setOnFilterSelection(setOnFilterSelection onFilterSelection) {
        this.onFilterSelection = onFilterSelection;
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            CodeReUse.setWhiteNavigationBar(dialog, getActivity());
        }
        return dialog;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bottom_list, container, false);
        view = binding.getRoot();
        act = getActivity();
        fragment = this;
        preafManager=new PreafManager(act);
        binding.titleText.setText("Select Option");

        if (listModels != null) {
            Log.e("SSSS", String.valueOf(listModels.size()));
            adpt = new DropDownAdpt(act, listModels, calledFlag);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);

        }

        if (calledFlag==0){
            binding.addBrandLayout.setVisibility(View.GONE);
        }
        binding.recyclerList.requestFocus();

        return view;
    }
    public void setListData(int callingFlag, String title, ArrayList<CommonListModel> listData) {
        this.calledFlag = callingFlag;
        listModels = listData;
        titleStr = title;
     }

    @Override public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override public void onDetach() {
        super.onDetach();

    }
    public interface setOnFilterSelection {
        void SetOnFilter(int filter, String selectedStr);
    }

}