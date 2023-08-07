package com.make.mybrand.Activity.about_us;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.R;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.databinding.ActivityPartnerProgramBinding;
import com.make.mybrand.utils.Utility;

public class PartnerProgramActivity extends BaseActivity {
    Activity act;
    private ActivityPartnerProgramBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_partner_program);
        Utility.isLiveModeOff(act);
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