package com.app.brandmania.Activity.basics;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityLoginBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    Activity act;
    private ActivityLoginBinding binding;
    private boolean isLoading = false;
    private ProgressDialog pDialog;
    PreafManager preafManager;
    String ContactNO;
    String referrerCode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        preafManager = new PreafManager(act);
        referrerCode = getIntent().getStringExtra("referrerCode");
        String WELCOME = "Welcome<br>Back!</font></br>";
        String Message = "Don't have account?<font color='#ad2753'><b><u>SignUp</u></b></font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.signupText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            binding.welcome.setText(Html.fromHtml(WELCOME, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.signupText.setText(Html.fromHtml(Message));
            binding.welcome.setText(Html.fromHtml(WELCOME));
        }
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactNO = binding.mobileNumber.getText().toString();
                if (!binding.mobileNumber.getText().toString().equals("")) {
                    if (ContactNO.length() < 10) {
                        binding.mobileNumber.setError("Enter Valid Mobile Number");
                        binding.mobileNumber.requestFocus();
                        return;
                    } else {
                        Intent intent = new Intent(act, OtpScreenActivity.class);
                        intent.putExtra(Constant.MOBILE_NUMBER, binding.mobileNumber.getText().toString());
                        intent.putExtra("referrerCode", referrerCode);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        preafManager.setMobileNumber(binding.mobileNumber.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                    }
                } else {
                    String ContactNO = binding.mobileNumber.getText().toString();
                    if (ContactNO.isEmpty()) {
                        binding.mobileNumber.setError("Enter Mobile Number");
                        binding.mobileNumber.requestFocus();
                        return;
                    }
                }
            }
        });
    }

}