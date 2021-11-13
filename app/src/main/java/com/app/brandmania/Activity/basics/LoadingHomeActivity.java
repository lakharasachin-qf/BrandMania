package com.app.brandmania.Activity.basics;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Common.Constant;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;


public class LoadingHomeActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        Activity act = this;
        com.app.brandmania.databinding.ActivityLoadingHomeBinding binding = DataBindingUtil.setContentView(act, R.layout.activity_loading_home);
        binding.logo.setVisibility(View.VISIBLE);
        binding.appVersionTxt.setText("App Version " + Constant.F_VERSION);
        binding.progressBar.setVisibility(View.GONE);
        binding.logo.setVisibility(View.GONE);
        binding.content.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);
        binding.logo.setVisibility(View.VISIBLE);
        binding.content.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.second.setText(Html.fromHtml(getIntent().getStringExtra("msg"), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.second.setText(Html.fromHtml(getIntent().getStringExtra("msg")));
        }
    }
}