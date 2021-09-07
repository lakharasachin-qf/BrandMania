package com.app.brandmania.Activity.basics;

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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Interface.iVerifyOTP;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.GenericTextWatcher;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityOtpScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OtpScreenActivity extends BaseActivity implements alertListenerCallback, iVerifyOTP {
    Activity act;
    PreafManager preafManager;
    private ActivityOtpScreenBinding binding;
    private String deviceToken = "";
    private String is_completed = "";
    private String toke = "";
    private boolean mTimerRunnig;
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 30000;
    private boolean isLoading = false;
    String NumberShow;
    String referrerCode = "";
    public String getDeviceToken(Activity act) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                deviceToken = task.getResult();
            }
        });
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
//                    @Override
//                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                        deviceToken = instanceIdResult.getToken();
//
//                    }
//                });
        return deviceToken;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp_screen);
        preafManager = new PreafManager(act);
        NumberShow = getIntent().getStringExtra(Constant.MOBILE_NUMBER);
        binding.verificationChildTitle.setText("We sent OTP to verify your number \n" + "+91" + NumberShow);
        String Verify = "OTP<br>Verification</font></br>";
        referrerCode = getIntent().getStringExtra("referrerCode");
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
            binding.changeContact.setText(Html.fromHtml("<u>Change</u>", Html.FROM_HTML_MODE_COMPACT));
            binding.verifyOtp.setText(Html.fromHtml(Verify, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.changeContact.setText(Html.fromHtml("<u>Change</u>"));
            binding.verifyOtp.setText(Html.fromHtml(Verify));
        }

        binding.changeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextChanger();
        deviceToken = getDeviceToken(this);
        preafManager = new PreafManager(this);
        binding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OtpString = binding.otpOne.getText().toString() + binding.otpTwo.getText().toString() + binding.otpThree.getText().toString() + binding.otpFour.getText().toString();
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
                Utility.Log("VERIFY_OTP", response);
                ArrayList<BrandListItem> brandListItems=new ArrayList<>();
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
                            i.putExtra("referrerCode",referrerCode);
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
                            JSONArray jsonArray1 = jsonArray.getJSONArray("brands");
                            for (int i=0;i<jsonArray1.length();i++)
                            {
                                JSONObject jsonObject=jsonArray1.getJSONObject(i);
                                BrandListItem brandListItemm=new BrandListItem();
                                brandListItemm.setId(ResponseHandler.getString(jsonObject,"id"));
                                brandListItemm.setName(ResponseHandler.getString(jsonObject,"br_name"));
                                brandListItemm.setWebsite(ResponseHandler.getString(jsonObject,"br_website"));
                                brandListItemm.setEmail(ResponseHandler.getString(jsonObject,"br_email"));
                                brandListItemm.setAddress(ResponseHandler.getString(jsonObject,"br_address"));
                                brandListItemm.setPhonenumber(ResponseHandler.getString(jsonObject,"br_phone"));
                                brandListItemm.setLogo(ResponseHandler.getString(jsonObject,"br_logo"));
                                brandListItems.add(brandListItemm);
                            }

                            preafManager.setAddBrandList(brandListItems);
                            preafManager.setIS_Brand(true);

                            if (brandListItems!=null && brandListItems.size()!=0){
                                preafManager.setActiveBrand(brandListItems.get(0));
                            }


                            Intent i = new Intent(act, HomeActivity.class);
                            i.putExtra("FirstLogin","1");
                            i.addCategory(Intent.CATEGORY_HOME);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            finish();
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
                if (error instanceof TimeoutError) {
                    Toast.makeText(act, "Time out error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(act, "AuthFailureError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(act, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(act, "ParseError", Toast.LENGTH_SHORT).show();
                }
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
             /*   hashMap.put("referrerCode", referrerCode);*/
                Log.e("",preafManager.getReferCode());
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("Verify-Param", hashMap.toString());

                return hashMap;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    public void TextChanger() {
        EditText[] edit = {binding.otpOne, binding.otpTwo, binding.otpThree, binding.otpFour};
        String OtpString = binding.otpOne.getText().toString() + binding.otpTwo.getText().toString() + binding.otpThree.getText().toString() + binding.otpFour.getText().toString();

        binding.otpOne.addTextChangedListener(new GenericTextWatcher(act,binding.otpOne, edit));
        binding.otpTwo.addTextChangedListener(new GenericTextWatcher(act,binding.otpTwo, edit));
        binding.otpThree.addTextChangedListener(new GenericTextWatcher(act,binding.otpThree, edit));
        binding.otpFour.addTextChangedListener(new GenericTextWatcher(act,binding.otpFour, edit));

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

                preafManager.loginStep("2");
                if (ResponseHandler.isSuccess(response, null)) {
                    binding.CouterText.setVisibility(View.VISIBLE);
                    binding.otpOne.setText("");
                    binding.otpTwo.setText("");
                    binding.otpThree.setText("");
                    binding.otpFour.setText("");
                    binding.otpOne.requestFocus();
                    counter();
                } else {
                    JSONObject responseJson = ResponseHandler.createJsonObject(response);
                    Utility.showAlert(act,ResponseHandler.getString(responseJson,"message"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                Utility.dismissProgress();
                volleyError.printStackTrace();
//                try {
//                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
//                    Log.e("REsepinERr ", responseBody);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }

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

    @Override
    public void alertListenerClick() {
        Intent intent=new Intent(act, LoginActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        finish();
    }

    @Override
    public void onVerification() {
        String OtpString= binding.otpOne.getText().toString()+binding.otpTwo.getText().toString()+binding.otpThree.getText().toString()+binding.otpFour.getText().toString();
        VerificationOtp(OtpString.trim(), NumberShow);
    }
    @Override
    public void onBackPressed() {CodeReUse.activityBackPress(act);
    }
}