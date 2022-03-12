package com.app.brandmania.Activity.brand;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.CountrySelectionFragment;
import com.app.brandmania.Fragment.bottom.ListBottomFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.ItemSelectionInterface;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityAddBrandMultipleBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddBrandMultipleActivity extends BaseActivity implements ItemSelectionInterface, alertListenerCallback {
    Activity act;
    private ActivityAddBrandMultipleBinding binding;
    public static int BRAND_CATEGORY = 0;
    public static int COUNTRY = 1;
    public static int STATE = 2;
    public static int CITY = 3;
    private String BrandTitle;
    private String cityTitle = "Choose City";
    private String countryTitle = "Choose Country";
    private String stateTtitle = "Choose State";
    CommonListModel commonListModel;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    PreafManager preafManager;
    private boolean isLoading = false;
    private String is_completed = "";
    private ListBottomFragment bottomSheetFragment;

    private boolean isEditModeEnable = false;
    private AlertDialog.Builder alertDialogBuilder;
    private Uri mCropImageUri;
    private Bitmap selectedLogo;


    private ArrayList<CommonListModel> countryList = new ArrayList<>();
    private ArrayList<CommonListModel> stateList = new ArrayList<>();
    private ArrayList<CommonListModel> cityList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_add_brand_multiple);
        preafManager = new PreafManager(this);
        alertDialogBuilder = new AlertDialog.Builder(act);
        binding.websiteEdt.setText("https://");
        Utility.isLiveModeOff(act);
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
        getBrandCategory(BRAND_CATEGORY);
        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
            }
        });

        binding.stateLayout.setVisibility(View.VISIBLE);
        binding.countryLayout.setVisibility(View.GONE);
