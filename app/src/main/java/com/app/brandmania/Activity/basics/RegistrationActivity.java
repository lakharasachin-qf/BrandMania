package com.app.brandmania.Activity.basics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.brand.AddBranddActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityRegistrationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    Activity act;
    private ActivityRegistrationBinding binding;
    private boolean isLoading = false;
    private String is_completed = "";
    int errorColor;
    final int version = Build.VERSION.SDK_INT;
    PreafManager preafManager;
    AlertDialog.Builder alertDialogBuilder;
    private ImageView menuOtpion;
    String referrerCode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_registration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialogBuilder = new AlertDialog.Builder(act);
        preafManager = new PreafManager(this);
        binding.emailId.setImeActionLabel("Custom text", KeyEvent.KEYCODE_ENTER);

        referrerCode = preafManager.getSpleshReferrer();
        Log.e("refferrer", "s" + referrerCode);

        binding.firstName.setNextFocusDownId(R.id.lastName);
        binding.lastName.setNextFocusDownId(R.id.emailId);
        binding.emailId.setNextFocusDownId(R.id.referrer);
        binding.referrer.setNextFocusDownId(R.id.submitBtn);
        binding.menuOtpion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preafManager.Logout();
                Intent i = new Intent(act, LoginActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                act.finish();

            }
        });

        if (preafManager.getSpleshReferrer() != null && !preafManager.getSpleshReferrer().isEmpty()) {
            binding.referrer.setText(preafManager.getSpleshReferrer());
        }

        menuOtpion = findViewById(R.id.menuOtpion);
        menuOtpion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(RegistrationActivity.this, view);
                popup.setOnMenuItemClickListener(RegistrationActivity.this);
                popup.inflate(R.menu.menu);
                popup.show();
            }
        });
        preafManager.setEMAIL_Id(binding.emailId.getText().toString());

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Validation();

            }
        });
        CodeReUse.RemoveError(binding.firstName, binding.userNameTextLayout);
        CodeReUse.RemoveError(binding.lastName, binding.lastNameTextLayout);
        CodeReUse.RemoveError(binding.emailId, binding.emailIdTextLayout);

        String CreatAccount = "Create<br>Account</font></br>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // binding.signupText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            binding.creatAccount.setText(Html.fromHtml(CreatAccount, Html.FROM_HTML_MODE_COMPACT));
        } else {
            //binding.signupText.setText(Html.fromHtml(Message));
            binding.creatAccount.setText(Html.fromHtml(CreatAccount));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void Validation() {

        boolean isError = false;
        boolean isFocus = false;

        if (binding.firstName.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.userNameTextLayout.setError(getString(R.string.enter_first_name));
            binding.userNameTextLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
            binding.firstName.requestFocus();

        }

        if (binding.lastName.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.lastNameTextLayout.setError(getString(R.string.enter_last_name));
            binding.lastNameTextLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
            binding.lastName.requestFocus();

        }
        if (!binding.emailId.getText().toString().equals("")) {
            if (!CodeReUse.isEmailValid(binding.emailId.getText().toString().trim())) {
                isError = true;
                isFocus = true;
                binding.emailIdTextLayout.setError(getString(R.string.enter_valid_email_address));
                binding.emailIdTextLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
                binding.emailId.requestFocus();

            } else {

            }

        } else {
            if (binding.emailId.getText().toString().trim().length() == 0) {
                isError = true;
                isFocus = true;
                binding.emailIdTextLayout.setError(getString(R.string.enter_email_id));
                binding.emailIdTextLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));
                binding.emailId.requestFocus();

            }
        }
        if (!isError) {
            addUser();
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logo:
                preafManager.Logout();
                Intent i = new Intent(act, LoginActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                act.finish();
                return true;
        }
        return false;
    }

    private void addUser() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Log.e("API", APIs.USER_REGISTRATION);

        Log.w("Tokennn", preafManager.getUserToken());
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.USER_REGISTRATION)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer " + preafManager.getUserToken())
                .addMultipartParameter("first_name", binding.firstName.getText().toString())
                .addMultipartParameter("last_name", binding.lastName.getText().toString())
                .addMultipartParameter("email", binding.emailId.getText().toString())
                .addMultipartParameter("referral_code", binding.referrer.getText().toString())
                /*hashMap.put("referrerCode", referrerCode);*/
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        preafManager.setEMAIL_Id(binding.emailId.getText().toString());
        Log.e("test", gson.toJson(request));
        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        isLoading = false;
                        Utility.dismissProgress();
                        Utility.Log("Verify-Response", response);
                        try {
                            if (response.getBoolean("status")) {
                                preafManager.setIs_Registration(true);
                                //preafManager.setSpleshReferrer(null);
                                JSONObject jsonArray = response.getJSONObject("data");
                                is_completed = jsonArray.getString("is_completed");

                                preafManager.loginStep(is_completed);
                                if (is_completed.equals("1")) {
                                    Intent i = new Intent(act, AddBranddActivity.class);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                    finish();
                                }
                                if (is_completed.equals("2")) {
                                    getBrandList();
//                                    Intent i = new Intent(act, HomeActivity.class);
//                                    i.addCategory(Intent.CATEGORY_HOME);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(i);
//                                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//                                    finish();
                                }

                            }else{

                                alertDialogBuilder.setMessage(ResponseHandler.getString(response, "message"));
                                alertDialogBuilder.setPositiveButton("Ok", (arg0, arg1) -> {
                                    arg0.dismiss();
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissProgress();
                        if (error.getErrorCode() != 0) {
                            Log.e("onError errorCode : ", String.valueOf(error.getErrorCode()));
                            Log.e("onError errorBody : ", error.getErrorBody());
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        } else {
                            Log.e("onError errorDetail : ", error.getErrorDetail());
                        }
                    }
                });

    }

    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("addbrandresponce", response);
                ArrayList<BrandListItem> brandListItems = new ArrayList<>();
                try {
                    JSONObject res = new JSONObject(response);

                    JSONArray jsonArray1 = res.getJSONArray("data");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        BrandListItem brandListItemm = new BrandListItem();
                        brandListItemm.setId(ResponseHandler.getString(jsonObject, "id"));
                        brandListItemm.setCategoryId(ResponseHandler.getString(jsonObject, "br_category_id"));
                        brandListItemm.setCategoryName(ResponseHandler.getString(jsonObject, "br_category_name"));
                        brandListItemm.setName(ResponseHandler.getString(jsonObject, "br_name"));
                        brandListItemm.setPhonenumber(ResponseHandler.getString(jsonObject, "br_phone"));
                        brandListItemm.setWebsite(ResponseHandler.getString(jsonObject, "br_website"));
                        brandListItemm.setEmail(ResponseHandler.getString(jsonObject, "br_email"));
                        brandListItemm.setAddress(ResponseHandler.getString(jsonObject, "br_address"));
                        brandListItemm.setLogo(ResponseHandler.getString(jsonObject, "br_logo"));
                        brandListItemm.setPackage_id(ResponseHandler.getString(jsonObject, "package_id"));
                        brandListItemm.setIs_frame(ResponseHandler.getString(jsonObject, "is_frame"));
                        brandListItemm.setFrame_message(ResponseHandler.getString(jsonObject, "frame_message"));
                        brandListItemm.setFrambaseyrl(ResponseHandler.getString(jsonObject, "fream_base_url"));
                        brandListItemm.setIs_payment_pending(ResponseHandler.getString(jsonObject, "is_payment_pending"));
                        brandListItemm.setSubscriptionDate(ResponseHandler.getString(jsonObject, "subscription_date"));
                        brandListItemm.setPayment_message(ResponseHandler.getString(jsonObject, "payment_message"));
                        brandListItemm.setPackagename(ResponseHandler.getString(jsonObject, "package"));
                        brandListItemm.setPackagemessage(ResponseHandler.getString(jsonObject, "package_message"));
                        brandListItemm.setNo_of_total_image(ResponseHandler.getString(jsonObject, "no_of_img"));
                        brandListItemm.setNo_of_used_image(ResponseHandler.getString(jsonObject, "no_of_used_img"));
                        brandListItemm.setNo_of_frame(ResponseHandler.getString(jsonObject, "no_of_frame"));
                        brandListItemm.setNo_of_remaining(ResponseHandler.getString(jsonObject, "remaining_img"));
                        brandListItemm.setExpiery_date(ResponseHandler.getString(jsonObject, "expire_date"));
                        brandListItemm.setRate(ResponseHandler.getString(jsonObject, "rate"));

                        JSONArray jsonArray = jsonObject.getJSONArray("br_frame");
                        ArrayList<FrameItem> frameItems = null;
                        frameItems = new ArrayList<>();
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                            FrameItem frameItem = new FrameItem();
                            frameItem.setFrame1(ResponseHandler.getString(jsonObject, "fream_base_url") + "/" + ResponseHandler.getString(jsonObject1, "frame_path"));
                            frameItem.setFrameId(ResponseHandler.getString(jsonObject1, "id"));

                            frameItems.add(frameItem);
                        }

                        brandListItemm.setFrame(frameItems);
                        brandListItems.add(brandListItemm);
                    }


                    preafManager.setAddBrandList(brandListItems);
                    preafManager.setIS_Brand(true);

                    if (brandListItems != null && brandListItems.size() != 0) {
                        preafManager.setActiveBrand(brandListItems.get(0));
                    }
                    Intent i = new Intent(act, HomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                    finish();

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
                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("Error ", body);


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
                params.put("Authorization", "Bearer " + preafManager.getUserToken());
                Log.e("Token", params.toString());
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

    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }


}
