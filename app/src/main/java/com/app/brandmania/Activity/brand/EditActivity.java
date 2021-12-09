package com.app.brandmania.Activity.brand;

import androidx.databinding.DataBindingUtil;
import androidx.preference.Preference;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditBinding;
import com.app.brandmania.utils.CodeReUse;
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

        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        //myAnim.setDuration((long)animationDuration);
        myAnim.setRepeatCount(Animation.INFINITE);

        // Use custom animation interpolator to achieve the bounce effect
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.continueBtn.startAnimation(myAnim);


        // Run button animation again after it finished
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

//        if (!binding.emailIdEdt.getText().toString().trim().equals("")) {
//            if (!CodeReUse.isEmailValid(binding.emailIdEdt.getText().toString().trim())) {
//                isError = true;
//
//                binding.emailIdEdtLayout.setError(getString(R.string.enter_valid_email_address));
//                binding.emailIdEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
//
//                if (!isFocus) {
//                    binding.emailIdEdt.requestFocus();
//                    isFocus = true;
//                }
//            }
//        } else {
//            if (binding.emailIdEdt.getText().toString().trim().equals("")) {
//                binding.emailIdEdtLayout.setError(getString(R.string.enter_email_id));
//                binding.emailIdEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
//                if (!isFocus) {
//                    binding.emailIdEdt.requestFocus();
//                    isFocus = true;
//                }
//                return;
//            }
//
//        }


    }
}