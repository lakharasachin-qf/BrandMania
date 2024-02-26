package com.make.mybrand.Activity.basics;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.make.mybrand.Activity.HomeActivity;
import com.make.mybrand.Activity.brand.AddBrandMultipleActivity;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Fragment.AskPasswordFragment;
import com.make.mybrand.Fragment.DeleteAccountFragment;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.R;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.make.mybrand.databinding.ActivityLoginBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends BaseActivity {

    Activity act;
    private ActivityLoginBinding binding;
    private boolean isLoading = false;
    private ProgressDialog pDialog;
    PreafManager preafManager;
    String ContactNO;
    String referrerCode = "";

    boolean isNoPassword = false;
    boolean isValided = false;

    boolean isDirectLogin = false;
    private String deviceToken = "";

    private ActivityResultLauncher<String> requestPermissionLauncher;
    String passwordData = "";

    public String getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    deviceToken = task.getResult();
                }
            }
        });

        return deviceToken;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        getDeviceToken();
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        preafManager = new PreafManager(act);
        Utility.isLiveModeOff(act);
        referrerCode = getIntent().getStringExtra("referrerCode");
        alertDialogBuilder = new AlertDialog.Builder(act);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task != null && task.isSuccessful() && task.getResult() != null) {
                    Log.e("TOKEN", task.getResult().toString());
                }
            }
        });

        String WELCOME = "Welcome<br>Back!</font></br>";
        String Message = "Don't have account?<font color='#ad2753'><b><u>SignUp</u></b></font>";
        setNotificationHandle();
        setNotificationPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.signupText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            binding.welcome.setText(Html.fromHtml(WELCOME, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.signupText.setText(Html.fromHtml(Message));
            binding.welcome.setText(Html.fromHtml(WELCOME));
        }

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HELPER.ROUTE(act, ForgotPasswordActivity.class);
            }
        });

        binding.mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (binding.mobileNumber.getText().toString().length() > 9) {
                    if (!isDirectLogin) {
                        checkForPassword();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
/*endpoint: loginWithPassword
param:
1. phone
2. passeword
3. deviceInfo
4. firebase_token*/


        /*endpoint: updatePassword
param:
1. phone
2. passeword*/

        binding.loginBtn.setOnClickListener(v -> {
            if (isDirectLogin) {
                String ContactNO = Objects.requireNonNull(binding.mobileNumber.getText()).toString();
                if (ContactNO.isEmpty()) {
                    binding.mobileNumber.setError("Enter Mobile Number");
                    binding.mobileNumber.requestFocus();
                } else if (ContactNO.length() < 10) {
                    binding.mobileNumber.setError("Enter Valid Mobile Number");
                    binding.mobileNumber.requestFocus();
                } else {
                    getDirectLogin();
                }
            } else {
                if (isValided) {
                    String ContactNO = Objects.requireNonNull(binding.mobileNumber.getText()).toString();
                    String password = Objects.requireNonNull(binding.passwordNumber.getText()).toString();
                    HELPER.print("isNoPassword", String.valueOf(isNoPassword));
                    if (isNoPassword) {
                        if (ContactNO.isEmpty()) {
                            binding.mobileNumber.setError("Enter Mobile Number");
                            binding.mobileNumber.requestFocus();
                        } else if (ContactNO.length() < 10) {
                            binding.mobileNumber.setError("Enter Valid Mobile Number");
                            binding.mobileNumber.requestFocus();
                        } else {
                            callsigninapicall();
                        }
                    } else {
                        if (ContactNO.isEmpty()) {
                            binding.mobileNumber.setError("Enter Mobile Number");
                            binding.mobileNumber.requestFocus();
                        } else if (ContactNO.length() < 10) {
                            binding.mobileNumber.setError("Enter Valid Mobile Number");
                            binding.mobileNumber.requestFocus();
                        } else if (password.isEmpty()) {
                            binding.passwordNumber.setError("Enter password");
                            binding.passwordNumber.requestFocus();
                        } else if (password.length() < 6) {
                            binding.passwordNumber.setError("Password should be at least 6 digit long");
                            binding.passwordNumber.requestFocus();
                        } else {
                            passwordData = binding.passwordNumber.getText().toString();
                            callsigninapicall();
                        }
                    }
                } else {
                    checkForPassword();
                }
            }
        });
    }

    private void getDirectLogin() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.LOGIN_WITHOUT_VALIDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                HELPER.print("RESPONSE", response);
                try {
                    JSONObject jObject = null;
                    jObject = new JSONObject(response);

                    if (ResponseHandler.isSuccess(response, null)) {
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
                        alertDialogBuilder.setMessage(ResponseHandler.getString(jObject, "message"));
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", binding.mobileNumber.getText().toString());
                params.put("firebase_token", deviceToken);
                // params.put("deviceInfo", HELPER.deviceINFO());
                HELPER.print("params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);


    }

    private void checkForPassword() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.checkPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                HELPER.print("RESPONSE", response);

                if (!ResponseHandler.isSuccess(response, null)) {
                    JSONObject responseJson = ResponseHandler.createJsonObject(response);
                    if (ResponseHandler.getString(responseJson, "message").contains("deleted") || ResponseHandler.getString(responseJson, "message").contains("blocked")) {
                        isNoPassword = false;
                        alertDialogBuilder.setMessage(ResponseHandler.getString(responseJson, "message"));
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        binding.passwordTextLayout.setVisibility(View.GONE);
                        return;
                    }

                    if (ResponseHandler.getString(responseJson, "message").contains("update")) {
                        //show dialog
                        isNoPassword = true;
                        askPassordDialog();
                        binding.passwordTextLayout.setVisibility(View.GONE);
                        return;
                    }

                    if (ResponseHandler.getString(responseJson, "message").contains("USER_EXIST") || ResponseHandler.getString(responseJson, "message").contains("USER_NOT_FOUND")) {
                        isNoPassword = false;
                        isValided = true;
                        binding.passwordTextLayout.setVisibility(View.VISIBLE);
                    }
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
                params.put("phone", binding.mobileNumber.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void updatePassword(String newPassword) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.updatePassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                HELPER.print("RESPONSE", response);
                passwordData = newPassword;
                callsigninapicall();
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
                params.put("phone", binding.mobileNumber.getText().toString());
                params.put("passeword", newPassword);
                HELPER.print("params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private AlertDialog.Builder alertDialogBuilder;

    private void callsigninapicall() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.loginWithPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                HELPER.print("RESPONSE", response);
                try {
                    JSONObject jObject = null;
                    jObject = new JSONObject(response);

                    if (ResponseHandler.isSuccess(response, null)) {
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
                        alertDialogBuilder.setMessage(ResponseHandler.getString(jObject, "message"));
                        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", binding.mobileNumber.getText().toString());
                params.put("passeword", passwordData);
                params.put("firebase_token", deviceToken);
                params.put("deviceInfo", HELPER.deviceINFO());
                HELPER.print("params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);


    }

    private void setNotificationHandle() {
        // Sets up permissions request launcher.
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), result -> {
                    if (result) {
                        HELPER.print("PERMISSION_RESULT", result.toString());
                    } else {
                        androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
                                .setTitle("Permission")
                                .setMessage(getString(R.string.notificationPermissionRequiredMsg))
                                .setPositiveButton("GO", (dialogInterface, i) -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts(
                                                "package",
                                                getPackageName(),
                                                null
                                        );
                                        intent.setData(uri);
                                        act.startActivityForResult(intent, 0);
                                    }
                                })
                                .setNeutralButton("LATER", (dialogInterface, i) -> {
                                })
                                .show();
                        AlertDialogBuilder.setCancelable(false);
                    }
                });
    }

    private void setNotificationPermission() {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //showDummyNotification()
            HELPER.print("POST_NOTIFICATIONS", "DONE");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    AskPasswordFragment askPasswordFragment;

    public void askPassordDialog() {
        try {
            if (askPasswordFragment != null) {
                if (askPasswordFragment.isVisible()) {
                    askPasswordFragment.dismiss();
                }
            }
            askPasswordFragment = new AskPasswordFragment();
            askPasswordFragment.setActivity(act);
            askPasswordFragment.setCanCancel(false);
            AskPasswordFragment.AskPasswordInterface onLetterHeadListener = (layout, password) -> {
                isValided = true;
                updatePassword(password);

            };
            askPasswordFragment.setClickListener(onLetterHeadListener);
            askPasswordFragment.setCancelable(false);
            askPasswordFragment.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}