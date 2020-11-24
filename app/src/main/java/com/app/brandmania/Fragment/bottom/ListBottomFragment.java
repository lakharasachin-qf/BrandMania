package com.app.brandmania.Fragment.bottom;

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
import com.app.brandmania.Adapter.DropDownAdpt;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentListBottomListBinding;

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
        binding.titleText.setText("Brand Category");

        if (listModels != null) {
            Log.e("SSSS", String.valueOf(listModels.size()));
            adpt = new DropDownAdpt(act, listModels, calledFlag);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);


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
