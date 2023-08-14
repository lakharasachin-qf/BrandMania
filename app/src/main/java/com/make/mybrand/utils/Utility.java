package com.make.mybrand.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.make.mybrand.BuildConfig;
import com.make.mybrand.Common.PreafManager;
import com.make.mybrand.Interface.alertListenerCallback;
import com.make.mybrand.Model.BrandListItem;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DetailImageviewBinding;
import com.make.mybrand.databinding.DialogImageViewLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utility {
    public static Dialog dialog;
    private static Dialog progressDialog;

    public static void Log(String act, Object msg) {
     Log.e(act, msg + "");
    }

    public static void showLoadingTran(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.colorProgressBackground);
        dialog.setContentView(R.layout.progress_bar_layout);
        dialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void isLiveModeOff(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }


    public static boolean oneTimeCodeExecutes(Activity act) {

        try {
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = formatter.format(date);
            //new PreafManager(act).isOneTimeLoad(currentDate);
            String expireDate = new PreafManager(act).getOneTimeLoad();
            Log.e("expired Date", expireDate);
            //String dates = "30/01/2022";
            if (!expireDate.equals(currentDate)) {
                Log.e("inner Date", expireDate);
                new PreafManager(act).isOneTimeLoad(currentDate);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean oneTimeCodeExecute(Activity act) {

        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(date);
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.e("currentDay", String.valueOf(currentDay));
        SharedPreferences settings = act.getSharedPreferences("makemybrand", 0);
        int lastDay = settings.getInt("day", 0);
        //int lastDay = new PreafManager(act).getOneTimeLoad();
        if (lastDay != currentDay) {
            // new PreafManager(act).isOneTimeLoad(currentDay);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("day", currentDay);
            editor.commit();
            return true;
        }
        return false;
    }

    public static boolean isPackageExpired(Activity act) {
        if (new PreafManager(act).getActiveBrand().getExpiery_date() != null && !new PreafManager(act).getActiveBrand().getExpiery_date().isEmpty()) {
            try {
                String expireDate = new PreafManager(act).getActiveBrand().getExpiery_date().replace('-', '/');

                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = formatter.format(date);

                Date convertedExpireDate = formatter.parse(expireDate);
                Date convertedCurrentDate = formatter.parse(currentDate);

                if (convertedExpireDate.compareTo(convertedCurrentDate) < 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static boolean isPackageExpired(BrandListItem brandListItem) {
        if (brandListItem.getExpiery_date() != null && !brandListItem.getExpiery_date().isEmpty()) {

            try {

                String expireDate = brandListItem.getExpiery_date().replace('-', '/');
                Date date = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = formatter.format(date);

                Date convertedExpireDate = formatter.parse(expireDate);
                Date convertedCurrentDate = formatter.parse(currentDate);


                if (convertedExpireDate.compareTo(convertedCurrentDate) < 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isNewUser(Activity act) {
        try {
            String expireDate = new PreafManager(act).getActiveBrand().getSubscriptionDate().replace('-', '/');
            Date date = new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = formatter.format(date);

            Date convertedExpireDate = formatter.parse(expireDate);
            Date convertedCurrentDate = formatter.parse(currentDate);


            if (convertedExpireDate.compareTo(convertedCurrentDate) < 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static int monthsBetweenDates(String subscriptionDateStr) {
        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        String currDateStr = fmt.format(c);
        try {
            Date subscriptionDate = fmt.parse(subscriptionDateStr);
            Date currentDate = fmt.parse(currDateStr);

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(subscriptionDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(currentDate);
            int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
            if ((diffYear == 0) && (diffMonth == 0)) {
                return diffMonth;
            } else {
                return 1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 1;
    }


    //return true if user is paid
    public static boolean isUserPaid(BrandListItem activeBrand) {
        if (!activeBrand.getPackagename().isEmpty() && activeBrand.getIs_payment_pending().equalsIgnoreCase("0")) {
            return true;
        } else
            return false;
    }


    public static void dismissLoadingTran() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void setItalicText(TextView textView, boolean italic) {
        if (italic) {
            if (textView.getTypeface() != null && textView.getTypeface().isBold())
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
            else
                textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
        } else {
            if (textView.getTypeface() != null && textView.getTypeface().isBold() && textView.getTypeface().isItalic())
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            else
                textView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public static void setUnderlineText(TextView textView, boolean underline) {
        if (underline) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            textView.setPaintFlags(0);
        }
    }

    public static void setBold(TextView textView, boolean bold) {

        if (bold) {
            if (textView.getTypeface() != null && textView.getTypeface().isItalic())
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
            else
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        } else {
            if (textView.getTypeface() != null && textView.getTypeface().isBold() && textView.getTypeface().isItalic())
                textView.setTypeface(textView.getTypeface(), Typeface.ITALIC);
            else
                textView.setTypeface(null, Typeface.NORMAL);
        }
    }

    public static void RemoveError(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    public static void loadImageOnURI(Activity act, ImageView imageView, Uri uri) {
        Glide.with(act)
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(1200, 1000)
                .into(imageView);
    }

    public static void dismissProgress() {
        try {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void sendMail(Activity act, String emailId, String contactNumber) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "lakharasachin.qf@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "iSmart Homes Android app support");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "" +
                "===========================\n" +
                "App Details\n" +
                "\n" +
                "App Version : " + BuildConfig.VERSION_NAME + "\n" +
                "Android Version : " + Build.VERSION.SDK_INT + "\n" +
                "Android Device : " + Build.BRAND + "\n" +
                "Android Device Model : " + Build.MODEL + "\n" +
                "Email : " + emailId + "\n" +
                "Mobile : " + contactNumber + "\n" +
                "===========================\n"
        );
        act.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }


    public static void showProgress(Activity act) {

        if (progressDialog != null && progressDialog.isShowing())
            return;

        progressDialog = new Dialog(act);
        progressDialog.setContentView(R.layout.progress_bar_layout);
        progressDialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (progressDialog != null && !progressDialog.isShowing())
                        progressDialog.show();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showAlert(Activity act, String msg) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //((alertListenerCallback) act).alertListenerClick();
                    }
                })
                .show();
    }
    public static void error(Activity act, String msg) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                     }
                })
                .show();
    }


    public static void showAlert(Activity act, String msg, String flag) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        act.onBackPressed();
                        ((alertListenerCallback) act).alertListenerClick();
                    }
                })
                .show();
    }


    public static void fullScreenImageViewer(Activity act, String imageUrl) {
        Log("Image url", imageUrl + "s");
        DetailImageviewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.detail_imageview, null, false);
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(binding.getRoot());
        binding.CloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Glide.with(act).load(imageUrl).error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder).into(binding.fullImageViewer);
        alertDialog.show();

    }

    public static void showAlertNew(Activity act, String msg) {
        new AlertDialog.Builder(act)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        act.onBackPressed();
                    }
                })
                .show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showLoading(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;

        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.colorProgressOverlay);
        dialog.setContentView(R.layout.dialog_loader);
        dialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void dismissLoading() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static boolean compareDate(String endDate) {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat fmt = new SimpleDateFormat("dd-mm-yyyy");
        String currtDate = fmt.format(c);
        try {

            Date currentDate = fmt.parse(currtDate);
            Date meetingDate = fmt.parse(endDate);
            if (currentDate.compareTo(meetingDate) > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void rateApp(Activity act) {
        Uri uri = Uri.parse("market://details?id=com.app.bespoke");// + act.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            act.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            act.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.app.bespoke")));//+ act.getPackageName())));
        }
    }

    private static OnImageViewDismiss viewDismiss;

    public static void fullScreenImageViewerBitmap(Activity act, Bitmap imageUrl, OnImageViewDismiss viewDi) {
        viewDismiss = viewDi;
        DialogImageViewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(act),
                R.layout.dialog_image_view_layout, null, false);
        androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(binding.getRoot());

        binding.CloseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

//        binding.fullImageViewer3.setImageBitmap(imageUrl);
//        binding.fullImageViewer3.setVisibility(View.VISIBLE);
//        binding.fullImageViewer.setVisibility(View.GONE);
//
//        binding.fullImageViewer3.setVisibility(View.VISIBLE);
//        binding.fullImageViewer.setVisibility(View.GONE);


        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                viewDismiss.onPhotoDialogDismiss();
            }
        });
    }

    public interface OnImageViewDismiss {
        void onPhotoDialogDismiss();
    }

    public static void showSnackBar(View view, Activity act, String message) {
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

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public static String convertFirstUpper(String str) {

        if (str == null || str.isEmpty()) {
            return str;
        }
        Utility.Log("FirstLetter", str.substring(0, 1) + "    " + str.substring(1));
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
