package com.app.brandmania.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.app.brandmania.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeReUse {
    public static final int GET_FORM_HEADER = 0;
    public static final int GET_JSON_HEADER = 1;
    public static final int GALLERY_INTENT = 2;
    public static final int CAMERA_INTENT = 3;
    public static final int PICK_FIRST = 1;
    public static final int PICK_SECOND = 2;
    public static final int ASK_PERMISSSION = 103;
    public static final int SELECT_VIDEO_CAMERA = 105;
    public static final int SELECT_VIDEO_GALLERY = 104;
    public static final int PDF_INTENT = 106;
    public static final String CHANNEL_ID = "brand360_chanel";
    public static final String CHANNEL_NAME = "Bran360App";
    public static final String CHANNEL_DESCRIPTION = "com.make.mybrand";
    public static void activityBackPress(Activity act) {
        act.finish();
        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWhiteNavigationBar(@NonNull Dialog dialog, Activity act) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.lightGraycolor));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static File createFileFromBitmap(Activity act, String fileName, Bitmap bitmap) {
        File file;
        file = new File(act.getCacheDir(), System.currentTimeMillis() + fileName);
        try {
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isContactValid(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        } else if (mobileNumber.isEmpty()) {
            return false;
        } else if (mobileNumber.length() < 10) {
            return false;
        } else return mobileNumber.length() <= 10;
    }

    public static void RemoveError(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                editText.setError(null);
                textInputLayout.setError(null);
                textInputLayout.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
    public static void showSnackBar(Activity act, View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(act, R.color.colorNavText));
        snackbar.show();
    }

}
