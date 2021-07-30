package com.app.brandadmin.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.app.brandadmin.Activity.admin.ViewNewCategory;
import com.app.brandadmin.Activity.admin.ViewUserListActivity;
import com.app.brandadmin.Common.HELPER;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.R;
import com.app.brandadmin.databinding.ActivityHomeBinding;

public class HomeActivity extends BaseActivity  {
    private Activity act;
    private ActivityHomeBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding =DataBindingUtil.setContentView(act,R.layout.activity_home);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        binding.categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ROUTE(act, ViewNewCategory.class);
            }
        });

        binding.sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ROUTE(act, ViewUserListActivity.class);
            }
        });
    }

}