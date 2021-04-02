package com.app.brandmania.newModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditingBinding;
import com.app.brandmania.newModule.newFragments.CustomFooterFragment;
import com.app.brandmania.newModule.newFragments.FragmentAdapter;
import com.app.brandmania.newModule.newFragments.ImagesFragment;

import java.util.ArrayList;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;

public class EditingActivity extends BaseActivity {


    private Activity act;
    private ActivityEditingBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_editing);
        act.getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN);
        binding.viewpager.setOffscreenPageLimit(5);
        setUpFragment();
    }




    private CustomFooterFragment footerFragment;
    private ImagesFragment imagesFragment;
    public void setUpFragment() {

        imagesFragment = new ImagesFragment();
        footerFragment = new CustomFooterFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(imagesFragment);
        adapter.addFragment(footerFragment);

        binding.viewpager.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                binding.viewImages.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.viewpager.setCurrentItem(0, true);
                    }
                });

                binding.viewCustomFooter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.viewpager.setCurrentItem(1, true);
                    }
                });
            }
        }).start();


    }

}