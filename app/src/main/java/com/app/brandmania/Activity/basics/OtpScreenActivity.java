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
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Interface.iVerifyOTP;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityOtpScreenBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.GenericTextWatcher;
import com.app.brandmania.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpScreenActivity extends BaseActivity implements alertListenerCallback, iVerifyOTP {
    private Activity act;
    private ActivityOtpScreenBinding binding;
    private String deviceToken = "";
    private String is_completed = "";
    private String toke = "";
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;
    private static final long START_TIME_IN_MILLIS = 30000;
    private boolean isLoading = false;
    String NumberShow;
    String referrerCode = "";
    String firebaseCode = "";


    public String getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task != null && task.isSuccessful() && task.getResult() != null) {
                    deviceToken = task.getResult();
                }
            }
        });

        return deviceToken;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;

        binding = DataBindingUtil.setContentView(act, R.layout.activity_otp_screen);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);
        NumberShow = getIntent().getStringExtra(Constant.MOBILE_NUMBER);
        //sendVerificationCode("+91" + NumberShow);

        binding.otpTxtLayout.setVisibility(View.GONE);
        binding.CouterText.setVisibility(View.VISIBLE);
        binding.otpOne.setText("");
        binding.otpTwo.setText("");
        binding.otpThree.setText("");
        binding.otpFour.setText("");
        binding.otpOne.requestFocus();

        binding.verificationChildTitle.setText("We sent OTP to verify your number \n" + "+91" + NumberShow);
        String Verify = "OTP<br>Verification</font></br>";
        referrerCode = getIntent().getStringExtra("referrerCode");
        Utility.isLiveModeOff(act);

        InsertRecord();
        binding.ResendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.ResendText.setVisibility(View.GONE);
