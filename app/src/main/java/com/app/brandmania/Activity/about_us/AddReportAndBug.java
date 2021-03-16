package com.app.brandmania.Activity.about_us;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.Interface.alertListenerCallback;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityAddReportAndBugBinding;

import org.json.JSONObject;

import java.io.File;

public class AddReportAndBug extends BaseActivity implements alertListenerCallback {

    Activity act;

    private ActivityAddReportAndBugBinding binding;
    private boolean isLoading = false;

    PreafManager preafManager;

    private Bitmap selectedImagesBitmap;
    private boolean isEditModeEnable = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding= DataBindingUtil.setContentView(act,R.layout.activity_add_report_and_bug);
        preafManager=new PreafManager(act);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.viewImgFirst.setTag("0");
        binding.imgCardFirst.setOnClickListener(new View.OnClickListener() {
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
        CodeReUse.RemoveError(binding.emailEdt, binding.emailTxtLayout);
        CodeReUse.RemoveError(binding.contactEdt, binding.contactTxtLayout);
        CodeReUse.RemoveError(binding.bugsEdt, binding.bugsLayout);
        binding.addReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });


    }
    private void validateData() {
        boolean isError = false;
        boolean isFocus = false;

        if (binding.contactEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.contactTxtLayout.setError(getString(R.string.enter_contact_number));
            binding.contactEdt.requestFocus();
            binding.scrollView.smoothScrollTo(0, binding.contactEdt.getTop());
        }
        if (!CodeReUse.isContactValid(binding.contactEdt.getText().toString())) {
            isError = true;
            isFocus = true;
            binding.contactTxtLayout.setError(getString(R.string.enter_valid_contact_number));
            binding.contactEdt.requestFocus();
            binding.scrollView.smoothScrollTo(0, binding.contactEdt.getTop());
        }
        if (!binding.emailEdt.getText().toString().equals("")) {
            if (!CodeReUse.isEmailValid(binding.emailEdt.getText().toString())) {
                isError = true;
                isFocus = true;
                binding.emailTxtLayout.setError(getString(R.string.enter_valid_email_address));
                binding.emailTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.emailEdt.requestFocus();

            }
            else
            {

            }

        }

        if (binding.bugsEdt.getText().toString().length() == 0) {
            isError = true;
            binding.bugsLayout.setError(getString(R.string.enter_problem));
            if (!isFocus) {
                binding.scrollView.smoothScrollTo(0, binding.bugsEdt.getHeight());
                binding.bugsEdt.requestFocus();
            }
        }



        if (!binding.contactEdt.getText().toString().equals("")) {
            if (binding.contactEdt.getText().toString().length() < 10) {
                binding.contactTxtLayout.setError(getString(R.string.validphoneno_txt));
                binding.contactTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.contactEdt.requestFocus();
                return;
            }

        } else {
            if (binding.contactEdt.getText().toString().equals("")) {
                binding.contactTxtLayout.setError(getString(R.string.entermobileno_text));
                binding.contactTxtLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                binding.contactEdt.requestFocus();
                return;
            }
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

            addReport(bitmap1);
        }

    }
    private void addReport(Bitmap img1) {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        File img1File = null;
        if (img1 != null) {
            img1File = CodeReUse.createFileFromBitmap(act, "reportBugs.jpeg", img1);
        }

        Log.e("API", APIs.REPORT_BUG);
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(APIs.REPORT_BUG)
                .addHeaders("Accept", "application/x-www-form-urlencoded")
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addHeaders("token", preafManager.getUserToken())
                .addMultipartParameter("contact_no", binding.contactEdt.getText().toString())
                .addMultipartParameter("email", binding.emailEdt.getText().toString())
                .addMultipartParameter("problem", binding.bugsEdt.getText().toString())
                .setTag("Add Report Bugs")
                .setPriority(Priority.HIGH);

        if (img1File != null) {
            request.addMultipartFile("avatar", img1File);

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
                        Log.e("Response", response.toString());
                        if (ResponseHandler.isSuccess(null, response)) {
                       //   Toast.makeText(act, "T",Toast.LENGTH_LONG).show();

                            Utility.showAlert(act, ResponseHandler.getString(ResponseHandler.createJsonObject(response.toString()), "message"), "Error");

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading = false;
                        Utility.dismissProgress();
                        error.printStackTrace();
                    }
                });

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
    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
    @Override public void alertListenerClick() {
        onBackPressed();
    }
}