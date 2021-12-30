package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.app.Dialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brandmania.Adapter.CountryChooseAdapter;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentCountryLayoutBinding;
import com.app.brandmania.utils.CodeReUse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class CountrySelectionFragment extends BottomSheetDialogFragment {
    ArrayList<CommonListModel> listModels;
    private Activity act;
    private View view;
    private CountryChooseAdapter adpt;
    private FragmentCountryLayoutBinding binding;
    private int calledFlag;
    private setOnFilterSelection onFilterSelection;
    private CountrySelectionFragment fragment;
    private String titleStr;
    private String previouslySelectData;

    private ArrayList<CommonListModel> rootList;


    public CountrySelectionFragment(String title, ArrayList<CommonListModel> datalist, int callingFlag, String alreadySelectedData) {
        rootList = new ArrayList<>(datalist);
        listModels = new ArrayList<>(datalist);
        this.calledFlag = callingFlag;
        titleStr = title;
        this.previouslySelectData = alreadySelectedData;
    }

    public void setOnFilterSelection(setOnFilterSelection onFilterSelection) {
        this.onFilterSelection = onFilterSelection;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_country_layout, container, false);
        view = binding.getRoot();
        act = getActivity();
        fragment = this;
        assert act != null;
        binding.titleText.setText(titleStr);

        if (listModels != null) {
            adpt = new CountryChooseAdapter(listModels, act, calledFlag);
            adpt.setPreviouslySelectedData(previouslySelectData);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            binding.recyclerList.setLayoutManager(mLayoutManager);
            binding.recyclerList.setItemAnimator(new DefaultItemAnimator());
            binding.recyclerList.setAdapter(adpt);
            binding.recyclerList.setNestedScrollingEnabled(true);
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

    void filterCountry(String text) {
        ArrayList<CommonListModel> temp = new ArrayList<>();
        for (CommonListModel d : rootList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        adpt.updateList(temp);
    }

    public void setListData(int callingFlag, String title, ArrayList<CommonListModel> listData) {
        rootList = new ArrayList<>(listData);
        listModels = new ArrayList<>(listData);
        this.calledFlag = callingFlag;
        titleStr = title;
    }

    public interface setOnFilterSelection {
        void SetOnFilter(int filter, String selectedStr);
    }


}