//        binding.countryEdt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (countryList != null)
//                    chooseFragment(COUNTRY, countryTitle, countryList, binding.countryEdt.getText().toString());
//            }
//        });
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
                if (binding.stateEdt.getText().length() != 0) {
                    if (cityList != null)
                        chooseFragment(CITY, cityTitle, cityList, binding.cityEdt.getText().toString());
                } else {

                }
            }
        });

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

        binding.viewImgFirst.setTag("0");
        binding.imgCardFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onSelectImageClick(v);
                if (!isEditModeEnable) {
                    if (binding.viewImgFirst.getTag().toString().equalsIgnoreCase("1"))
                        pickerView(Constant.PICKER_FIRST, true, selectedLogo);
                    else
                        pickerView(Constant.PICKER_FIRST, false, null);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }

    }

    public void askPermissions() {
        ActivityCompat.requestPermissions(act,
                new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                1110);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting = false;
        if (requestCode == 1110) {

            boolean readStorageGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, requestCode);
                        }
                    });
                } else {
                    targetSetting = true;
                }
            }
        }
        if (targetSetting) {
            showMessageOKCancel("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 1010);
                }
            });
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public static final int GALLERY_INTENT = 101;

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, GALLERY_INTENT);
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
                selectImageFromGallery();
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

        if (requestCode == GALLERY_INTENT) {
            Uri result = data.getData();
            if (resultCode == RESULT_OK) {
                startCropImageActivity(result);

            }
        }
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

            if (!isFocus) {
                binding.nameTxt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0, binding.nameTxt.getBottom());
            }
        }


        if (!binding.emailIdEdt.getText().toString().trim().equals("")) {
            if (!CodeReUse.isEmailValid(binding.emailIdEdt.getText().toString().trim())) {
                isError = true;

                binding.emailIdEdtLayout.setError(getString(R.string.enter_valid_email_address));
                binding.emailIdEdtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorNavText)));

                if (!isFocus) {
                    binding.emailIdEdt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0, binding.emailIdEdt.getBottom());
                }
            }


        }
        if (!binding.phoneTxt.getText().toString().trim().equals("")) {
            if (binding.phoneTxt.getText().toString().trim().length() < 10) {
                binding.phoneTxtLayout.setError(getString(R.string.validphoneno_txt));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                isError = true;
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0, binding.phoneTxt.getBottom());
                }
                return;
            }

        } else {
            if (binding.phoneTxt.getText().toString().trim().equals("")) {
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0, binding.phoneTxt.getBottom());
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

            addBrand(bitmap);
        }

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
        bottomSheetFragment.setListData(callingFlag, title, datalist);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void addBrand(Bitmap img) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showLoadingTran(act);
        //Log.e("API", APIs.ADD_BRAND);
        //Log.e("API", preafManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }

        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.ADD_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("X-Authorization", "Bearer" + preafManager.getUserToken())
                .addMultipartParameter("br_category", commonListModel.getId())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .addMultipartParameter("br_service", binding.businessServiceEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            //Log.e("br_logo", String.valueOf(img1File));
        }

        request.addMultipartParameter("br_address", binding.addressEdt.getText().toString());
        request.addMultipartParameter("br_country", "");
        request.addMultipartParameter("br_state", binding.stateEdt.getText().toString());
        request.addMultipartParameter("br_city", binding.cityEdt.getText().toString());
        request.addMultipartParameter("br_pincode", binding.pincodeEdt.getText().toString());

        Utility.Log("data", gson.toJson(request));

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
                        try {


                            if (response.getBoolean("status")) {


                                JSONObject jsonArray = response.getJSONObject("data");
                                is_completed = jsonArray.getString("is_completed");

                                alertDialogBuilder.setMessage(ResponseHandler.getString(response, "message"));
                                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        preafManager.loginStep(is_completed);
                                        if (is_completed.equals("2")) {
                                            getBrandList(ResponseHandler.getString(jsonArray, "brand_id"));
                                        }
                                    }
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
                        Utility.dismissLoadingTran();
                        if (error.getErrorCode() != 0) {
                            //Log.e("onError errorCode : ", String.valueOf(error.getErrorCode()));
                            //Log.e("onError errorBody : ", error.getErrorBody());
                            //Log.e("onError errorDetail : ", error.getErrorDetail());
                        } else {
                            //Log.e("onError errorDetail : ", error.getErrorDetail());
                        }
                    }
                });

    }

    private void getBrandList(String brand_id) {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("addbrandresponce", response);
                int toSetActiveBrand = 0;
                ArrayList<BrandListItem> brandListItems = new ArrayList<>();
                try {
                    JSONObject res = new JSONObject(response);

                    JSONArray jsonArray1 = res.getJSONArray("data");
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        BrandListItem brandListItemm = new BrandListItem();
                        brandListItemm.setId(ResponseHandler.getString(jsonObject, "id"));
                        if (brand_id.equals(brandListItemm.getId())) {
                            toSetActiveBrand = i;
                        }
                        brandListItemm.setCategoryId(ResponseHandler.getString(jsonObject, "br_category_id"));
                        brandListItemm.setCategoryName(ResponseHandler.getString(jsonObject, "br_category_name"));
                        brandListItemm.setName(ResponseHandler.getString(jsonObject, "br_name"));
                        brandListItemm.setPhonenumber(ResponseHandler.getString(jsonObject, "br_phone"));
                        brandListItemm.setWebsite(ResponseHandler.getString(jsonObject, "br_website"));
                        brandListItemm.setEmail(ResponseHandler.getString(jsonObject, "br_email"));
                        brandListItemm.setAddress(ResponseHandler.getString(jsonObject, "br_address"));
                        brandListItemm.setOriginalAddress(ResponseHandler.getString(jsonObject, "br_address"));


                        if (jsonObject.has("br_pincode")) {
                            brandListItemm.setPincode(ResponseHandler.getString(jsonObject, "br_pincode"));
                        }
                        if (jsonObject.has("br_state")) {
                            brandListItemm.setState(ResponseHandler.getString(jsonObject, "br_state"));
                        }
                        if (jsonObject.has("br_country")) {
                            brandListItemm.setCountry(ResponseHandler.getString(jsonObject, "br_country"));
                        }
                        if (jsonObject.has("br_city")) {
                            brandListItemm.setCity(ResponseHandler.getString(jsonObject, "br_city"));
                        }

                        String address = brandListItemm.getOriginalAddress();
                        if (brandListItemm.getCity() != null && !brandListItemm.getCity().isEmpty()) {
                            if (!address.isEmpty())
                                address = address + ", ";

                            address = address + brandListItemm.getCity();
                        }

                        if (brandListItemm.getState() != null && !brandListItemm.getState().isEmpty()) {
                            if (!address.isEmpty())
                                address = address + ", ";

                            address = address + brandListItemm.getState();
                        }


                        if (brandListItemm.getCountry() != null && !brandListItemm.getCountry().isEmpty()) {
                            if (!address.isEmpty())
                                address = address + ", ";

                            address = address + brandListItemm.getCountry();
                        }

                        if (brandListItemm.getPincode() != null && !brandListItemm.getPincode().isEmpty()) {
                            if (!address.isEmpty())
                                address = address + " - ";

                            address = address + brandListItemm.getPincode();
                        }
                        //brandListItemm.setAddress(getString(dataJsonObject, "br_address"));
                        brandListItemm.setAddress(address);

                        brandListItemm.setLogo(ResponseHandler.getString(jsonObject, "br_logo"));
                        brandListItemm.setIs_frame(ResponseHandler.getString(jsonObject, "is_frame"));
                        brandListItemm.setPackage_id(ResponseHandler.getString(jsonObject, "package_id"));
                        brandListItemm.setSubscriptionDate(ResponseHandler.getString(jsonObject, "subscription_date"));
                        brandListItemm.setFrame_message(ResponseHandler.getString(jsonObject, "frame_message"));
                        brandListItemm.setFrambaseyrl(ResponseHandler.getString(jsonObject, "fream_base_url"));
                        brandListItemm.setIs_payment_pending(ResponseHandler.getString(jsonObject, "is_payment_pending"));
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
                            frameItem.setFrame1(ResponseHandler.getString(jsonObject1, "frame_path"));
                            frameItem.setFrameId(ResponseHandler.getString(jsonObject1, "id"));

                            frameItems.add(frameItem);
                        }

                        brandListItemm.setFrame(frameItems);
                        brandListItems.add(brandListItemm);
                    }


                    preafManager.setAddBrandList(brandListItems);
                    preafManager.setIS_Brand(true);

                    if (brandListItems.size() != 0) {
                        preafManager.setActiveBrand(brandListItems.get(toSetActiveBrand));
                    }

                    //  MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.JUSTBRAND);


                    Intent i = new Intent(act, HomeActivity.class);

                    i.addCategory(Intent.CATEGORY_HOME);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    finish();

                    //  onBackPressed();

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
                        //Log.e("Error ", body);


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
                params.put("X-Authorization", "Bearer " + preafManager.getUserToken());
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

    private void getBrandCategory(int flag) {
        String apiUrl = "";
        int requestedMethod = 0;

        if (flag == BRAND_CATEGORY) {
            apiUrl = APIs.GET_BRAND_CATEGORY;
            requestedMethod = Request.Method.POST;
        }
        Utility.showLoadingTran(act);
        Utility.Log("API : ", apiUrl);

        StringRequest request = new StringRequest(requestedMethod, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();

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
                            listModel.setThumbnail(ResponseHandler.getString(itemObj, "thumbnail_url"));
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
                Utility.dismissLoadingTran();
                getCountryStateCity(CALL_STATE);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + preafManager.getUserToken());
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
//                stateList.clear();
//
//                binding.stateEdt.setText("");
//                binding.cityEdt.setText("");
//                selectedCity = null;
//                selectedState = null;
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
                binding.stateEdt.setText("");
                selectedState = null;


                binding.cityEdt.setText("");
                selectedCity = null;

                binding.cityLayout.setVisibility(View.GONE);
                binding.stateLayout.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
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

        if (flag == CALL_COUNTRY) {
            apiUrl = APIs.GET_COUNTRY;
            countryList.clear();
        }

        if (flag == CALL_STATE) {
            apiUrl = APIs.GET_STATE + "/101"; //+ selectedCountry.getId();
            stateList.clear();
        }

        if (flag == CALL_CITY) {
            apiUrl = APIs.GET_CITY + "/" + selectedState.getId();
            cityList.clear();
        }
        Utility.showLoadingTran(act);
        Utility.Log("API : ", apiUrl);
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.dismissLoadingTran();
                Utility.Log("GET_COUNTRY : ", response);
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
                            if (flag == CALL_COUNTRY) {
                                countryList.add(listModel);
                            }

                            if (flag == CALL_STATE) {
                                stateList.add(listModel);
                            }

                            if (flag == CALL_CITY) {
                                cityList.add(listModel);
                            }
                        }
//                        CommonListModel listModel = new CommonListModel();
//                        listModel.setLayoutType(CommonListModel.LAYOUT_BLOCK);
//                        listModel.setId("-1");
//                        listModel.setName("None");
//
//                        if (flag == CALL_COUNTRY && countryList.size() != 0) {
//                            countryList.add(0, listModel);
//                        }
//
//                        if (flag == CALL_STATE && stateList.size() != 0) {
//                            stateList.add(0, listModel);
//                        }
//
//                        if (flag == CALL_CITY && cityList.size() != 0) {
//                            cityList.add(0, listModel);
//                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Utility.dismissLoadingTran();
                isLoading = false;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + preafManager.getUserToken());
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


}