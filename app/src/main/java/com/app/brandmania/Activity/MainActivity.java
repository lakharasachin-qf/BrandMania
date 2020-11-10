package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    Activity act;
    private ActivityMainBinding binding;
    PreafManager preafManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
        setTheme(R.style.AppTheme_Second);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager=new PreafManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preafManager.getUserToken()!=null && !preafManager.getUserToken().isEmpty()) {
                    Intent intent = new Intent(act, HomeActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(act, LoginActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }

            }
        }, 4000);

    }
}