//                binding.CouterText.setVisibility(View.VISIBLE);
                //InsertRecord();
                resendVerificationCode("+91" + NumberShow, resendingToken);

                updateCountDownText();
                if (countDownTimer != null)
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
        deviceToken = getDeviceToken();

        binding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNormalLogin) {
                    if (binding.otpTxt.getText().toString().length() != 0) {
                        verifyVerificationCode(binding.otpTxt.getText().toString());
                    } else {
                        Toast.makeText(act, "Enter otp", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    String OtpString = binding.otpOne.getText().toString() + binding.otpTwo.getText().toString() + binding.otpThree.getText().toString() + binding.otpFour.getText().toString();

                    if (!OtpString.isEmpty()) {

                        VerificationOtp(OtpString, NumberShow);
                    } else {
                        Toast.makeText(act, "Enter otp", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        String Message = "Didn't received OTP? <u><b>RESEND</b></u></font>";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.ResendText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.ResendText.setText(Html.fromHtml(Message));
        }
    }

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    //sending message
    private void sendVerificationCode(String mobile) {
        Utility.showProgress(act);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        try {
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(act, "Error in sending otp", Toast.LENGTH_SHORT).show();
        }

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)// OnVerificationStateChangedCallbacks
                        .build();
        try {
            PhoneAuthProvider.verifyPhoneNumber(options);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(act, "Error in sending otp", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyVerificationCode(String code) {
        Utility.showProgress(act);
        if(code.isEmpty()){
            Utility.error(act,"Otp is empty");
            return;
        }
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }catch (Exception e){
            Utility.dismissProgress();
            e.printStackTrace();
            Utility.error(act,"Entered otp is not valid");
        }
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Utility.dismissProgress();
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            VerificationOtp(responseOTP, NumberShow);
                        } else {
                            String message = "Something is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(act, "Please enter correct OTP", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(act, e -> {
                    Utility.dismissProgress();
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Utility.dismissProgress();

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                firebaseCode = code;
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Utility.dismissProgress();
            e.printStackTrace();
            if (e.getMessage().contains("SafetyNet")) {
                Utility.error(act, "Please fill reCAPTCHA to get OTP, Click on RESEND to get otp again");

            }
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Utility.dismissProgress();
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            resendingToken = forceResendingToken;
            binding.CouterText.setVisibility(View.VISIBLE);
            counter();
            //Log.e("onCodeSent", "s" + mVerificationId);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            Utility.dismissProgress();
            super.onCodeAutoRetrievalTimeOut(s);
           // Log.e("Time", "Out" + s);
        }
    };

    private void VerificationOtp(String otp, String mobileno) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.VERIFY_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                Utility.Log("login-response", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    if (jObject.getBoolean("status")) {
                        //Log.e("otpResponse", "yes");
                        JSONObject jsonArray = jObject.getJSONObject("data");
                        prefManager.setUserName(jsonArray.getString("name"));
                        prefManager.setUserMobileNumber(jsonArray.getString("phone"));
                        prefManager.setUserEmail_Id(jsonArray.getString("email"));
                        prefManager.setUserToken(jsonArray.getString("token"));
                        prefManager.isLoginDate(jsonArray.getString("created_at"));
                        prefManager.setLogin(true);
                        ArrayList<BrandListItem> brands = ResponseHandler.handleLogin(jObject);
                        if (brands != null && brands.size() != 0) {
                            prefManager.setAddBrandList(brands);
                            prefManager.setActiveBrand(brands.get(0));
                        }
                        Intent i = new Intent(act, HomeActivity.class);
                        i.putExtra("FirstLogin", "1");
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Otp Dose Not Match", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            isLoading = false;
            Utility.dismissProgress();
            if (error instanceof TimeoutError) {
                //     Toast.makeText(act, "Time out error", Toast.LENGTH_SHORT).show();
            } else if (error instanceof AuthFailureError) {
                // Toast.makeText(act, "AuthFailureError", Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                // Toast.makeText(act, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            } else if (error instanceof ParseError) {
                //  Toast.makeText(act, "ParseError", Toast.LENGTH_SHORT).show();
            }
            onBackPressed();
            error.printStackTrace();
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("otp", otp);
                hashMap.put("phone", mobileno);
                if (prefManager.getUserToken() != null) {
                    hashMap.put("token", prefManager.getUserToken());
                }
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("User Data", hashMap.toString());
                return hashMap;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    public void TextChanger() {
        EditText[] edit = {binding.otpOne, binding.otpTwo, binding.otpThree, binding.otpFour};
        String OtpString = binding.otpOne.getText().toString() + binding.otpTwo.getText().toString() + binding.otpThree.getText().toString() + binding.otpFour.getText().toString();
        binding.otpOne.addTextChangedListener(new GenericTextWatcher(act, binding.otpOne, edit));
        binding.otpTwo.addTextChangedListener(new GenericTextWatcher(act, binding.otpTwo, edit));
        binding.otpThree.addTextChangedListener(new GenericTextWatcher(act, binding.otpThree, edit));
        binding.otpFour.addTextChangedListener(new GenericTextWatcher(act, binding.otpFour, edit));

    }

    boolean isNormalLogin = false;
    String responseOTP = "";

    private void InsertRecord() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.SEND_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                Utility.Log("OTP", response.toString());
                prefManager.loginStep("2");
                if (ResponseHandler.isSuccess(response, null)) {
                    isNormalLogin = ResponseHandler.getBool(ResponseHandler.createJsonObject(response), "is_normal_login");
                    if (!isNormalLogin) {
                        responseOTP = ResponseHandler.getString(ResponseHandler.createJsonObject(response), "data");
                        binding.otpTxtLayout.setVisibility(View.VISIBLE);
                        binding.CouterText.setVisibility(View.GONE);
                        binding.otpOne.setVisibility(View.GONE);
                        binding.otpTwo.setVisibility(View.GONE);
                        binding.otpThree.setVisibility(View.GONE);
                        binding.otpFour.setVisibility(View.GONE);
                        sendVerificationCode("+91" + NumberShow);
                        return;
                    }

                    binding.otpTxtLayout.setVisibility(View.GONE);
                    binding.CouterText.setVisibility(View.VISIBLE);
                    binding.otpOne.setText("");
                    binding.otpTwo.setText("");
                    binding.otpThree.setText("");
                    binding.otpFour.setText("");
                    binding.otpOne.requestFocus();

                    counter();
                } else {
                    JSONObject responseJson = ResponseHandler.createJsonObject(response);
                    Utility.showAlert(act, ResponseHandler.getString(responseJson, "message"));
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
                params.put("phone", NumberShow);
                params.put("deviceInfo", HELPER.deviceINFO());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                binding.ResendText.setVisibility(View.VISIBLE);
                binding.CouterText.setVisibility(View.GONE);
            }
        }.start();

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
        Intent intent = new Intent(act, LoginActivity.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        finish();
    }

    @Override
    public void onVerification() {
        String OtpString = binding.otpOne.getText().toString() + binding.otpTwo.getText().toString() + binding.otpThree.getText().toString() + binding.otpFour.getText().toString();
        //VerificationOtp(OtpString.trim(), NumberShow);
        if (!isNormalLogin) {
            verifyVerificationCode(binding.otpTxt.getText().toString());
        } else {
            VerificationOtp(OtpString, NumberShow);
        }
    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}