package com.make.mybrand.Activity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Common.Constant;
import com.make.mybrand.Common.HELPER;
import com.make.mybrand.Common.MakeMyBrandApp;
import com.make.mybrand.Common.ObserverActionID;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Fragment.bottom.CustomFragment;
import com.make.mybrand.Fragment.bottom.DownloadsFragment;
import com.make.mybrand.Fragment.bottom.HomeFragment;
import com.make.mybrand.Fragment.bottom.ProfileFragment;
import com.make.mybrand.Model.VersionListIItem;
import com.make.mybrand.R;
import com.make.mybrand.databinding.DialogPermissionsLayoutBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Observable;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.CUSTOM_TAB_CHANGE_INTERFACE {
    VersionListIItem versionListIItem;
    private AppUpdateManager appUpdateManager;

    BottomNavigationView navigation;
    private boolean isHomeTab = true;
    public static boolean isAlreadyDisplayed = false;
    public static boolean isAlreadyDisplayedOffer = false;
    public static boolean isAddBrandDialogDisplayed = false;
    public static boolean isOpenDownload = false;

    String receivedData = "";
    // Check the data to determine which fragment to open

    public static String[] storge_permissions_33 = {
            READ_MEDIA_IMAGES,
            READ_MEDIA_VIDEO
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUpdate();
        checkForUpdates();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        // Check the data to determine which fragment to open
        if (isOpenDownload) {
            openDownloadPage();
        } else {
            loadFragment(new HomeFragment());
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askPermissions();
        }

        Utility.Log("activeBrand", gson.toJson(prefManager.getActiveBrand()));
        try {
            int versionCode = act.getPackageManager()
                    .getPackageInfo(act.getPackageName(), 0).versionCode;
            prefManager.setAppCode(versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    void openDownloadPage() {
        isHomeTab = false;
        navigation.setSelectedItemId(R.id.navigation_download);
        Fragment fragment = new DownloadsFragment();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        loadFragment(fragment);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                isHomeTab = true;
                fragment = new HomeFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_custom:
                isHomeTab = false;
                fragment = new CustomFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_download:
                isHomeTab = false;
                fragment = new DownloadsFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;

            case R.id.navigation_profile:
                isHomeTab = false;
                fragment = new ProfileFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }


    public DialogPermissionsLayoutBinding permissionsLayoutBinding;
    private final int REQUESTED_ALL = 1;
    private final int REQUESTED_CAMERA = 2;
    private final int REQUESTED_STORAGE = 3;
    private final int REQUESTED_STORAGE_PERMISSION = 4;
    private final int REQUESTED_STORAGE_FOR_ALL_VERSION = 6;

    private final int REQUEST_SETTINGS = 5;
    private androidx.appcompat.app.AlertDialog alertDialog;

    public void askPermissions() {
        permissionsLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_permissions_layout, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(permissionsLayoutBinding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(permissionsLayoutBinding.getRoot());
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
        }

        permissionsLayoutBinding.permissionLayout1.setOnClickListener(v -> ActivityCompat.requestPermissions(act,
                new String[]{CAMERA}, REQUESTED_CAMERA));
        permissionsLayoutBinding.permissionLayout2.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(act,
                        new String[]{CAMERA, READ_MEDIA_IMAGES},
                        REQUESTED_STORAGE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(act,
                        new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                        REQUESTED_STORAGE);
            }
        });

        permissionsLayoutBinding.allowPermission.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                alertDialog.dismiss();
                isHomeTab = true;
                Fragment fragment = new HomeFragment();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                loadFragment(fragment);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(act,
                            new String[]{CAMERA, READ_MEDIA_IMAGES},
                            REQUESTED_STORAGE_FOR_ALL_VERSION);
                } else {
                    ActivityCompat.requestPermissions(act,
                            new String[]{CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                            REQUESTED_ALL);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean targetSetting = false;
        if (requestCode == REQUESTED_ALL) {
            if (grantResults.length > 0) {
                boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean writeStorageGrant = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (cameraGrant) {
                    permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
                }
                if (readStorageGrant && writeStorageGrant) {
                    permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                }
                if (cameraGrant && readStorageGrant && writeStorageGrant) {
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(CAMERA)) {
                        showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{CAMERA}, REQUESTED_CAMERA));
                    } else if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                        requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUESTED_STORAGE);
                    } else {
                        Log.e("Step ", "1");
                        targetSetting = true;
                    }
                }
            } else {
                Toast.makeText(act, "You need to allow permission for better performance", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUESTED_STORAGE_FOR_ALL_VERSION) {
            if (grantResults.length > 0) {
                boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraGrant) {
                    permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), storge_permissions_33[0]) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                    return;
                }
                if (cameraGrant && ContextCompat.checkSelfPermission(getApplicationContext(), storge_permissions_33[0]) == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(CAMERA)) {
                        showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{CAMERA}, REQUESTED_CAMERA));
                    } else if (shouldShowRequestPermissionRationale(storge_permissions_33[0])) {
                        showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{storge_permissions_33[0]}, REQUESTED_STORAGE_PERMISSION));
                    } else {
                        targetSetting = true;
                    }
                }
            } else {
                Toast.makeText(act, "You need to allow permission for better performance", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUESTED_CAMERA) {
            boolean cameraGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraGrant) {
                permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(CAMERA)) {
                    showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{CAMERA}, REQUESTED_CAMERA));
                } else {
                    targetSetting = true;
                }

            }
        } else if (requestCode == REQUESTED_STORAGE) {

            boolean readStorageGrant = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStorageGrant = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (readStorageGrant && writeStorageGrant) {
                permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) || shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUESTED_CAMERA));
                } else {
                    targetSetting = true;
                }
            }
        } else if (requestCode == REQUESTED_STORAGE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), storge_permissions_33[0]) == PackageManager.PERMISSION_GRANTED) {
                permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(storge_permissions_33[0])) {
                    showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> requestPermissions(new String[]{storge_permissions_33[0]}, REQUESTED_STORAGE_PERMISSION));
                } else {
                    targetSetting = true;
                }
            }
        }
        if (targetSetting) {
            showMessageOKCancel("You need to allow access to the permissions", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                act.startActivityForResult(intent, REQUEST_SETTINGS);
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

    //app updates
    private void checkForUpdates() {
        appUpdateManager = AppUpdateManagerFactory.create(act);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                startAppUpdates(appUpdateInfo);
            }
        });

    }

    private void startAppUpdates(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, act, Constant.APP_UPDATES);
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
                            appUpdateInfo -> {
                                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                    try {
                                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, IMMEDIATE, act, Constant.APP_UPDATES);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
        }


        if (alertDialog != null && alertDialog.isShowing()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.allowPermission.setText("Close");
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                        if (isOpenDownload) {
                            HELPER.print("DownloadFragment","DONE");
                            openDownloadPage();
                        } else {
                            HELPER.print("HomeFragment","DONE");
                            isHomeTab = true;
                            Fragment fragment = new HomeFragment();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            loadFragment(fragment);
                        }
                    }
                }

            } else {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.checked1.setVisibility(View.VISIBLE);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.checked2.setVisibility(View.VISIBLE);
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    permissionsLayoutBinding.allowPermission.setText("Close");
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                        if (isOpenDownload) {
                            HELPER.print("DownloadFragment","DONE");
                            openDownloadPage();
                        } else {
                            HELPER.print("HomeFragment","DONE");
                            isHomeTab = true;
                            Fragment fragment = new HomeFragment();
                            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                            loadFragment(fragment);
                        }
                    }
                }
            }
        }
        //  MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.NOTIFY);
    }

    public void onBackPressed() {
        if (isHomeTab) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        } else {
            navigation.setSelectedItemId(R.id.navigation_home);
        }
    }

    private void getUpdate() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonArray1 = jsonObject.getJSONObject("data");
                    versionListIItem = new VersionListIItem();
                    versionListIItem.setId(jsonArray1.getString("id"));
                    versionListIItem.setAppliactionVersion(jsonArray1.getString("application_version"));
                    versionListIItem.setMessage(jsonArray1.getString("message"));
                    versionListIItem.setForcefullyUpdate(jsonArray1.getString("forcefully_update"));
                    versionListIItem.setIsNew(jsonArray1.getString("is_new"));
                    versionListIItem.setCreatedAt(jsonArray1.getString("created_at"));
                    versionListIItem.setUpdatedAt(jsonArray1.getString("updated_at"));
                    versionListIItem.setDeletedAt(jsonArray1.getString("deleted_at"));

                    int apiVERSION = Integer.parseInt(versionListIItem.getAppliactionVersion().replace(".", ""));
                    int currentVERSION = Integer.parseInt(String.valueOf(Constant.F_VERSION).replace(".", ""));

                    if (apiVERSION > currentVERSION) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(act);
                        final View customLayout = getLayoutInflater().inflate(R.layout.frame_alert_box, null);
                        ImageView cloasedBox = customLayout.findViewById(R.id.CloseImg);
                        WebView webView = customLayout.findViewById(R.id.webView);
                        TextView updateTitle = customLayout.findViewById(R.id.updateTitle);
                        Button updateBtn = customLayout.findViewById(R.id.updateBtn);
                        webView.loadData(jsonArray1.getString("message"), "text/html; charset=utf-8", "utf-8");
                        webView.setBackgroundColor(Color.TRANSPARENT);
                        String htmlString = "<u>App Update</u>";
                        updateTitle.setText(Html.fromHtml(htmlString));
                        builder.setView(customLayout);

                        if (versionListIItem.getForcefullyUpdate().equalsIgnoreCase("1")) {
                            cloasedBox.setVisibility(View.GONE);
                        }

                        AlertDialog dialog = builder.create();
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
                                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.make.mybrand");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                try {

                                    startActivity(intent);
                                } catch (Exception e) {

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

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void makeTabChange(int i) {
        Fragment fragment = null;
        if (i == 1) {
            navigation.setSelectedItemId(R.id.navigation_custom);
            fragment = new CustomFragment();
        }
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        loadFragment(fragment);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.DOWNLOAD_FRAGMENT) {
            HELPER.print("UPDATE","DONEEEEEEEE");
            isOpenDownload = true;
        }
        super.update(observable, data);
    }
}