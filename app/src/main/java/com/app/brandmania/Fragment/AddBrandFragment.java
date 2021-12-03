package com.app.brandmania.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Activity.PdfActivity;
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogAddBrandFragmentLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddBrandFragment extends BottomSheetDialogFragment {

    private DialogAddBrandFragmentLayoutBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_brand_fragment_layout, container, false);
        binding.cancelBtn.setOnClickListener(v -> dismiss());
        binding.continueBtn.setOnClickListener(v -> {
            dismiss();
            HELPER.ROUTE(getActivity(), AddBrandMultipleActivity.class);
        });
        return binding.getRoot();
    }
}