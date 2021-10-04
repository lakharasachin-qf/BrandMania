package com.app.brandmania.Activity.about_us;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity {
    Activity act;
    private ActivityAboutUsBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        act=this;
        binding=DataBindingUtil.setContentView(act,R.layout.activity_about_us);

        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().hasExtra("termsNCondition")) {
            binding.webView.loadUrl("http://site.queryfinders.com/brandmania/privacy_policy.html");
            binding.toolbarTitle.setText("Terms & Conditions");
        }else {
            binding.webView.loadUrl("http://site.queryfinders.com/brandmania/index.html");
            binding.toolbarTitle.setText("About us");
        }
        binding.webView.setWebViewClient(new MyWebViewClient(this));


    }
}