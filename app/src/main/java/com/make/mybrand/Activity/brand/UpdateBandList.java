package com.make.mybrand.Activity.brand;

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
import com.make.mybrand.Common.Constant;
import com.make.mybrand.Common.MakeMyBrandApp;
import com.make.mybrand.Common.ObserverActionID;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Fragment.bottom.CountrySelectionFragment;
import com.make.mybrand.Fragment.bottom.ListBottomFragment;
import com.make.mybrand.Fragment.bottom.PickerFragment;
import com.make.mybrand.Interface.ItemSelectionInterface;
import com.make.mybrand.Interface.alertListenerCallback;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.Model.CommonListModel;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityUpdateBandListBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
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
    public static int COUNTRY = 1;
    public static int STATE = 2;
    public static int CITY = 3;
    private String BrandTitle;
    private String cityTitle = "Choose City";
    private String countryTitle = "Choose Country";
    private String stateTtitle = "Choose State";

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


    //private ArrayList<CommonListModel> countryList = new ArrayList<>();
    private ArrayList<CommonListModel> stateList = new ArrayList<>();
    private ArrayList<CommonListModel> cityList = new ArrayList<>();

    private boolean wantToShowDropDown = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_update_band_list);
        prefManager = new PreafManager(this);
        gson = new Gson();
        Utility.isLiveModeOff(act);
        CodeReUse.RemoveError(binding.categoryEdt, binding.categoryEdtLayout);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);
        CodeReUse.RemoveError(binding.addressEdt, binding.addressEdtLayout);
        CodeReUse.RemoveError(binding.websiteEdt, binding.websiteEdtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        CodeReUse.RemoveError(binding.businessServiceEdt, binding.businessFacilityEdtLayout);

        CodeReUse.RemoveError(binding.countryEdt, binding.countryLayout);
        CodeReUse.RemoveError(binding.stateEdt, binding.stateLayout);
        CodeReUse.RemoveError(binding.cityEdt, binding.cityLayout);

        alertDialogBuilder = new AlertDialog.Builder(act);
        binding.viewImgFirst.setTag("0");

        getBrandCategory(BRAND_CATEGORY);
        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
            }
        });


        binding.stateLayout.setVisibility(View.VISIBLE);
        binding.countryLayout.setVisibility(View.GONE);
