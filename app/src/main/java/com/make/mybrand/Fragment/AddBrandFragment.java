package com.make.mybrand.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.make.mybrand.Activity.brand.AddBrandMultipleActivity;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DialogAddBrandFragmentLayoutBinding;
import com.make.mybrand.utils.Utility;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddBrandFragment extends BottomSheetDialogFragment {

    private DialogAddBrandFragmentLayoutBinding binding;
    public boolean isAddBrandFromImageCat = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    public void setAddBrandFromImageCat(boolean isBrand) {
        isAddBrandFromImageCat = isBrand;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_brand_fragment_layout, container, false);
        binding.cancelBtn.setOnClickListener(v -> dismiss());

        if (isAddBrandFromImageCat) {
            Utility.Log("fromImageCat", "yes");
            binding.continueBtn.setOnClickListener(v -> {
                dismiss();
                Intent i = new Intent(getActivity(), AddBrandMultipleActivity.class);
                i.putExtra("fromImageCat", "yes");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                // HELPER.ROUTE(getActivity(), AddBrandMultipleActivity.class);
            });

        } else {
            binding.continueBtn.setOnClickListener(v -> {
                dismiss();
                HELPER.ROUTE(getActivity(), AddBrandMultipleActivity.class);
            });
        }

        return binding.getRoot();
    }
}