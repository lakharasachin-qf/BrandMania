package com.app.brandmania.Activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.Fragment.bottom.ListBottomFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityAddBrandBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddBrandActivity extends BaseActivity {
    Activity act;
    private ActivityAddBrandBinding binding;
    public static int BRAND_CATEGORY = 0;
    private String BrandTitle;
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    PreafManager preafManager;
    private String is_completed="";
    private ListBottomFragment bottomSheetFragment;
    private Bitmap selectedImagesBitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_add_brand);
        preafManager=new PreafManager(this);
        binding.addExpenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
        binding.logoCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView(CodeReUse.PICK_FIRST);
            }
        });
        binding.frameCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView(CodeReUse.PICK_SECOND);
            }
        });
  //      getBrandCategory(BRAND_CATEGORY);
//        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
//            }
//        });
    }
    private void Validation() {
        boolean isError = false;
        boolean isFocus = false;
        if (binding.categoryEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.categoryEdt.setError(getString(R.string.enter_first_name));
            binding.categoryEdt.requestFocus();

        }
        if (binding.nameTxt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.nameTxt.setError(getString(R.string.enter_last_name));
            binding.nameTxt.requestFocus();

        }
        if (binding.phoneTxt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.phoneTxt.setError(getString(R.string.enter_email_id));
            binding.phoneTxt.requestFocus();

        }
        if (binding.addressEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.addressEdt.setError(getString(R.string.enter_email_id));
            binding.addressEdt.requestFocus();

        }

        if (binding.websiteEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.websiteEdt.setError(getString(R.string.enter_email_id));
            binding.websiteEdt.requestFocus();

        }

        if (binding.emailIdEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.emailIdEdt.setError(getString(R.string.enter_email_id));
            binding.emailIdEdt.requestFocus();

        }

        if (!isError) {
            Bitmap bitmap = null;
            if (selectedImagesBitmap != null) {
                bitmap = selectedImagesBitmap;
            }
            Bitmap bitmap1 = null;
            if (selectedImagesBitmap != null) {
                bitmap1 = selectedImagesBitmap;
            }
            addBrand(bitmap,bitmap1);
        }

    }


    private void addBrand(Bitmap img,Bitmap img1) {
        Log.e("API", APIs.ADD_BRAND);
        Log.e("API", preafManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }
        File img1File1 = null;
        if (img1 != null) {
            img1File1= CodeReUse.createFileFromBitmap(act, "photo.jpeg", img1);
        }

        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.ADD_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer"+preafManager.getUserToken())
                .addMultipartParameter("br_category", binding.categoryEdt.getText().toString())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            Log.e("br_logo", String.valueOf(img1File));
        }

        if (img1File1 != null) {
            request.addMultipartFile("frame", img1File1);
            Log.e("frame", String.valueOf(img1File1));
        }

        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utility.Log("Verify-Response", response);

                        try {

                            if (response.getBoolean("status")) {
                                JSONObject jsonArray = response.getJSONObject("data");
                                is_completed= jsonArray.getString("is_completed");
                                if (is_completed.equals("2"))
                                {
                                    Intent i = new Intent(act, HomeActivity.class);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onError(ANError error) {

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
    private void getBrandCategory(int flag) {
        String apiUrl = "";
        int requestedMethod = 0;

        if (flag == BRAND_CATEGORY) {
            apiUrl = APIs.GET_BRAND_CATEGORY;
            requestedMethod = Request.Method.POST;
        }

        Utility.Log("API : ", apiUrl);

        StringRequest request = new StringRequest(requestedMethod, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Utility.Log(flag + "- Response : ", response);
                try {
                    if (ResponseHandler.isSuccess(response, null)) {
                        JSONObject responseJson = ResponseHandler.createJsonObject(response);
                        JSONArray jsonArray = ResponseHandler.getJSONArray(responseJson, "data");
                        Log.e("jsonArray-", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObj = jsonArray.getJSONObject(i);
                            CommonListModel listModel = new CommonListModel();
                            listModel.setLayoutType(CommonListModel.LAYOUT_BLOCK);
                            listModel.setId(ResponseHandler.getString(itemObj, "id"));
                            listModel.setName(ResponseHandler.getString(itemObj, "biz_cat_name"));
                            BRANDTypeList.add(listModel);
                        }
                        Log.w("Size", String.valueOf(BRANDTypeList.size()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
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
    private void pickerView(int actionId) {
        PickerFragment pickerFragment = new PickerFragment(this);
        pickerFragment.setActionId(actionId);


        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == CodeReUse.PICK_FIRST) {
                    selectedImagesBitmap = bitmap;
                    RelativeLayout placeHolderLayout = findViewById(R.id.chooselogo1);
                    binding.selectlogo1.setImageBitmap(bitmap);
                    placeHolderLayout.setVisibility(View.GONE);
                    binding.selectlogo1.setVisibility(View.VISIBLE);
                }
                    if (flag == CodeReUse.PICK_SECOND) {
                        selectedImagesBitmap = bitmap;
                        RelativeLayout placeHolderLayout1 = findViewById(R.id.chooseFrame1);
                        binding.selectframe1.setImageBitmap(bitmap);
                        placeHolderLayout1.setVisibility(View.GONE);
                        binding.selectframe1.setVisibility(View.VISIBLE);
                    }
                   /* binding.viewImgFirst.setImageBitmap(bitmap);
                    binding.imgEmptyStateFirst.setVisibility(View.GONE);
                    binding.actionDeleteFirst.setVisibility(View.VISIBLE);
                    selectedImageFirst = bitmap;
                    binding.viewImgFirst.setTag("1");
                    binding.imgCardSecond.setVisibility(View.VISIBLE);
                    binding.viewImgFirst.setVisibility(View.VISIBLE);*/

            }
        };
        pickerFragment.setImageLoad(imageLoad);
        pickerFragment.show(getSupportFragmentManager(), pickerFragment.getTag());
    }


//    @Override
//    public void onItemSelection(int calledFlag, int position, CommonListModel listModel) {
//        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {
//            bottomSheetFragment.dismiss();
//        }
//        if (calledFlag == BRAND_CATEGORY) {
//            binding.categoryEdt.setText(listModel.getName());
//        }
//    }
}