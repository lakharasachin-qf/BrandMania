package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.bottom.PickerFragment;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityAddReportAndBugBinding;

import org.json.JSONObject;

import java.io.File;

public class AddReportAndBug extends AppCompatActivity {
    Activity act;
    private ActivityAddReportAndBugBinding binding;
    private boolean isLoading = false;
    PreafManager preafManager;
    private Bitmap selectedImageFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
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
                if (selectedImageFirst != null)
                    pickerView(true, selectedImageFirst);
                else
                    pickerView(false, null);
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
        if (binding.emailEdt.getText().toString().length() == 0) {
            isError = true;
            isFocus = true;
            binding.emailTxtLayout.setError(getString(R.string.enter_email_id));
            binding.emailEdt.requestFocus();
            binding.scrollView.smoothScrollTo(0, binding.emailEdt.getTop());
        }
        if (!CodeReUse.isEmailValid(binding.emailEdt.getText().toString())) {
            isError = true;
            isFocus = true;
            binding.emailTxtLayout.setError(getString(R.string.enter_valid_email_address));
            binding.emailEdt.requestFocus();
            binding.scrollView.smoothScrollTo(0, binding.emailEdt.getTop());
        }

        if (binding.bugsEdt.getText().toString().length() == 0) {
            isError = true;
            binding.bugsLayout.setError(getString(R.string.enter_problem));
            if (!isFocus) {
                binding.scrollView.smoothScrollTo(0, binding.bugsEdt.getHeight());
                binding.bugsEdt.requestFocus();
            }
        }

        Bitmap bitmap1 = null;
        if (binding.viewImgFirst.getTag().toString().equals("1")) {
            bitmap1 = ((BitmapDrawable) binding.viewImgFirst.getDrawable()).getBitmap();
        } else {
            isError = true;
            CodeReUse.showSnackBar(act, binding.rootBackground, getString(R.string.please_attech_one_screeshot));
        }

        if (!isError)
            addReport(bitmap1);

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
                            Utility.showAlert(act, "Thank you , We will fix it soon", "backpress");

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
    private void pickerView(boolean viewMode, Bitmap selectedBitmap) {
        PickerFragment pickerFragment = new PickerFragment(act);
        pickerFragment.setEnableViewMode(viewMode);
        pickerFragment.setActionId(Constant.PICKER_FIRST);

        if (viewMode) {
            if (selectedBitmap != null)
                pickerFragment.setSelectedBitmapForFullView(selectedBitmap);
        }
        PickerFragment.HandlerImageLoad imageLoad = new PickerFragment.HandlerImageLoad() {
            @Override
            public void onGalleryResult(int flag, Bitmap bitmap) {
                if (flag == Constant.PICKER_FIRST) {
                    binding.viewImgFirst.setImageBitmap(bitmap);
                    binding.imgEmptyStateFirst.setVisibility(View.GONE);
                    binding.actionDeleteFirst.setVisibility(View.VISIBLE);
                    selectedImageFirst = bitmap;
                    binding.viewImgFirst.setTag("1");
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
    public void captureScreenShort()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}