package com.app.brandmania.newModule;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.IColorChange;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditingBinding;
import com.app.brandmania.newModule.newFragments.BackgroundColorFragment;
import com.app.brandmania.newModule.newFragments.CustomFooterFragment;
import com.app.brandmania.newModule.newFragments.DesignedFrameFragment;
import com.app.brandmania.newModule.newFragments.FragmentAdapter;
import com.app.brandmania.newModule.newFragments.ImagesFragment;
import com.app.brandmania.newModule.newFragments.TextEditingFragment;
import com.bumptech.glide.Glide;
import com.jaredrummler.android.colorpicker.ColorPickerView;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class EditingActivity extends BaseActivity   {


    private Activity act;
    private ActivityEditingBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_editing);
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding.viewpager.setOffscreenPageLimit(6);
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



}