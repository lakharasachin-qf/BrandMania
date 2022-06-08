package com.app.brandmania.Activity.brand;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityEditBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        hideKeyboard(binding.rootLayout);
        preafManager = new PreafManager(this);
        binding.BackButton.setOnClickListener(v -> onBackPressed());
        Utility.isLiveModeOff(act);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);

        if (preafManager.getUserEmail_Id() != null && !preafManager.getUserEmail_Id().isEmpty()) {
            binding.emailIdEdt.setText(preafManager.getUserEmail_Id());
        }

        if (preafManager.getMobileNumber() != null && !preafManager.getMobileNumber().isEmpty()) {
            binding.phoneTxt.setText(preafManager.getMobileNumber());
        }
        if (preafManager.getUserName() != null && !preafManager.getUserName().isEmpty()) {
            binding.nameTxt.setText(preafManager.getUserName());
        }
        binding.editProfile.setVisibility(View.VISIBLE);
        binding.content.setVisibility(View.GONE);
        binding.editBrandBtn.setOnClickListener(v -> {

            Validation();
        });

        if (binding.phoneTxt.getText().toString().isEmpty()) {
            binding.phoneTxt.setEnabled(true);
        } else {
            binding.phoneTxt.setEnabled(false);
        }

//        if (prefManager.getActiveBrand() != null) {
//            binding.editProfile.setVisibility(View.VISIBLE);
//            binding.content.setVisibility(View.GONE);
//            binding.editBrandBtn.setOnClickListener(v -> {
//                Validation();
//            });
//        } else {
//            binding.editProfile.setVisibility(View.GONE);
//            binding.content.setVisibility(View.VISIBLE);
//            animateButton();
//            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
//                }
//            });
//        }
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

            binding.nameTxtLayout.setError(getString(R.string.enter_name));
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
        if (!binding.emailIdEdt.getText().toString().equals("")) {
            if (!CodeReUse.isEmailValid(binding.emailIdEdt.getText().toString().trim())) {
                isError = true;
                binding.emailIdEdtLayout.setError(getString(R.string.enter_valid_email_address));
                binding.emailIdEdt.requestFocus();
                return;
            }

        } else {
            if (binding.emailIdEdt.getText().toString().trim().length() == 0) {
                isError = true;
                binding.emailIdEdtLayout.setError(getString(R.string.enter_email_id));
                binding.emailIdEdt.requestFocus();
            }
            return;
        }
        updateProfile();
    }

    private void updateProfile() {
        binding.progressCircular.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.EDIT_BRAND_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Edit-RESPONSE", response);
                binding.progressCircular.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    Toast.makeText(act, "Profile Updated", Toast.LENGTH_SHORT).show();
                    preafManager.setUserName(Objects.requireNonNull(binding.nameTxt.getText()).toString());
                    preafManager.setUserEmail_Id(Objects.requireNonNull(binding.emailIdEdt.getText()).toString());
                    preafManager.setUserMobileNumber(Objects.requireNonNull(binding.phoneTxt.getText()).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                error -> error.printStackTrace()
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return MakeMyBrandApp.getInstance().getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String[] array = binding.nameTxt.getText().toString().split(" ");
                params.put("first_name", array[0]);
                params.put("last_name", array.length > 1 ? array[1] : "");
                params.put("phone", binding.phoneTxt.getText().toString());
                params.put("email", binding.emailIdEdt.getText().toString());
                Log.e("Edit-PARAM", params.toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
}