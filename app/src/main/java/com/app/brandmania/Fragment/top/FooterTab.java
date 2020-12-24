package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.brandmania.R;
import com.app.brandmania.databinding.CategoryTabBinding;
import com.app.brandmania.databinding.FooterTabBinding;

public class FooterTab extends Fragment {
    Activity act;
    private FooterTabBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.footer_tab, container, false);
        return binding.getRoot();
    }
}
