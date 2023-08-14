package com.make.mybrand.Activity.basics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityAddBrandMultipleBinding;
import com.make.mybrand.databinding.ActivityForgotPasswordBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends BaseActivity {

    Activity act;
    private ActivityForgotPasswordBinding binding;
    private AlertDialog.Builder alertDialogBuilder;
    PreafManager preafManager;
    private boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_forgot_password);
        preafManager = new PreafManager(this);
        alertDialogBuilder = new AlertDialog.Builder(act);
        CodeReUse.RemoveError(binding.phoneTxt, binding.mobileEdtLayout);
        binding.submitBtn.setOnClickListener(view -> validationMobile());
        binding.BackButtonMember.setOnClickListener(view -> onBackPressed());
    }

    public  void validationMobile(){

        String mobile = binding.phoneTxt.getText().toString().trim();
        if(mobile.isEmpty()){
            binding.mobileEdtLayout.setError("Enter Mobile Number");
            binding.mobileEdtLayout.requestFocus();
            return;
        }
        if(mobile.length() < 10){
            binding.mobileEdtLayout.setError("Enter Valid Mobile Number");
            binding.mobileEdtLayout.requestFocus();
            return;
        }
        apiForCategoryRequest();
    }

    private void apiForCategoryRequest() {
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.changePassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                JSONObject jObject= null;
                try {
                    jObject = new JSONObject(response);
                    JSONObject responseData = ResponseHandler.createJsonObject(response);
                    if (ResponseHandler.getBool(responseData, "status")) {
                        alertDialogBuilder.setMessage("Your request has been received, Admin will inform you by resetting password,kindly update your password once you login with new password.");
                        alertDialogBuilder.setPositiveButton("Ok", (arg0, arg1) -> onBackPressed());
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }else{
                        alertDialogBuilder.setMessage(ResponseHandler.getString(responseData, "message"));
                        alertDialogBuilder.setPositiveButton("Ok", (arg0, arg1) -> onBackPressed());
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                Utility.dismissProgress();
                volleyError.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("phone", binding.phoneTxt.getText().toString().trim());
                hashMap.put("cat_name", "Change password request");
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }


    private void updatePassword(){
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.changePassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                JSONObject jObject= null;
                try {
                    jObject = new JSONObject(response);
                    alertDialogBuilder.setMessage("Your request has been received, Admin will inform you by resetting password,kindly update your password once you login with new password.");
                    alertDialogBuilder.setPositiveButton("Ok", (arg0, arg1) -> onBackPressed());
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    HELPER.print("RESPONSE", response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                Utility.dismissProgress();
                volleyError.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", binding.phoneTxt.getText().toString().trim());
                HELPER.print("params",params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

}