package com.app.brandmania.Activity.about_us;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.app.brandmania.R;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.databinding.ActivityHelpAndSupportBinding;
import com.app.brandmania.utils.Utility;


public class HelpAndSupport extends AppCompatActivity {
    Activity act;
    private ActivityHelpAndSupportBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_help_and_support);
        Utility.isLiveModeOff(act);
        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        binding.faqactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FaqActivity.class);
                startActivity(i);

            }
        });
        binding.reportAndBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddReportAndBug.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

}