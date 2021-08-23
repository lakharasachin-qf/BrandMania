package com.app.brandmania.Activity.brand;

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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import com.app.brandmania.Activity.basics.LoginActivity;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.CustomFragment;
import com.app.brandmania.Fragment.bottom.ListBottomFragment;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.ItemSelectionInterface;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityAddBranddBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddBranddActivity extends BaseActivity implements ItemSelectionInterface, alertListenerCallback, PopupMenu.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    Activity act;
    private ActivityAddBranddBinding binding;
    public static int BRAND_CATEGORY = 0;
    private String BrandTitle;
    CommonListModel commonListModel;
    private boolean iscutomEnable = false;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    ArrayList<CommonListModel> BRANDTypeList = new ArrayList<>();
    PreafManager preafManager;
    private boolean isLoading = false;
    private String is_completed = "";
    private Bitmap selectedLogo;
    private ListBottomFragment bottomSheetFragment;

    private boolean isEditModeEnable = false;
    AlertDialog.Builder alertDialogBuilder;
    private ImageView menuOtpion;
    private Uri mCropImageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;

        binding = DataBindingUtil.setContentView(act, R.layout.activity_add_brandd);
        preafManager = new PreafManager(this);
        preafManager.setIs_Registration(true);
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

        binding.navigation.setOnNavigationItemSelectedListener(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        binding.websiteEdt.setText("https://");
        alertDialogBuilder = new AlertDialog.Builder(act);
        String NumberShow = getIntent().getStringExtra(Constant.MOBILE_NUMBER);
        String EmailIdShow = getIntent().getStringExtra(Constant.EMAIL_ID);
        binding.phoneTxt.setText(preafManager.getMobileNumber());
        binding.emailIdEdt.setText(preafManager.getEMAIL_Id());
        menuOtpion=findViewById(R.id.menuOtpion);
        menuOtpion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(AddBranddActivity.this, view);
                popup.setOnMenuItemClickListener(AddBranddActivity.this);
                popup.inflate(R.menu.menu);
                popup.show();
            }
        });
        //LoginFlow();
        binding.addExpenceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
        getBrandCategory(BRAND_CATEGORY);
        CodeReUse.RemoveError(binding.categoryEdt, binding.categoryEdtLayout);
        CodeReUse.RemoveError(binding.nameTxt, binding.nameTxtLayout);
        CodeReUse.RemoveError(binding.phoneTxt, binding.phoneTxtLayout);
        CodeReUse.RemoveError(binding.addressEdt, binding.addressEdtLayout);
        CodeReUse.RemoveError(binding.websiteEdt, binding.websiteEdtLayout);
        CodeReUse.RemoveError(binding.emailIdEdt, binding.emailIdEdtLayout);
        CodeReUse.RemoveError(binding.businessServiceEdt,binding.businessFacilityEdtLayout);
        binding.categoryEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFragmentList(BRAND_CATEGORY, BrandTitle, BRANDTypeList);
            }
        });
        binding.viewImgFirst.setTag("0");


        binding.imgCardFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                onSelectImageClick(v);

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
    public void askPermissions(){
        ActivityCompat.requestPermissions(act,
                new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE},
                1110);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting=false;
        if (requestCode == 1110){

            boolean readStorageGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow access to the permissions", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{ READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, requestCode);
                        }
                    });
                }else {
                    targetSetting=true;
                }
            }
        }
        if (targetSetting){
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
    //For CustomFrame
    public void onSelectImageClick(View view) {
        //CropImage.startPickImageActivity(this);
        selectImageFromGallery();
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
        // handle result of CropImageActivity
        if (requestCode == GALLERY_INTENT) {
            Uri result = data.getData();
            if (resultCode == RESULT_OK) {
                startCropImageActivity(result);
//                binding.viewImgFirst.setVisibility(View.VISIBLE);
//                binding.imgEmptyStateFirst.setVisibility(View.GONE);
//                binding.actionDeleteFirst.setVisibility(View.VISIBLE);
//                ((ImageView) findViewById(R.id.viewImgFirst)).setImageURI(result);
//                ImageView imageView = ((ImageView) findViewById(R.id.viewImgFirst));
//                selectedLogo = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

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
            binding.scrollView.scrollTo(0,binding.categoryEdt.getBottom());

        }
        if (binding.nameTxt.getText().toString().trim().length() == 0) {
            isError = true;

            binding.nameTxtLayout.setError(getString(R.string.brandname_text));
            binding.nameTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            if (!isFocus) {
                binding.nameTxt.requestFocus();
                isFocus = true;
                binding.scrollView.scrollTo(0,binding.nameTxt.getBottom());
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
                    binding.scrollView.scrollTo(0,binding.emailIdEdt.getBottom());
                }

            }
            else
            {

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
                    binding.scrollView.scrollTo(0,binding.phoneTxt.getBottom());
                }
                return;
            }

        } else {
            if (binding.phoneTxt.getText().toString().trim().equals("")) {
                binding.phoneTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.phoneTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                isError = true;
                if (!isFocus) {
                    binding.phoneTxt.requestFocus();
                    isFocus = true;
                    binding.scrollView.scrollTo(0,binding.phoneTxt.getBottom());
                }
                return;
            }

        }
        if (!isError) {
            Bitmap bitmap = null;

            if (selectedLogo != null) {
                bitmap = selectedLogo;
            }

            addBrand(bitmap);
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
    private void addBrand(Bitmap img) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Log.e("API", APIs.ADD_BRAND);
        Log.e("API", preafManager.getUserToken());
        File img1File = null;
        if (img != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "photo.jpeg", img);
        }


        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.ADD_BRAND)
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer" + preafManager.getUserToken())
                .addMultipartParameter("br_category", commonListModel.getId())
                .addMultipartParameter("br_name", binding.nameTxt.getText().toString())
                .addMultipartParameter("br_phone", binding.phoneTxt.getText().toString())
                .addMultipartParameter("br_address", binding.addressEdt.getText().toString())
                .addMultipartParameter("br_website", binding.websiteEdt.getText().toString())
                .addMultipartParameter("br_email", binding.emailIdEdt.getText().toString())
                .addMultipartParameter("br_service",binding.businessServiceEdt.getText().toString())
                .setTag("Add User")
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("br_logo", img1File);
            Log.e("br_logo", String.valueOf(img1File));
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
                        isLoading = false;
                        Utility.dismissProgress();
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
                                            getBrandList();

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
    @Override public void alertListenerClick() {
        requestAgain();
    }
    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                CodeReUse.ASK_PERMISSSION);
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
                        brandListItemm.setSubscriptionDate(ResponseHandler.getString(jsonObject,"subscription_date"));
                        brandListItemm.setFrame_message(ResponseHandler.getString(jsonObject, "frame_message"));
                        brandListItemm.setFrambaseyrl(ResponseHandler.getString(jsonObject, "fream_base_url"));
                        brandListItemm.setIs_payment_pending(ResponseHandler.getString(jsonObject, "is_payment_pending"));
                        brandListItemm.setPayment_message(ResponseHandler.getString(jsonObject, "payment_message"));
                        brandListItemm.setPackagename(ResponseHandler.getString(jsonObject, "package"));
                        brandListItemm.setRate(ResponseHandler.getString(jsonObject, "rate"));
                        brandListItemm.setPackagemessage(ResponseHandler.getString(jsonObject, "package_message"));
                        brandListItemm.setNo_of_total_image(ResponseHandler.getString(jsonObject, "no_of_img"));
                        brandListItemm.setNo_of_used_image(ResponseHandler.getString(jsonObject, "no_of_used_img"));
                        brandListItemm.setNo_of_frame(ResponseHandler.getString(jsonObject, "no_of_frame"));
                        brandListItemm.setNo_of_remaining(ResponseHandler.getString(jsonObject, "remaining_img"));
                        brandListItemm.setExpiery_date(ResponseHandler.getString(jsonObject, "expire_date"));


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

    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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


    //For Fragment
@Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    Fragment fragment = null;
    switch (menuItem.getItemId()) {
        case R.id.navigation_home:
            addbrandAlertBox();
            break;

        case R.id.navigation_custom:

          //  if (iscutomEnable)
           //{
                binding.fragmentContainer.setVisibility(View.VISIBLE);
                binding.scrollView.setVisibility(View.GONE);
               // binding.my_toolbar.setVisibility(View.GONE);
                binding.myToolbar.setVisibility(View.GONE);
                binding.backImageLogo.setVisibility(View.GONE);
                fragment = new CustomFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            break;

        case R.id.navigation_download:
            addbrandAlertBox();
            break;

        case R.id.navigation_profile:
            addbrandAlertBox();
            break;
    }
//        if (iscutomEnable)
//        {
//         return false;
//        }
    return loadFragment(fragment);
}

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    public void addbrandAlertBox(){
        alertDialogBuilder = new AlertDialog.Builder(act);
        alertDialogBuilder.setTitle("Add Barand");
        alertDialogBuilder.setMessage("Please Add Your Brand");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i = new Intent(act,AddBranddActivity.class);
                        startActivity(i);
                        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

       AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }






}