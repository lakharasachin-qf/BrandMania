package com.make.mybrand.Activity.basics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.make.mybrand.Common.Constant;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.R;
import com.make.mybrand.utils.Utility;
import com.make.mybrand.databinding.ActivityLoginBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    Activity act;
    private ActivityLoginBinding binding;
    private boolean isLoading = false;
    private ProgressDialog pDialog;
    PreafManager preafManager;
    String ContactNO;
    String referrerCode = "";
    private ActivityResultLauncher<String> requestPermissionLauncher;
        private void initFun(){
            FirebaseApp.initializeApp(/*context=*/ this);
            FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
            firebaseAppCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance());
        }
    @Override

    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_login);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        preafManager = new PreafManager(act);
        Utility.isLiveModeOff(act);
        referrerCode = getIntent().getStringExtra("referrerCode");

        initFun();

        String WELCOME = "Welcome<br>Back!</font></br>";
        String Message = "Don't have account?<font color='#ad2753'><b><u>SignUp</u></b></font>";
        setNotificationHandle();
        setNotificationPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.signupText.setText(Html.fromHtml(Message, Html.FROM_HTML_MODE_COMPACT));
            binding.welcome.setText(Html.fromHtml(WELCOME, Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.signupText.setText(Html.fromHtml(Message));
            binding.welcome.setText(Html.fromHtml(WELCOME));
        }
        binding.loginBtn.setOnClickListener(v -> {
            ContactNO = Objects.requireNonNull(binding.mobileNumber.getText()).toString();
            if (!binding.mobileNumber.getText().toString().equals("")) {
                if (ContactNO.length() < 10) {
                    binding.mobileNumber.setError("Enter Valid Mobile Number");
                    binding.mobileNumber.requestFocus();
                    return;
                } else {
                    Intent intent = new Intent(act, OtpScreenActivity.class);
                    intent.putExtra(Constant.MOBILE_NUMBER, binding.mobileNumber.getText().toString());
                    intent.putExtra("referrerCode", referrerCode);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    preafManager.setMobileNumber(binding.mobileNumber.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
            } else {
                String ContactNO = binding.mobileNumber.getText().toString();
                if (ContactNO.isEmpty()) {
                    binding.mobileNumber.setError("Enter Mobile Number");
                    binding.mobileNumber.requestFocus();
                    return;
                }
            }
        });
    }


    private void setNotificationHandle() {
        // Sets up permissions request launcher.
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), result -> {
                    if (result) {
                        HELPER.print("PERMISSION_RESULT", result.toString());
                    } else {
                        androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
                                .setTitle("Permission")
                                .setMessage(getString(R.string.notificationPermissionRequiredMsg))
                                .setPositiveButton("GO", (dialogInterface, i) -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts(
                                                "package",
                                                getPackageName(),
                                                null
                                        );
                                        intent.setData(uri);
                                        act.startActivityForResult(intent, 0);
                                    }
                                })
                                .setNeutralButton("LATER", (dialogInterface, i) -> {
                                })
                                .show();
                        AlertDialogBuilder.setCancelable(false);
                    }
                });
    }

    private void setNotificationPermission() {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            //showDummyNotification()
            HELPER.print("POST_NOTIFICATIONS", "DONE");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}