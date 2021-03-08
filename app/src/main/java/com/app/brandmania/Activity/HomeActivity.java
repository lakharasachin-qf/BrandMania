package com.app.brandmania.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.VersionListIItem;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Fragment.bottom.CustomFragment;
import com.app.brandmania.Fragment.bottom.DownloadsFragment;
import com.app.brandmania.Fragment.bottom.HomeFragment;
import com.app.brandmania.Fragment.bottom.ProfileFragment;
import com.app.brandmania.R;
import com.app.brandmania.Utils.CodeReUse;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Timer;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    VersionListIItem versionListIItem;
    PreafManager preafManager;
    private AppUpdateManager appUpdateManager;
    private Task<AppUpdateInfo> appUpdateInfoTask;
    private Activity act;
    AlertDialog.Builder alertDialogBuilder;
    private boolean iscutomEnable = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FetchCustomeFrameStatus();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager=new PreafManager(this);
        act=this;
        getUpadte();

        checkForUpdates();
        loadFragment(new HomeFragment());
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }
    @SuppressLint("NonConstantResourceId")
    @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_custom:

                if (iscutomEnable)
                {
                    fragment = new CustomFragment();
//                Intent intent=new Intent(getApplicationContext(),CustomViewAllActivit.class);
//                startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                else
                {
                    Intent intent=new Intent(getApplicationContext(),CustomViewAllActivit.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                }
                break;

                case R.id.navigation_download:
                fragment = new DownloadsFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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


    //app updates
    private void checkForUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(act);
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        // For a flexible update, use AppUpdateType.FLEXIBLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    startAppUpdates(appUpdateInfo);
                }
            }
        });
    }

    private void startAppUpdates(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    IMMEDIATE,
                    act,
                    Constant.APP_UPDATES);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.APP_UPDATES) {
            if (resultCode == RESULT_CANCELED) {
                checkForUpdates();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appUpdateManager != null) {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(
                            new OnSuccessListener<AppUpdateInfo>() {
                                @Override
                                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                    if (appUpdateInfo.updateAvailability()
                                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                        // If an in-app update is already running, resume the update.
                                        try {
                                            appUpdateManager.startUpdateFlowForResult(
                                                    appUpdateInfo,
                                                    IMMEDIATE,
                                                    act,
                                                    Constant.APP_UPDATES);
                                        } catch (IntentSender.SendIntentException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setWhiteNavigationBar( Activity act) {
        Window window = getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.Graycolor));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
        }
    }
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        //  a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }
    public void captureScreenShort() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
    private void getUpadte() {

        Utility.Log("API : ", APIs.GET_UPDATE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_UPDATE,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("GET_UPDATE : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    versionListIItem=new VersionListIItem();
                    versionListIItem.setId(jsonArray1.getString("id"));
                    versionListIItem.setAppliactionVersion(jsonArray1.getString("application_version"));
                    versionListIItem.setMessage(jsonArray1.getString("message"));
                    versionListIItem.setForcefullyUpdate(jsonArray1.getString("forcefully_update"));
                    versionListIItem.setIsNew(jsonArray1.getString("is_new"));
                    versionListIItem.setCreatedAt(jsonArray1.getString("created_at"));
                    versionListIItem.setUpdatedAt(jsonArray1.getString("updated_at"));
                    versionListIItem.setDeletedAt(jsonArray1.getString("deleted_at"));


//                    int apiVERSION=Integer.parseInt(versionListIItem.getAppliactionVersion().replace(".",""));
//                    int currentVERSION=Integer.parseInt(String.valueOf(Constant.F_VERSION).replace(".",""));
//
//                    if (apiVERSION>currentVERSION)
//                    {
//                        // Create an alert builder
//                        AlertDialog.Builder builder = new AlertDialog.Builder(act);
//                        // set the custom layout
//                        final View customLayout = getLayoutInflater().inflate(R.layout.frame_alert_box, null);
//                        TextView Message=customLayout.findViewById(R.id.messageMaessage);
//                        ImageView CloseImg=customLayout.findViewById(R.id.CloseImg);
//                        Message.setText(jsonArray1.getString("message"));
//
//                        builder.setView(customLayout);
//
//
//                        // create and show
//                        // the alert dialog
//                        AlertDialog dialog
//                                = builder.create();
//                        dialog.getWindow().setBackgroundDrawableResource(R.color.colorNavText);
//                        dialog.setCancelable(false);
//                        dialog.show();
//                        CloseImg.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//
//                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                        pbutton.setBackgroundColor(Color.WHITE);
//                    }

                    int apiVERSION=Integer.parseInt(versionListIItem.getAppliactionVersion().replace(".",""));
                    int currentVERSION=Integer.parseInt(String.valueOf(Constant.F_VERSION).replace(".",""));

                    if (apiVERSION>currentVERSION)
                    {
                        // Create an alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(act);
                        // set the custom layout
                        final View customLayout = getLayoutInflater().inflate(R.layout.frame_alert_box, null);
                        ImageView cloasedBox=customLayout.findViewById(R.id.CloseImg);
                        WebView webView=customLayout.findViewById(R.id.webView);
                        TextView updateTitle=customLayout.findViewById(R.id.updateTitle);
                        Button updateBtn=customLayout.findViewById(R.id.updateBtn);
                        webView.loadData(jsonArray1.getString("message"), "text/html; charset=utf-8", "utf-8");
                        webView.setBackgroundColor(Color.TRANSPARENT);
                        String htmlString="<u>App Update</u>";
                        updateTitle.setText(Html.fromHtml(htmlString));
                        builder.setView(customLayout);

                        if (versionListIItem.getForcefullyUpdate().equalsIgnoreCase("1")){
                            cloasedBox.setVisibility(View.GONE);
                        }

                        // create and show
                        // the alert dialog
                        AlertDialog dialog
                                = builder.create();
                        dialog.getWindow().setBackgroundDrawableResource(R.color.colorNavText);
                        dialog.setCancelable(false);
                        dialog.show();
                        cloasedBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        updateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.make.mybrand");
                                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                                try {

                                    startActivity(intent);
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        });

                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setBackgroundColor(Color.WHITE);
                  }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
//                        String body;
//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    private void FetchCustomeFrameStatus() {

        Utility.Log("API : ", APIs.FETCH_CUSTOME_FRAME_STATUS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.FETCH_CUSTOME_FRAME_STATUS,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("FETCH_CUSTOME_FRAME_STATUS : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                  //  JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    if (ResponseHandler.getBool(jsonObject,"status")){
                        iscutomEnable=true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
//                        String body;
//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

}