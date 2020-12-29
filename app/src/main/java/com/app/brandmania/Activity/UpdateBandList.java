package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Connection.BaseActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.ItemSelectionInterface;
import com.app.brandmania.Fragment.bottom.ListBottomFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityUpdateBandListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateBandList extends BaseActivity implements ItemSelectionInterface,alertListenerCallback {
    PreafManager prefManager;
    Gson gson;
    public static int BRAND_CATEGORY = 0;
    private String BrandTitle;
    private int showingView = -1;
    private boolean isEditModeEnable = false;
    private BrandListItem listModel;
    private boolean isLoading=false;
    private ActivityUpdateBandListBinding binding;
    private Activity act;
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    private ListBottomFragment bottomSheetFragment;
    CommonListModel commonListModel;
    private Bitmap selectedImagesBitmap;
    private AlertDialog.Builder alertDialogBuilder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        captureScreenShort();
        binding = DataBindingUtil.setContentView(act, R.layout.activity_update_band_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        prefManager = new PreafManager(this);
        gson = new Gson();
        CodeReUse.RemoveError(binding.categoryEdt, binding.categoryEdtLayout);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);
        CodeReUse.RemoveError(binding.addressEdt, binding.addressEdtLayout);
        CodeReUse.RemoveError(binding.websiteEdt, binding.websiteEdtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        alertDialogBuilder=new AlertDialog.Builder(act);
        binding.viewImgFirst.setTag("0");

        getBrandCategory(BRAND_CATEGORY);
        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
            }
        });
        binding.addExpenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
        listModel = gson.fromJson(getIntent().getStringExtra("detailsObj"), BrandListItem.class);
      //  getConversation();
        Log.e("-- ", getIntent().getStringExtra("detailsObj"));
        //not filled conditions remains ask to mem
        if (listModel != null) {
            binding.categoryEdt.setText(listModel.getCategoryName());
            binding.nameTxt.setText(listModel.getName());
            binding.phoneTxt.setText(listModel.getPhonenumber());
            binding.addressEdt.setText(listModel.getAddress());
            binding.websiteEdt.setText(listModel.getWebsite());
            binding.emailIdEdt.setText(listModel.getEmail());
            binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }

            });
            Glide.with(act).load(listModel.getLogo()).placeholder(R.drawable.placeholder).into((binding.viewImgFirst));
            Glide.with(act).load(listModel.getFrame()).placeholder(R.drawable.placeholder).into((binding.selectframe1));
        }

        if (listModel.getNo_of_used_image().isEmpty() || listModel.getNo_of_used_image().equalsIgnoreCase("0") || listModel.getLogo().isEmpty()) {
            binding.viewImgFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isEditModeEnable) {
                        if (binding.viewImgFirst.getTag().toString().equalsIgnoreCase("1"))
                            pickerView(Constant.PICKER_FIRST, true, selectedImagesBitmap);
                        else
                            pickerView(Constant.PICKER_FIRST, false, null);
                    }
                }
            });
        }else
        {
            binding.nameTxt.setEnabled(true);
        }
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
                params.put("Authorization", "Bearer"+prefManager.getUserToken());
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
    private void Validation() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.categoryEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.categoryEdtLayout.setError(getString(R.string.brandcategory_text));
            binding.categoryEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.categoryEdt.requestFocus();

        }
        if (binding.nameTxt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.nameTxtLayout.setError(getString(R.string.brandname_text));
            binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.nameTxt.requestFocus();

        }

        if (!binding.phoneTxt.getText().toString().equals("")) {
            if (binding.phoneTxt.getText().toString().length() < 10) {
                binding.phoneTxtLayout.setError(getString(R.string.validphoneno_txt));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.phoneTxt.requestFocus();
                return;
            }

        }
        else
        {
            if (binding.phoneTxt.getText().toString().equals(""))
            {
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.phoneTxt.requestFocus();
                return;
            }

        }

        if (binding.addressEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.addressEdtLayout.setError(getString(R.string.enter_address));
            binding.addressEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.addressEdt.requestFocus();

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
            EditBrandBrand(bitmap);
        }

    }

    private void EditBrandBrand(Bitmap img) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Log.e("API", APIs.EDIT_BRAND);
        Log.e("API", prefManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.EDIT_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer"+prefManager.getUserToken())
                .addMultipartParameter("brand_id",listModel.getId())
                .addMultipartParameter("br_category",  binding.categoryEdt.getText().toString())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        if (commonListModel!=null)
        {
            request.addMultipartParameter("br_category",commonListModel.getId());
        }
        else
        {
            request.addMultipartParameter("br_category",listModel.getCategoryId());
        }

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            Log.e("br_logo", String.valueOf(img1File));
        }

//        if (img1File1 != null) {
//            request.addMultipartFile("frame", img1File1);
//            Log.e("frame", String.valueOf(img1File1));
//        }

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

                                alertDialogBuilder.setMessage(ResponseHandler.getString(response, "message"));
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        onBackPressed();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                                MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.RELOAD_BRANDS);
                                MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.REFRESH_BRAND_NAME);


                            }
                        }
                        catch (JSONException e) {
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
    private void pickerView(int actionId, boolean viewMode, Bitmap selectedBitmap) {
        PickerFragment pickerFragment = new PickerFragment(act);
        pickerFragment.setEnableViewMode(viewMode);
        pickerFragment.setActionId(actionId);

        if (viewMode) {
            pickerFragment.setSelectedBitmapForFullView(selectedBitmap);
        }
        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == Constant.PICKER_FIRST) {
                    Glide.with(act).clear(binding.viewImgFirst);
                    Glide.with(act).clear(binding.selectframe1);
                    binding.viewImgFirst.setImageBitmap(bitmap);
                    binding.imgEmptyStateFirst.setVisibility(View.GONE);
                    binding.actionDeleteFirst.setVisibility(View.VISIBLE);
                    selectedImagesBitmap = bitmap;
                    binding.viewImgFirst.setTag("1");
                    if (!isEditModeEnable) {

                    }
                    binding.viewImgFirst.setVisibility(View.VISIBLE);
                }



            }
        };
        pickerFragment.setImageLoad(imageLoad);
        pickerFragment.show(getSupportFragmentManager(), pickerFragment.getTag());
    }
    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
    @Override public void onItemSelection(int calledFlag, int position, CommonListModel listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (calledFlag == BRAND_CATEGORY) {
            binding.categoryEdt.setText(listModel.getName());
            commonListModel=listModel;
        }
    }
    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
    }
    @Override public void alertListenerClick() {
        requestAgain();
    }
    public void captureScreenShort()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}