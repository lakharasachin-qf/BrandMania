package com.app.brandmania.Activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Utils.GenericTextWatcher;
import com.app.brandmania.R;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityOtpScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OtpScreenActivity extends BaseActivity {
    Activity act;
    PreafManager preafManager;
    private ActivityOtpScreenBinding binding;
    private String deviceToken = "";
    private String is_completed="";
    private String toke="";
    private boolean mTimerRunnig;
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 30000;
    private boolean isLoading = false;
    String NumberShow;
    public String getDeviceToken(Activity act) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        deviceToken = instanceIdResult.getToken();

                    }
                });
        return deviceToken;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp_screen);
        NumberShow = getIntent().getStringExtra(Constant.MOBILE_NUMBER);
        binding.verificationChildTitle.setText("We sent OTP to verify your number \n" + "+91" + NumberShow);
        String Verify = "OTP<br>Verification</font></br>";
        InsertRecord();

        binding.ResendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ResendText.setVisibility(View.GONE);
                binding.CouterText.setVisibility(View.VISIBLE);
                InsertRecord();
                updateCountDownText();
                countDownTimer.start();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            binding.verifyOtp.setText(Html.fromHtml(Verify, Html.FROM_HTML_MODE_COMPACT));
        } else {

            binding.verifyOtp.setText(Html.fromHtml(Verify));
        }
        TextChanger();
        deviceToken = getDeviceToken(this);
        preafManager=new PreafManager(this);
        binding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OtpString= binding.otpOne.getText().toString()+binding.otpTwo.getText().toString()+binding.otpThree.getText().toString()+binding.otpFour.getText().toString();
                VerificationOtp(OtpString.trim(), NumberShow);
            }
        });
        String Message = "Didn't received OTP? <u><b>RESEND</b></u></font>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.ResendText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.ResendText.setText(Html.fromHtml(Message));
        }
    }
    private void VerificationOtp(String otp, String mobileno) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Utility.Log("Verify-Responce-Api", APIs.VERIFY_OTP);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.VERIFY_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                Utility.Log("Verify-Response", response);

                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getBoolean("status")) {
                        JSONObject jsonArray = jObject.getJSONObject("data");
                        preafManager.setUserToken(jsonArray.getString("token"));
                        Log.w("Tpokennnn",jsonArray.getString("token"));
                        is_completed= jsonArray.getString("is_completed");
                        preafManager.loginStep(is_completed);

                        if (is_completed.equals("0"))
                        {
                            Intent i = new Intent(act, RegistrationActivity.class);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            finish();
                        }
                        if (is_completed.equals("1"))
                        {
                            Intent i = new Intent(act, AddBranddActivity.class);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            finish();
                        }
                        if (is_completed.equals("2"))
                        {
                            getBrandList();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Otp Dose Not Match", Toast.LENGTH_LONG).show();

                    }
                }
                    catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false;
                Utility.dismissProgress();
                onBackPressed();
                error.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Utility.Log("Verify-Header", getHeader(CodeReUse.GET_FORM_HEADER).toString());
                return getHeader(CodeReUse.GET_FORM_HEADER);

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("otp", otp);
                hashMap.put("phone", mobileno);
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("Verify-Param", hashMap.toString());
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("addbrandresponce",response);
                ArrayList<BrandListItem> brandListItems=new ArrayList<>();
                try {
                    JSONObject res=new JSONObject(response);
                    JSONArray jsonArray1 = res.getJSONArray("data");
                    for (int i=0;i<jsonArray1.length();i++)
                    {
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        BrandListItem brandListItemm=new BrandListItem();
                        brandListItemm.setId(ResponseHandler.getString(jsonObject,"id"));
                        brandListItemm.setCategoryId(ResponseHandler.getString(jsonObject, "br_category_id"));
                        brandListItemm.setCategoryName(ResponseHandler.getString(jsonObject, "br_category_name"));
                        brandListItemm.setName(ResponseHandler.getString(jsonObject,"br_name"));
                        brandListItemm.setWebsite(ResponseHandler.getString(jsonObject,"br_website"));
                        brandListItemm.setEmail(ResponseHandler.getString(jsonObject,"br_email"));
                        brandListItemm.setAddress(ResponseHandler.getString(jsonObject,"br_address"));
                        brandListItemm.setLogo(ResponseHandler.getString(jsonObject,"br_logo"));
                        JSONArray jsonArray = jsonObject.getJSONArray("br_frame");
                        ArrayList<FrameItem>frameItems=null;
                        if (!jsonArray.isNull(0) && jsonArray.length() != 0) {
                            frameItems = new ArrayList<>();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                FrameItem frameItem = new FrameItem();
                                frameItem.setFrame1((ResponseHandler.getString(jsonObject,"fream_base_url"))+"/"+ResponseHandler.getString(jsonObject1, "frame_path"));
                                frameItem.setFrameId(ResponseHandler.getString(jsonObject1, "id"));

                                Gson gson=new Gson();
                               Log.e("Frameeeee",gson.toJson(frameItem));
                                frameItems.add(frameItem);
                            }
                        }
                        brandListItemm.setFrame(frameItems);
                        brandListItems.add(brandListItemm);
                    }


                    preafManager.setAddBrandList(brandListItems);
                    preafManager.setIS_Brand(true);

                    if (brandListItems!=null && brandListItems.size()!=0){
                        preafManager.setActiveBrand(brandListItems.get(0));
                    }


                    Intent i = new Intent(act, HomeActivity.class);
                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    public void TextChanger() {
        EditText[] edit = {binding.otpOne, binding.otpTwo, binding.otpThree, binding.otpFour};

        binding.otpOne.addTextChangedListener(new GenericTextWatcher(binding.otpOne, edit));
        binding.otpTwo.addTextChangedListener(new GenericTextWatcher( binding.otpTwo, edit));
        binding.otpThree.addTextChangedListener(new GenericTextWatcher(binding.otpThree, edit));
        binding.otpFour.addTextChangedListener(new GenericTextWatcher(binding.otpFour, edit));

    }
    private void InsertRecord() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                isLoading = false;
                Utility.dismissProgress();
                Utility.Log("Response: ", response);
              //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
              //  preafManager.setMobileNumber(binding.mobileNumber.getText().toString());
                preafManager.loginStep("2");
                if (ResponseHandler.isSuccess(response, null)) {
                    binding.CouterText.setVisibility(View.VISIBLE);
                    counter();
                } else {
                    JSONObject responseJson = ResponseHandler.createJsonObject(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                Utility.dismissProgress();
                volleyError.printStackTrace();
                try {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    Log.e("REsepinERr ", responseBody);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone",NumberShow);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);

    }
    private void counter() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(mTimeLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

                mTimerRunnig = false;
                binding.ResendText.setVisibility(View.VISIBLE);
                binding.CouterText.setVisibility(View.GONE);
            }
        }.start();

        mTimerRunnig = true;
        binding.ResendText.setVisibility(View.GONE);
    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMills / 1000) / 60;
        int second = (int) (mTimeLeftInMills / 1000) % 60;
        String timeLeftFormeted = String.format(Locale.getDefault(), "%02d:%02d", minutes, second);
        binding.CouterText.setText(timeLeftFormeted);
    }
}