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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.R;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Utils.CodeReUse;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static Dialog noconnectionAlertDialog;
    Activity act;
    PreafManager prefManager;
    MakeMyBrandApp studentApp;
    private BroadcastReceiver mNetworkReceiver;
    /*public FirebaseAuth mAuth;*/
    private ResponseHandler responseHandler;
    private boolean isLoading = false;
    Gson gson;
    private static void showNoConnectionDialog() {
        if (!noconnectionAlertDialog.isShowing()) {
            noconnectionAlertDialog.setContentView(R.layout.dialog_no_internet_connection);
            noconnectionAlertDialog.setCancelable(false);
            noconnectionAlertDialog.show();
        }
    }
    public static void InternetError(boolean value) {
        if (value) {
            if (noconnectionAlertDialog.isShowing()) {
                noconnectionAlertDialog.dismiss();

            }
        } else {
            showNoConnectionDialog();

        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
// In Activity's onCreate() for instance
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

     //   getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        prefManager = new PreafManager(this);
        // responseHandler = new ResponseHandler();
      //  profileModel = prefManager.getUsers();
        gson = new Gson();
        /*profileObject = new StoreUserData().getUsers(this);
        prefManager = new PrefManager(this);
        responseHandler = new ResponseHandler(this);*/





        noconnectionAlertDialog = new Dialog(this);
        noconnectionAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();

       /* if(prefManager.getUserToken() != null)
            updateUser();*/

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
        Map<String, String> headers = new HashMap<>();
        if (flag == CodeReUse.GET_JSON_HEADER) {
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
        } else {
            headers.put("Accept", "application/x-www-form-urlencoded");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        }

        if (prefManager.getUserToken() != null) {
            headers.put("Authorization","Bearer"+prefManager.getUserToken());
        }
        return headers;
    }
    private void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    @Override public void onResume() {
        super.onResume();
    }
    @Override public void update(Observable observable, Object data) {

    }




}
