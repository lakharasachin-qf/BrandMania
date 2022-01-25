package com.app.brandadmin.Activity.brand;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
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
import com.app.brandadmin.Common.Constant;
import com.app.brandadmin.Common.MakeMyBrandApp;
import com.app.brandadmin.Common.ObserverActionID;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Common.ResponseHandler;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.Fragment.bottom.ListBottomFragment;
import com.app.brandadmin.Fragment.bottom.PickerFragment;
import com.app.brandadmin.Interface.ItemSelectionInterface;
import com.app.brandadmin.Interface.alertListenerCallback;
import com.app.brandadmin.Model.BrandListItem;
import com.app.brandadmin.Model.CommonListModel;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.APIs;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivityUpdateBandListBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateBandList extends BaseActivity implements ItemSelectionInterface, alertListenerCallback {
    PreafManager prefManager;
    Gson gson;
    public static int BRAND_CATEGORY = 0;
    private String BrandTitle;
    private int showingView = -1;
    private boolean isEditModeEnable = false;
    private BrandListItem listModel;
    private BrandListItem data;
    private boolean isLoading = false;
    private ActivityUpdateBandListBinding binding;
    private Activity act;
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    private ListBottomFragment bottomSheetFragment;
    CommonListModel commonListModel;

    private AlertDialog.Builder alertDialogBuilder;
    private Uri mCropImageUri;
    private Bitmap selectedLogo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        captureScreenShort();
        binding = DataBindingUtil.setContentView(act, R.layout.activity_update_band_list);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        prefManager = new PreafManager(this);
        gson = new Gson();
        CodeReUse.RemoveError(binding.categoryEdt, binding.categoryEdtLayout);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);
        CodeReUse.RemoveError(binding.addressEdt, binding.addressEdtLayout);
        CodeReUse.RemoveError(binding.websiteEdt, binding.websiteEdtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        CodeReUse.RemoveError(binding.businessServiceEdt, binding.businessFacilityEdtLayout);
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
        data = gson.fromJson(getIntent().getStringExtra("data"), BrandListItem.class);
        Log.e("SSSS",gson.toJson(listModel));
        Log.e("data",gson.toJson(data));

        if (listModel != null) {
            binding.catIdEdt.setText(listModel.getId());
            binding.IdEdt.setText(listModel.getId());
            binding.categoryEdt.setText(listModel.getCategoryName());
            binding.nameTxt.setText(listModel.getName());
            binding.phoneTxt.setText(listModel.getPhonenumber());
            binding.addressEdt.setText(listModel.getAddress());
            binding.websiteEdt.setText(listModel.getWebsite());
            binding.emailIdEdt.setText(listModel.getEmail());
            binding.businessServiceEdt.setText(listModel.getBrandService());
            binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }

            });
            Glide.with(act).load(listModel.getLogo()).placeholder(R.drawable.placeholder).into((binding.viewImgFirst));
            Glide.with(act).load(listModel.getFrame()).placeholder(R.drawable.placeholder).into((binding.selectframe1));

            binding.viewImgFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((listModel.getNo_of_used_image().isEmpty() || listModel.getLogo().isEmpty()) || listModel.getNo_of_used_image().equalsIgnoreCase("0")) {
                        //         onSelectImageClick(v);

                        if (!isEditModeEnable) {
                            if (binding.viewImgFirst.getTag().toString().equalsIgnoreCase("1"))
                                pickerView(Constant.PICKER_FIRST, true, selectedLogo);
                            else
                                pickerView(Constant.PICKER_FIRST, false, null);
                        }
                    } else {
                        new AlertDialog.Builder(act)
                                .setMessage("once you download or share image. You can't change your logo.\nIf you want to change logo please contact to admin.")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        ((alertListenerCallback) act).alertListenerClick();
                                    }
                                })
                                .show();
                    }
                }
            });
        }
        else
        {
            binding.catIdEdt.setText(prefManager.getActiveBrand().getCategoryName());
            binding.IdEdt.setText(prefManager.getActiveBrand().getId());
             binding.categoryEdt.setText(prefManager.getActiveBrand().getCategoryName());
            binding.nameTxt.setText(prefManager.getActiveBrand().getName());
            binding.phoneTxt.setText(prefManager.getActiveBrand().getPhonenumber());
            binding.addressEdt.setText(prefManager.getActiveBrand().getAddress());
            binding.websiteEdt.setText(prefManager.getActiveBrand().getWebsite());
            binding.emailIdEdt.setText(prefManager.getActiveBrand().getEmail());
            binding.businessServiceEdt.setText(prefManager.getActiveBrand().getBrandService());
            //            binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onBackPressed();
//                }
//
//            });
            Glide.with(act).load(prefManager.getActiveBrand().getLogo()).placeholder(R.drawable.placeholder).into((binding.viewImgFirst));
          //  Glide.with(act).load(listModel.getFrame()).placeholder(R.drawable.placeholder).into((binding.selectframe1));


            binding.viewImgFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((prefManager.getActiveBrand().getNo_of_used_image().isEmpty() || prefManager.getActiveBrand().getLogo().isEmpty()) || prefManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
                        //         onSelectImageClick(v);

                        if (!isEditModeEnable) {
                            if (binding.viewImgFirst.getTag().toString().equalsIgnoreCase("1"))
                                pickerView(Constant.PICKER_FIRST, true, selectedLogo);
                            else
                                pickerView(Constant.PICKER_FIRST, false, null);
                        }
                    } else {
                        new AlertDialog.Builder(act)
                                .setMessage("once you download or share image. You can't change your logo.\nIf you want to change logo please contact to admin.")
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        ((alertListenerCallback) act).alertListenerClick();
                                    }
                                })
                                .show();
                    }
                }
            });
        }



        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

    }
    //For CustomFrame
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                .start(this);

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                binding.viewImgFirst.setVisibility(View.VISIBLE);
                binding.imgEmptyStateFirst.setVisibility(View.GONE);
                binding.actionDeleteFirst.setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.viewImgFirst)).setImageURI(result.getUri());
                ImageView imageView = ((ImageView) findViewById(R.id.viewImgFirst));
                selectedLogo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

            }
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

        if (binding.categoryEdt.getText().toString().trim().length() == 0) {
            isError = true;
            isFocus = true;
            binding.categoryEdtLayout.setError(getString(R.string.brandcategory_text));
            binding.categoryEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.categoryEdt.requestFocus();
            binding.scrollView.scrollTo(0,binding.categoryEdt.getBottom());
        }
        if (binding.nameTxt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.nameTxtLayout.setError(getString(R.string.brandname_text));
            binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.nameTxt.requestFocus();
            if (!isFocus) {
                binding.nameTxt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0,binding.nameTxt.getBottom());
            }
        }

        if (!binding.phoneTxt.getText().toString().trim().equals("")) {
            if (binding.phoneTxt.getText().toString().trim().length() < 10) {
                isError = true;
                binding.phoneTxtLayout.setError(getString(R.string.validphoneno_txt));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0,binding.phoneTxt.getBottom());
                }
                return;
            }

        }
        else
        {
            if (binding.phoneTxt.getText().toString().trim().equals(""))
            {isError = true;
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                if (!isFocus) {
                    binding.emailIdEdt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0,binding.emailIdEdt.getBottom());
                }
                return;
            }

        }

