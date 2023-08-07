package com.make.mybrand.Activity.basics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Activity.HomeActivity;
import com.make.mybrand.Activity.brand.AddBranddActivity;
import com.make.mybrand.Common.Constant;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Interface.alertListenerCallback;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityMainBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.Utility;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        try {
            int versionCode = act.getPackageManager().getPackageInfo(act.getPackageName(), 0).versionCode;
            if (versionCode > preafManager.getPreviousCode()) {
                preafManager.Logout();
                Utility.deleteCache(act);
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.logo.setVisibility(View.VISIBLE);
        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        getInvitation();
        if (!preafManager.getLoginDate().isEmpty()) {
            HELPER.IsTwoDateComparison(preafManager.getLoginDate(), act, preafManager.getDaysCounter());
        }
        new Handler().postDelayed(() -> {
            if (preafManager.isLogin()) {
                Intent intent = new Intent(act, HomeActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            } else {
                Intent intent = new Intent(act, LoginActivity.class);
                intent.putExtra("referrerCode", referrerCode);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            }

        }, 1000);

    }

    private void LoginFlow() {
        Utility.Log("API : ", APIs.IS_COMPLETE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.IS_COMPLETE, new Response.Listener<String>() {
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

                            /*    "error_msg": [
            {
                "application_version": "1.2.0,1.2.1",
                "message": "<p>Test Messagge new</p>"
            },
            {
                "application_version": "appl verison",
                "message": "<p><b>ffrfefr</b></p>"
            }
        ]*/
                            if (jsonObject1.has("error_msg")) {
                                boolean appError = false;
                                String msg = "";
                                JSONArray versionDataArray = jsonObject1.getJSONArray("error_msg");
                                if (versionDataArray.length() != 0) {
                                    for (int i = 0; i < versionDataArray.length(); i++) {
                                        int apiVERSION = Integer.parseInt(versionDataArray.getJSONObject(i).getString("application_version").replace(".", ""));
                                        int currentVERSION = Integer.parseInt(String.valueOf(Constant.F_VERSION).replace(".", ""));

                                        if (apiVERSION == currentVERSION) {
                                            appError = true;
                                            msg = versionDataArray.getJSONObject(i).getString("message");
                                            break;
                                        }
                                    }
                                }
                                Intent i;
                                if (appError) {
                                    i = new Intent(act, LoadingHomeActivity.class);
                                    i.putExtra("msg", msg);
                                } else {
                                    i = new Intent(act, HomeActivity.class);
                                }
                                startActivity(i);
                                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                finish();

                            } else {
                                Intent i = new Intent(act, HomeActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                finish();
                            }
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
                        error.printStackTrace();
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
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("deviceInfo", HELPER.deviceINFO());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
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
                            String referLink = deepLink.toString();
                            try {
                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                                referrerCode = referLink.substring(referLink.lastIndexOf("=") + 1);
                                preafManager.setSpleshReferrer(referrerCode);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                })
                .addOnFailureListener(this, e -> {
                });

    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(s.parse(date).getTime()));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
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