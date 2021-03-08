package com.app.brandmania.Fragment.bottom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.UpdateBandList;
import com.app.brandmania.Activity.ViewNotificationActivity;
import com.app.brandmania.Adapter.DasboardAddaptor;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Adapter.ViewPagerAdapter;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.ItemMultipleSelectionInterface;
import com.app.brandmania.Connection.MyPdfPageEventHelper;
import com.app.brandmania.Connection.WatermarkPageEvent;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class HomeFragment extends BaseFragment implements ItemMultipleSelectionInterface , ImageCateItemeInterFace, NegativeReviewListener, ReviewListener,SwipeRefreshLayout.OnRefreshListener {
    public static int BUSINESS_TYPE = 1;
    private String BusinessTitle;
    ArrayList<BrandListItem> BusinessTypeList = new ArrayList<>();
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    DashBoardItem apiResponse;
    BrandListItem brandListItem;
    private int[] layouts;
     BaseColor red = new BaseColor(77, 86, 222);
    String ContactNo;
    Intent CamIntent, GalIntent, CropIntent;
    private Bitmap selectedLogo;
    private Uri mCropImageUri;
    AlertDialog.Builder alertDialogBuilder;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    FiveStarsDialog fiveStarsDialog;
    private static final int REQUEST_CALL = 1;
    private DasboardAddaptor dasboardAddaptor;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    private RelativeLayout mTitleContainer;
    Activity act;
    int pageWidth = 1200;
    private String is_frame = "";
    PreafManager preafManager;
    private String deviceToken = "";
    private FragmentHomeBinding binding;
    Timer timer;
    private HomeFragment homeFragment;
    private SelectBrandListBottomFragment bottomSheetFragment;

    public String getDeviceToken(Activity act) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        deviceToken = instanceIdResult.getToken();
                        UpdateToken();
                    }
                });
        return deviceToken;
    }

    @Override
    public void onResume() {
        preafManager = new PreafManager(act);
        super.onResume();
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, parent, false);
        homeFragment = this;
        fiveStarsDialog = new FiveStarsDialog(Objects.requireNonNull(getActivity()), "brandmania@gmail.com");
        preafManager = new PreafManager(act);

            Glide.with(act)
                    .load(preafManager.getActiveBrand().getLogo())
                    .into(binding.pdfLogo);


        Log.e("LogoForPdf", binding.pdfLogo.toString());
        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }


        requestAgain();
        RateUs();




            Glide.with(act).load(preafManager.getActiveBrand().getLogo());







        mTitleContainer = act.findViewById(R.id.main_linearlayout_title);
        // binding.alertText.setSelected(true);
        binding.showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(act, ViewNotificationActivity.class);
                startActivity(intent);
                act.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
            }
        });

        binding.creatPdfCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preafManager.getActiveBrand().getLogo().isEmpty()) {

                    createPdf1();
                } else {
                    alertDialogBuilder = new AlertDialog.Builder(act);

                    alertDialogBuilder.setTitle("Save image");
                    alertDialogBuilder.setMessage("Your Logo is empty..!");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent i = new Intent(act, UpdateBandList.class);
                                    startActivity(i);
                                    act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                                }
                            });


                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        getBrandList();

        getFrame();

        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorsecond, R.color.colorthird);

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startAnimation();
                getFrame();
                getImageCtegory();

            }
        });
        getDeviceToken(act);
        // AddUserActivity();

        binding.businessNameDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(BUSINESS_TYPE, BusinessTitle);

            }
        });
        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        String number = "8460638464";
                        String BrandContact = "\nRegistered Number: ";
                        String text = "Hello *BrandMania* ,  \n" + "this is request to add  *Frame* For BrandName:" + binding.businessName.getText().toString() + BrandContact + preafManager.getMobileNumber();
                        String toNumber = "91" + number;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        });
        startAnimation();
        getImageCtegory();
        getBanner();


        if (!checkPermission()) {
            requestPermission();
        }
        binding.contactTxtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
                        String number = Constant.ADMIN_CONTACT_NUMBER;
                        String BrandContact = "\nRegistered Number: ";
                        String text = "Hello *BrandMania* , \n" + "this is request to add *Frame* For BrandName:" + preafManager.getActiveBrand().getName() + BrandContact + preafManager.getMobileNumber();
                        String toNumber = "91" + number;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        });
        return binding.getRoot();
    }

    public void isPermissionGranted(boolean permission) {
        if (!permission) {
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", act.getPackageName(), null)));
        }
    }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.rocommRecycler.setVisibility(View.GONE);

    }

    //Show Fragment For BrandList...........
    public void showFragmentList(int callingFlag, String title) {

        bottomSheetFragment = new SelectBrandListBottomFragment();
        bottomSheetFragment.setHomeFragment(homeFragment);
        bottomSheetFragment.setListData(callingFlag, title);
        bottomSheetFragment.setLayoutType(1);
        if (bottomSheetFragment.isVisible()) {
            bottomSheetFragment.dismiss();
        }
        if (bottomSheetFragment.isAdded()) {
            bottomSheetFragment.dismiss();
        }
        bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
    }

    //SetAddeptor.....................
    public void setAdapter() {
        dasboardAddaptor = new DasboardAddaptor(menuModels, act);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.rocommRecycler.setHasFixedSize(true);
        binding.rocommRecycler.setLayoutManager(mLayoutManager);
        binding.rocommRecycler.setAdapter(dasboardAddaptor);

    }

    //GetBanner........................
    private void getBanner() {
        binding.swipeContainer.setRefreshing(true);
        Utility.Log("API : ", APIs.GET_BANNER);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BANNER : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    binding.swipeContainer.setRefreshing(false);
                    viewPagerItems = ResponseHandler.HandleGetBanneList(jsonObject);
                    if (viewPagerItems != null && viewPagerItems.size() != 0) {
                        final ViewPagerAdapter viewPagerAddeptor = new ViewPagerAdapter(viewPagerItems, act);
                        binding.ViewPagerView.setAdapter(viewPagerAddeptor);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                binding.ViewPagerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.ViewPagerView.setCurrentItem((binding.ViewPagerView.getCurrentItem() + 1) % viewPagerAddeptor.getCount(), true);
                                    }
                                });
                            }
                        };
                        timer = new Timer();
                        timer.schedule(timerTask, 3000, 3000);

                    } else {
                        Log.e("Condidtion", "Else");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();


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
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    //GetImageCategory..................
    private void getImageCtegory() {
        Utility.Log("API : ", APIs.GET_IMAGE_CATEGORY + "?page=1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGE_CATEGORY + "?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_IMAGE_CATEGORY : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    apiResponse = ResponseHandler.HandleGetImageCategory(jsonObject);
                    if (apiResponse.getDashBoardItems() != null) {
                        menuModels = apiResponse.getDashBoardItems();
                        if (menuModels != null && menuModels.size() != 0) {
                            setAdapter();
                            binding.shimmerViewContainer.stopShimmer();
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.rocommRecycler.setVisibility(View.VISIBLE);
                        }
                    }

                    if (apiResponse.getLinks() != null) {

                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCtegoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        } else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    } else {
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();
                        binding.swipeContainer.setRefreshing(false);
                        binding.rocommRecycler.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
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
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void getImageCtegoryNextPage(String nextPageUrl) {
        Utility.Log("API-", nextPageUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, nextPageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_IMAGE_CATEGORY : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    apiResponse = ResponseHandler.HandleGetImageCategory(jsonObject);
                    if (apiResponse.getDashBoardItems() != null) {
                        if (menuModels != null && menuModels.size() != 0) {
                            int lastPos = menuModels.size();
                            menuModels.addAll(menuModels.size(), apiResponse.getDashBoardItems());
                            dasboardAddaptor.notifyItemRangeInserted(lastPos, apiResponse.getDashBoardItems().size());
                        } else {
                            menuModels = new ArrayList<>();
                            menuModels.addAll(0, apiResponse.getDashBoardItems());
                        }
                    }
                    if (apiResponse.getLinks() != null) {
                        Log.e("APIIII", new Gson().toJson(apiResponse.getLinks()));
                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCtegoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        } else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }
                    if (apiResponse.getDashBoardItems() == null || apiResponse.getDashBoardItems().size() == 0) {
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);

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
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    //Update Token......................
    private void UpdateToken() {
        Utility.Log("TokenURL", APIs.UPDATE_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("UPDATE_TOKENnn", response);
                // {"status":true,"data":[{"id":1,"video_url_path":"http:\/\/queryfinders.com\/brandmania_uat\/public\/storage\/uploads\/video\/Skype_Video.mp4"}],"message":"Device Token Updated."}
                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                try {
                    preafManager.setAppTutorial(ResponseHandler.getString(ResponseHandler.getJSONArray(jsonObject, "data").getJSONObject(0), "video_url_path"));
                    MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.APP_INTRO_REFRESH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                return params;

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("Verify-Param", hashMap.toString());
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    //Back Event.........................
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
      //  a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }

    @Override
    public void onItemSMultipleelection(int calledFlag, int position, BrandListItem listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {

            bottomSheetFragment.dismiss();
        }
        preafManager.setActiveBrand(listModel);
        Intent i = new Intent(act, HomeActivity.class);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        act.finish();
        binding.businessName.setText(listModel.getName());
        brandListItem = listModel;
        preafManager.setActiveBrand(listModel);
        Gson gson = new Gson();


    }

    @Override
    public void ImageCateonItemSelection(int position, ImageList listModel) {

    }


    private void makePhoneCall() {
        String number = "8460638464";
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(act,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
    }

    private void RateUs() {
//        AppRate.with(act)
//                .setInstallDays(1)
//                .setLaunchTimes(3)
//                .setRemindInterval(2)
//                .monitor();
//
//        AppRate.showRateDialogIfMeetsConditions(act);

        fiveStarsDialog.setRateText("Rate Us")

                .setTitle("How was your experience with us?")

                .setForceMode(false)

                .setUpperBound(2)

                .setNegativeReviewListener(this)

                .setReviewListener(this)

                .showAfter(2);

    }

    private void getBrandList() {

        binding.swipeContainer.setRefreshing(true);
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BRAND : ", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    binding.swipeContainer.setRefreshing(false);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    preafManager.setAddBrandList(multiListItems);

                    for (int i = 0; i < multiListItems.size(); i++) {
                        if (multiListItems.get(i).getId().equalsIgnoreCase(preafManager.getActiveBrand().getId())) {
                            preafManager.setActiveBrand(multiListItems.get(i));
                            break;
                        }
                    }
                    preafManager = new PreafManager(act);
                    binding.businessName.setText(preafManager.getActiveBrand().getName());

                    //FirstLogin
                    if (act.getIntent().hasExtra("FirstLogin")) {


                        preafManager.setIS_Brand(true);

                        if (multiListItems.size() != 0) {
                            preafManager.setActiveBrand(multiListItems.get(0));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();


                    }
                }
        ) {
            /**
             * Passing some request headers*
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void requestAgain() {
        ActivityCompat.requestPermissions(act,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                CodeReUse.ASK_PERMISSSION);
    }

    @Override
    public void onNegativeReview(int stars) {
        Log.d(TAG, "Negative review " + stars);
    }

    @Override
    public void onRefresh() {
        startAnimation();
        getFrame();
        getImageCtegory();
        getBrandList();
        getBanner();

    }

    @Override
    public void onReview(int stars) {
        Log.d(TAG, "Review " + stars);
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            //container.removeView(view);
        }
    }

    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FRAME : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
                    JSONObject datajsonobjecttt = ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame = datajsonobjecttt.getString("is_frame");

//                    if (is_frame.equals("1")) {
//                      //  Toast.makeText(act,"Frame is added",Toast.LENGTH_LONG).show();
//                        binding.alertRelative.setVisibility(View.GONE);
//                        is_payment_pending= datajsonobjecttt.getString("is_payment_pending");
//                        is_package= datajsonobjecttt.getString("package");
//
//                        if(is_package.equals(""))
//                        {
//                            binding.contactTxtLayout.setVisibility(View.VISIBLE);
//                          //  binding.alertText.setText(ResponseHandler.getString(datajsonobjecttt, "package_message"));
//
//                        }
//                        else if (is_payment_pending.equals("1"))
//                        {
//                            binding.contactTxtLayout.setVisibility(View.VISIBLE);
//                            binding.alertText.setText(ResponseHandler.getString(datajsonobjecttt, "payment_message"));
//
//                        }
//
//
//                    }
//                    else if (is_frame.equals("0")) {
//                        binding.alertText.setText(ResponseHandler.getString(datajsonobjecttt, "frame_message"));
//                        binding.contactTxtLayout.setVisibility(View.VISIBLE);
//                    }


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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", preafManager.getActiveBrand().getId());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    @Override
    public void update(Observable observable, Object data) {

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_BRAND_NAME) {
            getBrandList();
        }

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static Font catFont = new Font(Font.FontFamily.UNDEFINED, 30, Font.BOLD, BaseColor.BLACK);
    private static Font smallBold = new Font(Font.FontFamily.UNDEFINED, 22, Font.NORMAL);
    private static Font clckableText = new Font(Font.FontFamily.UNDEFINED, 25, Font.BOLD, BaseColor.BLUE);


    //Creat PDF
    private static final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {


        ActivityCompat.requestPermissions(act, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted)
                        Snackbar.make(binding.creatPdf, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        Snackbar.make(binding.creatPdf, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_REQUEST_CODE);
                        }

                    }
                }


                break;
        }
    }
    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "BrandManiaPdf");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) private void createPdf1() {


        Document document = new Document(PageSize.A4);
//        String outpath = Environment.getExternalStorageDirectory() + "/MytPdfBrand.pdf";
//
//
//
//
//        File file = getDisc();
//        if (!file.exists() && !file.mkdirs()) {
//            return;
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
//        String date = simpleDateFormat.format(new Date());
//        String name = "BrandPdf" +System.currentTimeMillis()+ ".pdf";
//        String file_name = file.getAbsolutePath() + "/" + name;


        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BrandManiaPdf";

        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();

        File file = new File(dir, "brandmania.pdf");


        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(), R.drawable.pdfbackk);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100, stream);
            Image img;

            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(0, 0);
            img.scalePercent(60f,60f);
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(document, fOut);
            writer.setPageEvent(new MyPdfPageEventHelper(act));
            document.open();

            //Drawable d = act.getResources().getDrawable(R.drawable.pdf_banner);
            //BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = ((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap();//bitDw.getBitmap();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image image = Image.getInstance(stream1.toByteArray());
            image.scalePercent(25);

            // image.setAlignment(Element.ALIGN_CENTER);
            image.setAbsolutePosition(240, 670);

            document.add(image);




            Paragraph preface = new Paragraph();

            //For Brand Name..............
            Font brandName = FontFactory.getFont("assets/font/montserrat_bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 40); //10 is the size
            brandName.setColor(WebColors.getRGBColor("#faa81e"));
            addEmptyLine(preface, 11);
            preface.add(new Paragraph(preafManager.getActiveBrand().getName(), brandName));



            //For Address
            Font address = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 23); //10 is the size
            preface.add(new Paragraph(preafManager.getActiveBrand().getAddress(), address));
            addEmptyLine(preface, 0);
            preface.setIndentationLeft(0);
            document.add(preface);

            //For First UnderLine
            PdfContentByte canvas = writer.getDirectContent();
            BaseColor baseColorFirst=new BaseColor(173,39,83);
            canvas.setColorStroke(baseColorFirst);
            canvas.moveTo(30, 500);
            canvas.lineTo(570, 500);
            canvas.setLineWidth(2f);
            canvas.closePathStroke();

            //For Second UnderLine
            PdfContentByte canvasSecond = writer.getDirectContent();
            BaseColor baseColorSecond=new BaseColor(173,39,83);
            canvasSecond.setColorStroke(baseColorSecond);
            canvasSecond.moveTo(30, 492);
            canvasSecond.lineTo(570, 492);
            canvasSecond.setLineWidth(5f);
            canvasSecond.closePathStroke();

            //For Third UnderLine
             PdfContentByte canvasThird = writer.getDirectContent();
             BaseColor baseColorThird=new BaseColor(173,39,83);
             canvasThird.setColorStroke(baseColorThird);
             canvasThird.moveTo(30, 484);
             canvasThird.lineTo(570, 484);
             canvasThird.setLineWidth(2f);
             canvasThird.closePathStroke();





        //For Contact Number and Contact Logo..........
        if (preafManager.getActiveBrand().getPhonenumber()!=null && !prefManager.getActiveBrand().getPhonenumber().isEmpty()) {
       //  Drawable contact = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
         Paragraph prefaceClicableContact = new Paragraph();
       //BitmapDrawable bitContact = ((BitmapDrawable) contact);
         @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpContact = getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_call_for_pdf));//bitContact.getBitmap();
         ByteArrayOutputStream streamContact = new ByteArrayOutputStream();
         bmpContact.compress(Bitmap.CompressFormat.PNG, 100, streamContact);
         Image imageContact = Image.getInstance(streamContact.toByteArray());
         imageContact.scalePercent(50);
         imageContact.setAbsolutePosition(30f, 415f);
         imageContact.setAlignment(Element.ALIGN_LEFT);
         document.add(imageContact);
         addEmptyLine(prefaceClicableContact, 3);
         prefaceClicableContact.add(new Phrase(""));
         prefaceClicableContact.setIndentationLeft(50);
         Font contactFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
         contactFont.setColor(WebColors.getRGBColor("#ad2753"));
         Anchor anchor = new Anchor(preafManager.getActiveBrand().getPhonenumber(), contactFont);
         anchor.setReference(String.valueOf(Uri.parse("tel:" + 91 + preafManager.getActiveBrand().getPhonenumber())));
         prefaceClicableContact.add(anchor);
         document.add(prefaceClicableContact);
        }
        //For Gmail Id and Gmail logo................
        if (preafManager.getActiveBrand().getEmail()!=null &&  !prefManager.getActiveBrand().getEmail().isEmpty()) {
                Paragraph prefaceClicableEmail = new Paragraph();
             //   Drawable email = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
             //   BitmapDrawable bitEmail = ((BitmapDrawable) email);
                @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpEmail =getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_gmail_for_pdf)); //bitEmail.getBitmap();
                ByteArrayOutputStream streamEmail = new ByteArrayOutputStream();
                bmpEmail.compress(Bitmap.CompressFormat.PNG, 100, streamEmail);
                Image imageEmail = Image.getInstance(streamEmail.toByteArray());
                imageEmail.scalePercent(50);
                imageEmail.setAbsolutePosition(30f, 358f);
                imageEmail.setAlignment(Element.ALIGN_LEFT);
                document.add(imageEmail);
                addEmptyLine(prefaceClicableEmail, 1);
                prefaceClicableEmail.add(new Phrase(""));
                prefaceClicableEmail.setIndentationLeft(50);
                Font emailFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                emailFont.setColor(WebColors.getRGBColor("#ad2753"));
                Anchor anchorEmail = new Anchor(preafManager.getActiveBrand().getEmail(), emailFont);
                anchorEmail.setReference(String.valueOf(Uri.parse("mailto:" + preafManager.getActiveBrand().getEmail())));
                prefaceClicableEmail.add(anchorEmail);
                document.add(prefaceClicableEmail);
            }
        //For Website and websiteLogo..................
        if (prefManager.getActiveBrand().getWebsite()!=null && !prefManager.getActiveBrand().getWebsite().isEmpty()) {
                Paragraph prefaceClicableWebsite = new Paragraph();
              //  Drawable website = act.getResources().getDrawable(R.drawable.ic_call_for_pdf);
             //   BitmapDrawable bitWebsite = ((BitmapDrawable) website);
                @SuppressLint("UseCompatLoadingForDrawables") Bitmap bmpWebsite = getBitmapFromDrawable(act.getResources().getDrawable(R.drawable.ic_website_for_pdf));//bitWebsite.getBitmap();
                ByteArrayOutputStream streamWebsite = new ByteArrayOutputStream();
                bmpWebsite.compress(Bitmap.CompressFormat.PNG, 100, streamWebsite);
                Image imageWebsite = Image.getInstance(streamWebsite.toByteArray());
                imageWebsite.scalePercent(50);
                imageWebsite.setAbsolutePosition(30f, 300f);
                imageWebsite.setAlignment(Element.ALIGN_LEFT);
                addEmptyLine(prefaceClicableWebsite, 1);
                prefaceClicableWebsite.setIndentationLeft(50);
                Font websiteFont = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
                websiteFont.setColor(WebColors.getRGBColor("#ad2753"));
                Anchor anchorWebsite = new Anchor(preafManager.getActiveBrand().getWebsite(), websiteFont);
                anchorWebsite.setReference(preafManager.getActiveBrand().getWebsite());
                prefaceClicableWebsite.add(anchorWebsite);
                document.add(prefaceClicableWebsite);
                document.add(imageWebsite);
            }

        if (prefManager.getActiveBrand().getBrandService()!=null && !prefManager.getActiveBrand().getBrandService().isEmpty()) {
            Paragraph prefaceClicableServicesTag = new Paragraph();
            addEmptyLine(prefaceClicableServicesTag, 1);
            Font brandServicetag = FontFactory.getFont("assets/font/robotobold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 30); //10 is the size
            brandServicetag.setColor(WebColors.getRGBColor("#faa81e"));
            prefaceClicableServicesTag.add(new Paragraph("Service:", brandServicetag));
            prefaceClicableServicesTag.setIndentationLeft(0);
            document.add(prefaceClicableServicesTag);


            Paragraph paragraphClicableService = new Paragraph();
            addEmptyLine(paragraphClicableService, 0);
            paragraphClicableService.setIndentationLeft(0);
            Font bsuinessService = FontFactory.getFont("assets/font/montserrat_medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 25); //10 is the size
            bsuinessService.setColor(WebColors.getRGBColor("#000"));
            String list[] = preafManager.getActiveBrand().getBrandService().split(",|\\\n");

            for (int i = 0; i < list.length; i++) {
                // Log.e("pdfff",list[i]);
                paragraphClicableService.add(new Paragraph("\u2022\u00a0" + list[i], bsuinessService));

            }
            paragraphClicableService.setIndentationLeft(0);
            document.add(paragraphClicableService);
        }
        document.close();
//        try {
//            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +file_name);
//        }catch (Exception exc) {
//            System.out.println("Houston we got a problem! : "+exc);
//        }

//            File pdfFile = new File(outpath);
//            Uri path = Uri.fromFile(pdfFile);
//            // Setting the intent for pdf reader
//            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//            pdfIntent.setDataAndType(path, "application/pdf");
//            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            try {
//                startActivity(pdfIntent);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(act, "Can't read pdf file", Toast.LENGTH_SHORT).show();
//            }
            viewPdf("brandmania.pdf", "BrandManiaPdf");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    // Method for opening a pdf file
    private void viewPdf(String file, String directory) {

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
      //  Uri path = Uri.fromFile(pdfFile);
        Uri path = FileProvider.getUriForFile(act, act.getApplicationContext().getPackageName() + ".provider", pdfFile);
        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(act, "Can't read pdf file", Toast.LENGTH_SHORT).show();
        }
    }





}
