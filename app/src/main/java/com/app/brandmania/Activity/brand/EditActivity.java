package com.app.brandmania.Activity.brand;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditBinding;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;

public class EditActivity extends BaseActivity {

    Activity act;
    private ActivityEditBinding binding;
    boolean isError = false;
    boolean isFocus = false;
    PreafManager preafManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_edit);
        preafManager = new PreafManager(this);

        binding.BackButton.setOnClickListener(v -> onBackPressed());
        Utility.isLiveModeOff(act);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);

        if (prefManager.getActiveBrand() != null) {
            binding.editProfile.setVisibility(View.VISIBLE);
            binding.content.setVisibility(View.GONE);
            binding.editBrandBtn.setOnClickListener(v -> Validation());
        } else {
            binding.editProfile.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);
            animateButton();
            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce);
        double animationDuration = 4 * 1000;
        myAnim.setRepeatCount(Animation.INFINITE);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);
        myAnim.setInterpolator(interpolator);
        binding.continueBtn.startAnimation(myAnim);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }

    private void Validation() {

        if (binding.nameTxt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.nameTxtLayout.setError(getString(R.string.brandname_text));
            binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

            if (!isFocus) {
                binding.nameTxt.requestFocus();
                isFocus = true;
            }
        }
        if (!binding.phoneTxt.getText().toString().trim().equals("")) {
            if (binding.phoneTxt.getText().toString().trim().length() < 10) {
                binding.phoneTxtLayout.setError(getString(R.string.validphoneno_txt));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                isError = true;
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                }
                return;
            }

        } else {
            if (binding.phoneTxt.getText().toString().trim().equals("")) {
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                }
                return;
            }

        }


    }
}