//
//        binding.countryEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (countryList != null)
//                    chooseFragment(COUNTRY, countryTitle, countryList, binding.countryEdt.getText().toString());
//            }
//        });
        getCountryStateCity(CALL_STATE);
        binding.stateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateList != null)
                    chooseFragment(STATE, stateTtitle, stateList, binding.stateEdt.getText().toString());
            }
        });
        binding.cityEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.stateEdt.getText().toString().length() != 0) {
                    if (cityList != null && cityList.size() != 0)
                        chooseFragment(CITY, cityTitle, cityList, binding.cityEdt.getText().toString());
                    else {
                        wantToShowDropDown = true;
                        getCountryStateCity(CALL_CITY);
                    }
                } else {
                    wantToShowDropDown = true;
                    getCountryStateCity(CALL_CITY);
                }
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

        if (listModel != null) {
            binding.catIdEdt.setText(listModel.getId());
            binding.IdEdt.setText(listModel.getId());
            binding.categoryEdt.setText(listModel.getCategoryName());
            binding.nameTxt.setText(listModel.getName());
            binding.phoneTxt.setText(listModel.getPhonenumber());
            binding.addressEdt.setText(listModel.getOriginalAddress());
            binding.websiteEdt.setText(listModel.getWebsite());
            binding.emailIdEdt.setText(listModel.getEmail());

            binding.businessServiceEdt.setText(listModel.getBrandService());

            if (listModel.getPincode() != null && !listModel.getPincode().isEmpty()) {
                binding.pincodeEdt.setText(listModel.getPincode());
            }
//            if (listModel.getCountry() != null && !listModel.getCountry().isEmpty()) {
//                binding.countryLayout.setVisibility(View.VISIBLE);
//                binding.countryEdt.setText(listModel.getCountry());
//
//                CommonListModel data = new CommonListModel();
//                data.setLayoutType(CommonListModel.LAYOUT_BLOCK);
//                data.setId("-1");
//                data.setName(listModel.getCountry());
//                selectedCountry = data;
//            }

            if (listModel.getState() != null && !listModel.getState().isEmpty()) {
                binding.stateLayout.setVisibility(View.VISIBLE);
                binding.stateEdt.setText(listModel.getState());

                CommonListModel data = new CommonListModel();
                data.setLayoutType(CommonListModel.LAYOUT_BLOCK);
                data.setId("-1");
                data.setName(listModel.getState());
                selectedState = data;

            }

            if (listModel.getCity() != null && !listModel.getCity().isEmpty()) {
                binding.cityLayout.setVisibility(View.VISIBLE);
                binding.cityEdt.setText(listModel.getCity());

                CommonListModel data = new CommonListModel();
                data.setLayoutType(CommonListModel.LAYOUT_BLOCK);
                data.setId("-1");
                data.setName(listModel.getCity());
                selectedCity = data;
            }


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
        } else {
            binding.catIdEdt.setText(prefManager.getActiveBrand().getCategoryName());
            binding.IdEdt.setText(prefManager.getActiveBrand().getId());
            binding.categoryEdt.setText(prefManager.getActiveBrand().getCategoryName());
            binding.nameTxt.setText(prefManager.getActiveBrand().getName());
            binding.phoneTxt.setText(prefManager.getActiveBrand().getPhonenumber());
            binding.addressEdt.setText(prefManager.getActiveBrand().getOriginalAddress());
            binding.websiteEdt.setText(prefManager.getActiveBrand().getWebsite());
            binding.emailIdEdt.setText(prefManager.getActiveBrand().getEmail());
            binding.businessServiceEdt.setText(prefManager.getActiveBrand().getBrandService());


            if (prefManager.getActiveBrand().getPincode() != null && !prefManager.getActiveBrand().getPincode().isEmpty()) {
                binding.pincodeEdt.setText(prefManager.getActiveBrand().getPincode());
            }
//            if (prefManager.getActiveBrand().getCountry() != null && !prefManager.getActiveBrand().getCountry().isEmpty()) {
//                binding.countryLayout.setVisibility(View.VISIBLE);
//                binding.countryEdt.setText(prefManager.getActiveBrand().getCountry());
//
//
//            }

            if (prefManager.getActiveBrand().getState() != null && !prefManager.getActiveBrand().getState().isEmpty()) {
                binding.stateLayout.setVisibility(View.VISIBLE);
                binding.stateEdt.setText(prefManager.getActiveBrand().getState());
            }

            if (prefManager.getActiveBrand().getCity() != null && !prefManager.getActiveBrand().getCity().isEmpty()) {
                binding.cityLayout.setVisibility(View.VISIBLE);
                binding.cityEdt.setText(prefManager.getActiveBrand().getCity());
            }


            Glide.with(act).load(prefManager.getActiveBrand().getLogo()).placeholder(R.drawable.placeholder).into((binding.viewImgFirst));
            //  Glide.with(act).load(listModel.getFrame()).placeholder(R.drawable.placeholder).into((binding.selectframe1));


            binding.viewImgFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((prefManager.getActiveBrand().getNo_of_used_image().isEmpty() || prefManager.getActiveBrand().getLogo().isEmpty()) || prefManager.getActiveBrand().getNo_of_used_image().equalsIgnoreCase("0")) {
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
                        //Log.e("jsonArray-", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObj = jsonArray.getJSONObject(i);
                            CommonListModel listModel = new CommonListModel();
                            listModel.setLayoutType(CommonListModel.LAYOUT_BLOCK);
                            listModel.setThumbnail(ResponseHandler.getString(itemObj, "thumbnail_url"));

                            listModel.setId(ResponseHandler.getString(itemObj, "id"));
                            listModel.setName(ResponseHandler.getString(itemObj, "biz_cat_name"));
                            BRANDTypeList.add(listModel);
                        }
                        Log.w("Size", String.valueOf(BRANDTypeList.size()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getCountryStateCity(CALL_STATE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getCountryStateCity(CALL_STATE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + prefManager.getUserToken());
                //Log.e("Token", params.toString());
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
            binding.scrollView.scrollTo(0, binding.categoryEdt.getBottom());
        }
        if (binding.nameTxt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.nameTxtLayout.setError(getString(R.string.brandname_text));
            binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            binding.nameTxt.requestFocus();
            if (!isFocus) {
                binding.nameTxt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.nameTxt.getBottom());
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
                    binding.scrollView.scrollTo(0, binding.phoneTxt.getBottom());
                }
                return;
            }

        } else {
            if (binding.phoneTxt.getText().toString().trim().equals("")) {
                isError = true;
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                if (!isFocus) {
                    binding.emailIdEdt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0, binding.emailIdEdt.getBottom());
                }
                return;
            }

        }

        if (binding.stateEdt.getText().toString().trim().length() == 0) {
            binding.stateLayout.setError("Please select state");
            binding.stateLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            isError = true;
            if (!isFocus) {
                binding.stateEdt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.stateEdt.getBottom());
            }
            return;
        }

        if (binding.cityEdt.getText().toString().trim().length() == 0) {
            binding.cityLayout.setError("Please select city");
            binding.cityLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            isError = true;
            if (!isFocus) {
                binding.cityEdt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.cityEdt.getBottom());
            }
            return;
        }

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
        //Log.e("API", APIs.EDIT_BRAND);
      //  Log.e("API", prefManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.EDIT_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-Authorization", "Bearer" + prefManager.getUserToken())
                .addMultipartParameter("brand_id", binding.IdEdt.getText().toString())
                .addMultipartParameter("br_category", binding.categoryEdt.getText().toString())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .addMultipartParameter("br_service", binding.businessServiceEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        request.addMultipartParameter("br_address", binding.addressEdt.getText().toString());
        request.addMultipartParameter("br_country", "");
        request.addMultipartParameter("br_state", binding.stateEdt.getText().toString());
        request.addMultipartParameter("br_city", binding.cityEdt.getText().toString());
        request.addMultipartParameter("br_pincode", binding.pincodeEdt.getText().toString());

        if (commonListModel != null) {
            request.addMultipartParameter("br_category", commonListModel.getId());
        } else {
            if (listModel != null)
                request.addMultipartParameter("br_category", listModel.getCategoryId());
            else
                request.addMultipartParameter("br_category", prefManager.getActiveBrand().getCategoryId());
        }

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            //Log.e("br_logo", String.valueOf(img1File));
        }

        if (img1File != null) {
            request.addMultipartFile("frame", img1File);
            //Log.e("br_logo", String.valueOf(img1File));
        }
        Log.e("UpdateParam:",gson.toJson(request));
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
                                //MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.REFRESH_LOGO);

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
                            //Log.e("onError errorCode : ", String.valueOf(error.getErrorCode()));
                            //Log.e("onError errorBody : ", error.getErrorBody());
                            //Log.e("onError errorDetail : ", error.getErrorDetail());
                        } else {
                           // Log.e("onError errorDetail : ", error.getErrorDetail());
                        }
                    }
                });
    }

    CountrySelectionFragment countrySelectionFragment;

    public void chooseFragment(int callingFlag, String title, ArrayList<CommonListModel> datalist, String alreadySelectedData) {
        countrySelectionFragment = new CountrySelectionFragment(title, datalist, callingFlag, alreadySelectedData);

        if (countrySelectionFragment.isVisible()) {
            countrySelectionFragment.dismiss();
        }
        if (countrySelectionFragment.isAdded()) {
            countrySelectionFragment.dismiss();
        }

        if (!countrySelectionFragment.isVisible()) {
            countrySelectionFragment.show(getSupportFragmentManager(), countrySelectionFragment.getTag());
        }
    }


    public void showFragmentList(int callingFlag, String title, ArrayList<CommonListModel> datalist) {
        bottomSheetFragment = new ListBottomFragment();
        //Log.e("Size---", String.valueOf(datalist.size()));
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

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }


    private CommonListModel selectedCountry;
    private CommonListModel selectedState;
    private CommonListModel selectedCity;

    @Override
    public void onItemSelection(int calledFlag, int position, CommonListModel listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (countrySelectionFragment != null && countrySelectionFragment.isVisible()) {
            countrySelectionFragment.dismiss();
        }

        if (calledFlag == BRAND_CATEGORY) {
            binding.categoryEdt.setText(listModel.getName());
            commonListModel = listModel;
        }

//        if (calledFlag == COUNTRY) {
//            if (!listModel.getId().equalsIgnoreCase("-1")) {
//                binding.countryEdt.setText(listModel.getName());
//                selectedCountry = listModel;
//                binding.stateLayout.setVisibility(View.VISIBLE);
//                binding.stateEdt.setText("");
//                binding.cityEdt.setText("");
//                selectedCity = null;
//                selectedState = null;
//
//                stateList.clear();
//                getCountryStateCity(CALL_STATE);
//            } else {
//                binding.countryEdt.setText("");
//                selectedCountry = null;
//
//                binding.stateEdt.setText("");
//                selectedState = null;
//                binding.cityEdt.setText("");
//                selectedCity = null;
//
//                binding.cityLayout.setVisibility(View.GONE);
//                binding.stateLayout.setVisibility(View.GONE);
//            }
//        }

        if (calledFlag == STATE) {
            if (!listModel.getId().equalsIgnoreCase("-1")) {
                binding.stateEdt.setText(listModel.getName());
                selectedState = listModel;
                binding.cityLayout.setVisibility(View.VISIBLE);
                binding.cityEdt.setText("");
                selectedCity = null;
                cityList.clear();
                getCountryStateCity(CALL_CITY);
            } else {
                selectedState = null;
                binding.stateEdt.setText("");

                binding.cityEdt.setText("");
                selectedCity = null;

                binding.cityLayout.setVisibility(View.GONE);

            }
        }

        if (calledFlag == CITY) {
            if (!listModel.getId().equalsIgnoreCase("-1")) {
                binding.cityEdt.setText(listModel.getName());
                selectedCity = listModel;
            } else {
                binding.cityEdt.setText("");
                selectedCity = null;
            }
        }
    }

    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
    }

    @Override
    public void alertListenerClick() {
        requestAgain();
    }

    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }


    int CALL_COUNTRY = 0;
    int CALL_STATE = 1;
    int CALL_CITY = 2;

    private void getCountryStateCity(int flag) {
        if (isLoading)
            return;
        isLoading = true;
        String apiUrl = "";


        if (flag == CALL_STATE) {
            apiUrl = APIs.GET_STATE + "/101"; //+ selectedCountry.getId();
            stateList.clear();
        }

        if (flag == CALL_CITY) {
            apiUrl = APIs.GET_CITY + "/" + selectedState.getId();
            cityList.clear();
        }
        Utility.Log("API : ", apiUrl);
        Utility.showLoadingTran(act);
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("GET_COUNTRY : ", response);
                Utility.dismissLoadingTran();
                isLoading = false;
                try {
                    if (ResponseHandler.isSuccess(response, null)) {
                        JSONObject responseJson = ResponseHandler.createJsonObject(response);
                        JSONArray jsonArray = ResponseHandler.getJSONArray(responseJson, "data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject itemObj = jsonArray.getJSONObject(i);
                            CommonListModel listModel = new CommonListModel();
                            listModel.setLayoutType(CommonListModel.LAYOUT_BLOCK);
                            listModel.setId(ResponseHandler.getString(itemObj, "id"));
                            listModel.setName(ResponseHandler.getString(itemObj, "name"));

                            if (flag == CALL_STATE) {
                                stateList.add(listModel);
                            }

                            if (flag == CALL_CITY) {
                                cityList.add(listModel);
                            }
                        }

                        if (flag == CALL_STATE && stateList.size() != 0) {
                            for (int i = 0; i < stateList.size(); i++) {
                                if (binding.stateEdt.getText().toString().equalsIgnoreCase(stateList.get(i).getName())) {
                                    selectedState = stateList.get(i);
                                    getCountryStateCity(CALL_CITY);
                                    break;
                                }
                            }
                        }

                        if (wantToShowDropDown) {
                            if (flag == CALL_STATE) {
                                chooseFragment(STATE, stateTtitle, stateList, binding.stateEdt.getText().toString());
                            }
                            if (flag == CALL_CITY) {
                                chooseFragment(CITY, cityTitle, cityList, binding.cityEdt.getText().toString());
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Utility.dismissLoadingTran();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isLoading = false;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + prefManager.getUserToken());
                //Log.e("Token", params.toString());
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

}