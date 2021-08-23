package com.app.brandmania.Activity.basics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SpleshActivity extends BaseActivity implements alertListenerCallback {
    Activity act;
    private ActivityMainBinding binding;
    PreafManager preafManager;
    AnimatorSet animatorSet1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager=new PreafManager(this);

        binding.logo.setVisibility(View.VISIBLE);
        final ObjectAnimator scaleAnimatiorXX = ObjectAnimator.ofFloat(binding.logo, "scaleX", 0, 1f);
        ObjectAnimator scaleAnimatiorYX = ObjectAnimator.ofFloat(binding.logo, "scaleY", 0, 1f);
        animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(scaleAnimatiorXX, scaleAnimatiorYX);
        animatorSet1.setDuration(3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (preafManager.getUserToken()!=null && !preafManager.getUserToken().isEmpty() ) {
                    LoginFlow();
                }
                else {
                    Intent intent = new Intent(act, LoginActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }

            }
        }, 1000);

    }
    private void LoginFlow() {
        Utility.Log("API : ", APIs.IS_COMPLETE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.IS_COMPLETE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("IS_COMPLETE : ", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if (ResponseHandler.getBool(jsonObject,"status")) {
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
                    }else {
                        Utility.showAlert(act,ResponseHandler.getString(jsonObject,"message"));
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
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
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

        preafManager=new PreafManager(act);
        if (preafManager.getIs_Registration())
        {
            if (preafManager.getIS_Brand())
            {
                Intent i = new Intent(act, HomeActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            }
            else
            {
                Intent i = new Intent(act, AddBranddActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                finish();
            }
        }
        else
        {
            Intent intent = new Intent(act, RegistrationActivity.class);
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