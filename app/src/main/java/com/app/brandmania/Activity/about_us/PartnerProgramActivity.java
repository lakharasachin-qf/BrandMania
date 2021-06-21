package com.app.brandmania.Activity.about_us;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.databinding.ActivityPartnerProgramBinding;

public class PartnerProgramActivity extends BaseActivity {
    Activity act;
    private ActivityPartnerProgramBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_partner_program);
        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
    }
    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}