//        if (binding.addressEdt.getText().toString().trim().length() == 0) {
//            isError = true;
//
//            binding.addressEdtLayout.setError(getString(R.string.enter_address));
//            binding.addressEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
//            if (!isFocus) {
//                binding.addressEdt.requestFocus();
//                isFocus = true;
//                binding.scrollView.scrollTo(0,binding.addressEdt.getBottom());
//            }
//
//        }



        if (!isError) {
            Bitmap bitmap = null;
            if (selectedLogo != null) {
                bitmap = selectedLogo;
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
                .addMultipartParameter("brand_id", binding.IdEdt.getText().toString())
                .addMultipartParameter("br_category",  binding.categoryEdt.getText().toString())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .addMultipartParameter("br_service",binding.businessServiceEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        if (commonListModel!=null)
        {
            request.addMultipartParameter("br_category",commonListModel.getId());
        }
        else
        {
            if (listModel != null)
                request.addMultipartParameter("br_category",listModel.getCategoryId());
            else
                request.addMultipartParameter("br_category",prefManager.getActiveBrand().getCategoryId());
        }

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            Log.e("br_logo", String.valueOf(img1File));
        }

        if (img1File != null) {
            request.addMultipartFile("frame", img1File);
            Log.e("br_logo", String.valueOf(img1File));
        }

        Log.e("PARAM",gson.toJson(request));

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
                    selectedLogo = bitmap;
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