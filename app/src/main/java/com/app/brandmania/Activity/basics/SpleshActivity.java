package com.app.brandmania.Activity.basics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityMainBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SpleshActivity extends BaseActivity implements alertListenerCallback {
    Activity act;
    private ActivityMainBinding binding;
    PreafManager preafManager;
    AnimatorSet animatorSet1;
    private String referrerCode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;

        binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        preafManager = new PreafManager(act);
        binding.logo.setVisibility(View.VISIBLE);
        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        getInvitation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              //  Log.e("UserToken", gson.toJson(preafManager.getUserToken()));
                    if (preafManager.getUserToken() != null && !preafManager.getUserToken().isEmpty()) {
                        LoginFlow();
                    } else {
                        Intent intent = new Intent(act, LoginActivity.class);
                        intent.putExtra("referrerCode", referrerCode);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        finish();
                    }
//                 throw new RuntimeException("Test Crash");


            }
        }, 1000);

    }

    public void getInvitation() {
        // [START ddl_get_invitation]
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.e("My Refer Link", deepLink.toString());
                            String referLink = deepLink.toString();
                            try {
                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                                Log.e("First", "subString = " + referLink);
                                referrerCode = referLink.substring(referLink.lastIndexOf("=") + 1);
                                Log.e("referrerid", referrerCode);
                                preafManager.setSpleshReferrer(referrerCode);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "getDynamicLink:onFailure", e);
                    }
                });

    }

    private void LoginFlow() {
        Utility.Log("API : ", APIs.IS_COMPLETE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.IS_COMPLETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("IS_COMPLETE : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (ResponseHandler.getBool(jsonObject, "status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getString("is_completed").equals("0")) {
                            preafManager.setIs_Registration(false);
                            sessionCreat();
                        }
                        if (jsonObject1.getString("is_completed").equals("1")) {
                            preafManager.setIS_Brand(false);
                            sessionCreat();
                        }
                        if (jsonObject1.getString("is_completed").equals("2")) {
                            preafManager.setIs_Registration(true);
                            preafManager.setIS_Brand(true);

                            Intent i = new Intent(act, HomeActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                            finish();
                        }
                    } else {
                        Utility.showAlert(act, ResponseHandler.getString(jsonObject, "message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        String body;
                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("X-Authorization", "Bearer " + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.e("DateNdClass", params.toString());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private void sessionCreat() {

        preafManager = new PreafManager(act);
        if (preafManager.getIs_Registration()) {
            if (preafManager.getIS_Brand()) {
                Intent i = new Intent(act, HomeActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            } else {
                Intent i = new Intent(act, AddBranddActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            }

        } else {
            Intent intent = new Intent(act, RegistrationActivity.class);
            intent.putExtra("referrerCode", referrerCode);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            finish();
        }

    }


    @Override
    public void alertListenerClick() {
        preafManager.Logout();

        Intent intent = new Intent(act, LoginActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        finish();

    }
}