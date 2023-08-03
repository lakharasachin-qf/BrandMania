package com.app.brandmania.Connection;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static Dialog noconnectionAlertDialog;
    public Activity act;
    public static Activity staticAct;
    public PreafManager prefManager;
    public MakeMyBrandApp myBrandApp;
    private BroadcastReceiver mNetworkReceiver;
    private boolean LIVE_MODE = true;
    public Gson gson;

    public BaseActivity() {
    }

    private static void showNoConnectionDialog() {
        if (!noconnectionAlertDialog.isShowing()) {
            noconnectionAlertDialog.setContentView(R.layout.dialog_no_internet_connection);
            noconnectionAlertDialog.setCancelable(false);
            noconnectionAlertDialog.show();
        }
    }

    public static void InternetError(boolean value) {
        if (!staticAct.isDestroyed() && !staticAct.isFinishing()) {
            if (value) {
                if (noconnectionAlertDialog.isShowing()) {
                    noconnectionAlertDialog.dismiss();
                }
            } else {
                showNoConnectionDialog();
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staticAct = this;
        act = this;
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        prefManager = new PreafManager(this);
        gson = new Gson();
        myBrandApp = (MakeMyBrandApp) this.getApplication();
        myBrandApp.getObserver().addObserver(this);
        noconnectionAlertDialog = new Dialog(this);
        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

        if (LIVE_MODE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected Map<String, String> getHeader(int flag) {
        return MakeMyBrandApp.getInstance().getHeader(flag);
//        Map<String, String> headers = new HashMap<>();
//        if (flag == CodeReUse.GET_JSON_HEADER) {
//            headers.put("Accept", "application/json");
//            headers.put("Content-Type", "application/json");
//        } else {
//            headers.put("Accept", "application/x-www-form-urlencoded");
//            headers.put("Content-Type", "application/x-www-form-urlencoded");
//        }
//
//        if (prefManager.getUserToken() != null) {
//            headers.put("X-Authorization", "Bearer " + prefManager.getUserToken());
//        }
//        return headers;
    }

    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void hideKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(act);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboard(innerView);
            }
        }
    }

    public void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
