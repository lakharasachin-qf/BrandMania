package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.DropDownAdpt;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;
import com.app.brandmania.utils.CodeReUse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ListBottomFragment extends BottomSheetDialogFragment {
    ArrayList<CommonListModel> listModels;
    private ArrayList<CommonListModel> rootList;
    private Activity act;
    private DropDownAdpt adpt;
    private int calledFlag;

    private ListBottomFragment fragment;

    public ListBottomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            CodeReUse.setWhiteNavigationBar(dialog, getActivity());
        }
        this.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        com.app.brandmania.databinding.FragmentListBottomListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_bottom_list, container, false);
        View view = binding.getRoot();
        act = getActivity();
        fragment = this;
        binding.titleText.setText("Brand Category");


        if (listModels != null) {
            adpt = new DropDownAdpt(act, listModels, calledFlag);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 3);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);
            binding.recyclerList.setNestedScrollingEnabled(false);
            binding.searchEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterCountry(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

//        if (listModels != null) {
//            adpt = new DropDownAdpt(act, listModels, calledFlag);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
//            binding.recyclerList.setLayoutManager(mLayoutManager);
//            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
//            binding.recyclerList.setAdapter(adpt);
//            binding.recyclerList.setNestedScrollingEnabled(false);
//            binding.searchEdt.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    filterCountry(s.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//        }
        if (calledFlag == 0) {
            binding.addBrandLayout.setVisibility(View.GONE);
        }
        binding.recyclerList.requestFocus();
        binding.cancelAction.setOnClickListener(v -> {
            if (!act.isDestroyed() && !act.isFinishing())
                fragment.dismiss();
        });
        return view;
    }

    void filterCountry(String text) {
        ArrayList<CommonListModel> temp = new ArrayList<>();
        for (CommonListModel d : rootList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        adpt.updateList(temp);
    }

    public void setListData(int callingFlag, String title, ArrayList<CommonListModel> datalist) {
        this.calledFlag = callingFlag;
        rootList = new ArrayList<>(datalist);
        listModels = new ArrayList<>(datalist);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
