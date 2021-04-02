package com.app.brandmania.newModule.newFragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.R;
import com.app.brandmania.databinding.FragmentCustomFooterBinding;

public class CustomFooterFragment extends BaseFragment {

   private FragmentCustomFooterBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom_footer,parent,false);

        return binding.getRoot();
    }
}