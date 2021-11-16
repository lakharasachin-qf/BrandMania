package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.app.brandmania.Adapter.SelecBrandLIstAdeptor;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.databinding.FragmentListBottomListBinding;

import java.util.ArrayList;

public class SelectBrandListBottomFragment extends BottomSheetDialogFragment {
    ArrayList<BrandListItem> listModels;
    private Activity act;
    private View view;
    private SelecBrandLIstAdeptor adpt;
    private PreafManager preafManager;
    private FragmentListBottomListBinding binding;
    private int calledFlag;
    private SelectBrandListBottomFragment.setOnFilterSelection onFilterSelection;
    private SelectBrandListBottomFragment fragment;
    private String titleStr;
    private SelectBrandListBottomFragment context;
    private HomeFragment homeFragment;
    public static final int BRAND_NAME = 0;
    public static final int BRAND_CATEGORY = 1;
    private int layoutType;

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public SelectBrandListBottomFragment() {

        }

        public void setOnFilterSelection(com.app.brandmania.Fragment.bottom.SelectBrandListBottomFragment.setOnFilterSelection onFilterSelection) {
            this.onFilterSelection = onFilterSelection;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                CodeReUse.setWhiteNavigationBar(dialog, getActivity());
            }
            return dialog;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bottom_list, container, false);
            view = binding.getRoot();
            act = getActivity();
            context=this;
            fragment = this;
            preafManager=new PreafManager(act);

            listModels=preafManager.getAddBrandList();
            binding.titleText.setText("Your Brands");
            if (listModels != null) {
                adpt = new SelecBrandLIstAdeptor(listModels, act, calledFlag);
                adpt.setSelectBrandListBottomFragment(homeFragment);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
                binding.recyclerList.setLayoutManager(mLayoutManager);
                binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerList.setAdapter(adpt);
            }
            binding.searchLayout.setVisibility(View.GONE);
            binding.addBrandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.dismiss();
                    Intent i = new Intent(act, AddBrandMultipleActivity.class);
                    startActivity(i);
                    act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            });
            binding.recyclerList.requestFocus();
            binding.cancelAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!act.isDestroyed() && !act.isFinishing())
                        fragment.dismiss();
                }
            });
            return view;
        }

        public void setListData(int callingFlag, String title) {
            this.calledFlag = callingFlag;
            titleStr = title;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

        }

        @Override
        public void onDetach() {
            super.onDetach();

        }

        public interface setOnFilterSelection {
            void SetOnFilter(int filter, String selectedStr);
        }


    }
