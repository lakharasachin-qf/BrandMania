package com.app.brandmania.Activity.admin;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.ListBottomFragment;
import com.app.brandmania.Interface.ItemSelectionInterface;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivitySendNotificationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendNotification extends BaseActivity implements ItemSelectionInterface {
    Activity act;
    private ActivitySendNotificationBinding binding;
    public static int BRAND_CATEGORY = 0;
    private String BrandTitle;
    CommonListModel commonListModel;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    PreafManager preafManager;
    private boolean isLoading = false;
    private ListBottomFragment bottomSheetFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_send_notification);
        preafManager = new PreafManager(this);
        getNotificationFlag();
        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.addExpenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
            }
        });
        CodeReUse.RemoveError(binding.categoryEdt, binding.categoryEdtLayout);
        CodeReUse.RemoveError(binding.notificationEdt, binding.notificationEdtLayout);
        CodeReUse.RemoveError(binding.messageTxt, binding.messageLayout);


    }


    private void Validation() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.categoryEdt.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.categoryEdtLayout.setError(getString(R.string.brandcategory_text));
            binding.categoryEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.categoryEdt.requestFocus();
            binding.scrollView.scrollTo(0, binding.categoryEdt.getBottom());
        }
        if (binding.notificationEdt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.notificationEdt.setError(getString(R.string.brandname_text));
            binding.notificationEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

            if (!isFocus) {
                binding.notificationEdt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.notificationEdt.getBottom());
            }
        }

        if (binding.messageTxt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.messageTxt.setError(getString(R.string.brandname_text));
            binding.messageLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

            if (!isFocus) {
                binding.messageTxt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.messageTxt.getBottom());
            }
        }

        if (!isError) {

            addBrand();
        }

    }

    public void showFragmentList(int callingFlag, String title, ArrayList<CommonListModel> datalist) {
        bottomSheetFragment = new ListBottomFragment();
        Log.e("Size---", String.valueOf(datalist.size()));
        bottomSheetFragment.setListData(callingFlag, title, datalist);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void addBrand() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showLoadingTran(act);


        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.ADD_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer" + preafManager.getUserToken())
                .addMultipartParameter("br_category", commonListModel.getId())

                .addMultipartParameter("br_service", binding.messageTxt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);


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
                        Utility.dismissLoadingTran();
                        Utility.Log("Verify-Response", response);
                        ArrayList<BrandListItem> brandListItems = new ArrayList<>();


                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissLoadingTran();
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


    private ArrayList<NotificationFlag> notificationFlags=new ArrayList<>();
    private void getNotificationFlag() {
        Utility.Log("API : ", APIs.GET_NOTIFICATION_FLAG);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_NOTIFICATION_FLAG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("GET_NOTIFICATION_FLAG", response);
                try {
                    JSONObject res = new JSONObject(response);
                    JSONArray jsonArray1 = res.getJSONArray("data");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        NotificationFlag notificationFlag = new NotificationFlag();
                        notificationFlag.setId(ResponseHandler.getString(jsonObject, "id"));
                        notificationFlag.setNotificationFlag(ResponseHandler.getString(jsonObject, "notification_flag"));
                        notificationFlag.setName(ResponseHandler.getString(jsonObject, "name"));
                        notificationFlags.add(notificationFlag);
                    }
                    getCategory();
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

    private void getCategory() {
        String apiUrl = "";
        Utility.Log("API : ", apiUrl);

        StringRequest request = new StringRequest(Request.Method.GET, APIs.GET_CATEGORY_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("GET_USER_ADMIN", response);

//                try {
//                    if (ResponseHandler.isSuccess(response, null)) {
//                        JSONObject responseJson = ResponseHandler.createJsonObject(response);
//                        JSONArray jsonArray = ResponseHandler.getJSONArray(responseJson, "data");
//                        Log.e("jsonArray-", jsonArray.toString());
////                        for (int i = 0; i < jsonArray.length(); i++) {
////                            JSONObject itemObj = jsonArray.getJSONObject(i);
////                            CommonListModel listModel = new CommonListModel();
////                            listModel.setLayoutType(CommonListModel.LAYOUT_BLOCK);
////                            listModel.setId(ResponseHandler.getString(itemObj, "id"));
////                            listModel.setName(ResponseHandler.getString(itemObj, "biz_cat_name"));
////                            BRANDTypeList.add(listModel);
////                        }
//                        Log.w("Size", String.valueOf(BRANDTypeList.size()));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();


                Utility.Log("Params : ", map.toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    @Override
    public void onItemSelection(int calledFlag, int position, CommonListModel listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (calledFlag == BRAND_CATEGORY) {
            binding.categoryEdt.setText(listModel.getName());
            commonListModel = listModel;
        }
    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }


}