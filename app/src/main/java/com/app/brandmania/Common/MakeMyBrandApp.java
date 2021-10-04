package com.app.brandmania.Common;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MakeMyBrandApp extends MultiDexApplication {
    private static MakeMyBrandApp sInstance;

    private SharedPreferences sharedPreferences;
    private AppObserver observer;
    private RequestQueue mRequestQueue;


    public static MakeMyBrandApp getsInstance() {
        return sInstance;
    }


    public synchronized static MakeMyBrandApp getInstance() {
        return sInstance;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
//        // Enable verbose OneSignal logging to debug issues if needed.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//
//        // OneSignal Initialization
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(Constant.ONE_SIGNAL_APP_ID);
        MultiDex.install(this);

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        ApplicationLifeCycle.init(sInstance);

        observer = new AppObserver(getApplicationContext());
        mRequestQueue = Volley.newRequestQueue(sInstance);




        printHashKey();
    }

    private void printHashKey() {
        // Add code to print out the key hash
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }


    public AppObserver getObserver() {
        return observer;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, int tag) {
        req.setShouldCache(false);
        req.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(tag == 0 ? 111 : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private SharedPreferences getPreferences() {
        return sharedPreferences = getSharedPreferences("StoreCookie", MODE_PRIVATE);
    }

    public void saveCookie(String cookie) {
        if (cookie == null) {
            return;
        }

        SharedPreferences prefs = getPreferences();
        if (null == prefs) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cookie", cookie);
        editor.apply();
    }

    public String getCookie() {
        SharedPreferences prefs = getPreferences();
        return prefs.getString("cookie", "");
    }

    public void removeCookie() {
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("cookie");
        editor.apply();
    }



}