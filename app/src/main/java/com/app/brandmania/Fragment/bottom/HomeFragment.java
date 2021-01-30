package com.app.brandmania.Fragment.bottom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ParagraphStyle;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.app.brandmania.Activity.ViewNotificationActivity;
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Adapter.DasboardAddaptor;
import com.app.brandmania.Adapter.ImageCateItemeInterFace;
import com.app.brandmania.Adapter.ViewPagerAdapter;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.ItemMultipleSelectionInterface;
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
import com.google.android.exoplayer2.text.Cue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    String ContactNo;
    ArrayList<BrandListItem> multiListItems=new ArrayList<>();
    FiveStarsDialog fiveStarsDialog;
    private static final int REQUEST_CALL = 1;
    private DasboardAddaptor dasboardAddaptor;
    ArrayList<FrameItem> FramePagerItems = new ArrayList<>();
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.8f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.2f;
    private static final int ALPHA_ANIMATIONS_DURATION = 100;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private RelativeLayout mTitleContainer;
    Activity act;
    Bitmap bmp;
    Bitmap bmpGmail;
    Bitmap bmpContact;
    Bitmap bmpWebsite;
    Bitmap scaledbmp;
    Bitmap scaledbmpPhone;
    Bitmap scaledbmpGmail;
    Bitmap scaledbmpWebsite;
    int pageWidth=1200;
    private String is_frame="";
    private String is_payment_pending="";
    private String is_package="";
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
        preafManager=new PreafManager(act);
        super.onResume();
    }

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home,parent,false);
        homeFragment=this;
        fiveStarsDialog = new FiveStarsDialog(getActivity(), "brandmania@gmail.com");
        preafManager = new PreafManager(act);

        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }
        requestAgain();
        RateUs();

        binding.businessName.setText(preafManager.getActiveBrand().getName());
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
        binding.creatPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf1();
            }
        });
        getBrandList();
        getFrame();

        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorsecond,
                R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startAnimation();
                getFrame();
                getImageCtegory();

            }
        });
        ContactNo= String.valueOf(Html.fromHtml( "<p>Tel: <a href= 441274 433043</a></p> Email: <a href=\"mailto:careers@bradfordcollege.ac.uk\"> careers@bradfordcollege.ac.uk</a>"));


        getDeviceToken(act);
        // AddUserActivity();

        binding.businessNameDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(BUSINESS_TYPE,BusinessTitle);

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
                    String number ="8460638464";
                    String BrandContact="\nRegistered Number: ";
                    String text = "Hello *BrandMania* ,  \n" + "this is request to add  *Frame* For BrandName:"+ binding.businessName.getText().toString() +BrandContact+preafManager.getMobileNumber();
                    String toNumber ="91"+number;
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


        if (!checkPermission())
        {
            requestPermission();
        }


//        bmp= BitmapFactory.decodeResource(getResources(),R.drawable.logoo);
//        bmpGmail= BitmapFactory.decodeResource(getResources(),R.drawable.mail);
//        bmpContact= BitmapFactory.decodeResource(getResources(),R.drawable.phone);
//        bmpWebsite= BitmapFactory.decodeResource(getResources(),R.drawable.world_wide_web);

Log.e("BrandMainiaImageURL",preafManager.getActiveBrand().getLogo());
      //  byte[] decodedString = Base64.decode(preafManager.getActiveBrand().getLogo(), Base64.DEFAULT);
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 100, decodedString.length);

//        scaledbmp= Bitmap.createScaledBitmap(bmp,300,300,false);
//        scaledbmpGmail= Bitmap.createScaledBitmap(bmpGmail,64,64,false);
//         scaledbmpPhone= Bitmap.createScaledBitmap(bmpContact,64,64,false);
//          scaledbmpWebsite= Bitmap.createScaledBitmap(bmpWebsite,64,64,false);
        binding.contactTxtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String number = Constant.ADMIN_CONTACT_NUMBER;
                    String BrandContact="\nRegistered Number: ";
                    String text = "Hello *BrandMania* , \n" + "this is request to add *Frame* For BrandName:"+ preafManager.getActiveBrand().getName() +BrandContact+preafManager.getMobileNumber();
                    String toNumber ="91"+number;
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
        bottomSheetFragment.setListData(callingFlag,title);
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
                        final ViewPagerAdapter viewPagerAddeptor=new ViewPagerAdapter(viewPagerItems,act);
                        binding.ViewPagerView.setAdapter(viewPagerAddeptor);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() { binding.ViewPagerView.post(new Runnable(){
                                @Override
                                public void run() {
                                    binding.ViewPagerView.setCurrentItem((binding.ViewPagerView.getCurrentItem()+1)%viewPagerAddeptor.getCount(),true);
                                }
                            }); }
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
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
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
                        }else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }else {
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
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
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
                        }else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }
                    if (apiResponse.getDashBoardItems()==null ||apiResponse.getDashBoardItems().size()==0) {
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
                JSONObject jsonObject=ResponseHandler.createJsonObject(response);
                try {
                    preafManager.setAppTutorial(ResponseHandler.getString(ResponseHandler.getJSONArray(jsonObject,"data").getJSONObject(0),"video_url_path"));
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
                params.put("Authorization","Bearer"+preafManager.getUserToken());
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
        CodeReUse.activityBackPress(act);
    }
    @Override public void onItemSMultipleelection(int calledFlag, int position, BrandListItem listModel) {
        if (bottomSheetFragment != null && bottomSheetFragment.isVisible()) {

            bottomSheetFragment.dismiss();
        }
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
        Log.e("Second", gson.toJson(preafManager.getActiveBrand()));

    }
    @Override public void ImageCateonItemSelection(int position, ImageList listModel) {

    }
    public void resetData() {
        binding.businessName.setText(preafManager.getActiveBrand().getName());

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
                    for (int i=0;i<multiListItems.size();i++){
                        if (multiListItems.get(i).getId().equalsIgnoreCase(preafManager.getActiveBrand().getId())){
                            preafManager.setActiveBrand(multiListItems.get(i));
                            break;
                        }
                    }
                    preafManager=new PreafManager(act);
                    binding.businessName.setText(preafManager.getActiveBrand().getName());

                    //FirstLogin
                    if (act.getIntent().hasExtra("FirstLogin")){


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
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
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
    @Override public void onNegativeReview(int stars) {
        Log.d(TAG, "Negative review " + stars);
    }
    @Override public void onRefresh() {
        startAnimation();
        getFrame();
        getImageCtegory();
        getBrandList();
        getBanner();

    }
    @Override public void onReview(int stars) {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FRAME : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
                    JSONObject datajsonobjecttt =ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame= datajsonobjecttt.getString("is_frame");

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
                params.put("brand_id",preafManager.getActiveBrand().getId());
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



    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD,BaseColor.BLACK);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font clckableText = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD,BaseColor.BLUE);
    private  void addTitlePage(Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 2);
        // Lets write a big header
        preface.add(new Paragraph( preafManager.getActiveBrand().getName(), catFont));
        addEmptyLine(preface, 0);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph(preafManager.getActiveBrand().getAddress() ,smallBold));
        document.add(preface);

//        PdfContentByte canvas = writer.getDirectContent();
//        CMYKColor magentaColor = new CMYKColor(0.f, 0.f, 0.f, 100.f);
//        canvas.setColorStroke(magentaColor);
//        canvas.moveTo(36, 36);
//        canvas.lineTo(36, 806);
//        canvas.lineTo(559, 36);
//        canvas.lineTo(559, 806);
//        canvas.closePathStroke();




        // Start a new page
        document.newPage();
    }



    //Creat PDF
    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {


        ActivityCompat.requestPermissions(act, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }
    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
    public static final BaseColor blue = new BaseColor(0, 0, 255);


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf1() {

        Document document=new Document();
        String outpath=Environment.getExternalStorageDirectory()+"/MytPdfBrand.pdf";

        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outpath));
            //PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();


            Drawable d = act.getResources().getDrawable(R.drawable.logoo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scalePercent(20);
            image.setAlignment(Element.ALIGN_CENTER);
            document.add(image);

            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 2);
            // Lets write a big header
            preface.add(new Paragraph( preafManager.getActiveBrand().getName(), catFont));

            addEmptyLine(preface, 0);
            // Will create: Report generated by: _name, _date
            preface.add(new Paragraph(preafManager.getActiveBrand().getAddress() ,smallBold));
            preface.setIndentationLeft(0);
            document.add(preface);



            Paragraph prefaceClicable = new Paragraph();
            //  PdfContentByte pdfContentByte=new PdfContentByte(writer);
            PdfContentByte canvas = writer.getDirectContent();
            CMYKColor magentaColor = new CMYKColor(0.f, 0.f, 0.f, 100.f);
            canvas.setColorStroke(magentaColor);
            canvas.moveTo(30, 570);
            canvas.lineTo(570, 570);
            canvas.closePathStroke();

            Drawable contact = act.getResources().getDrawable(R.drawable.phone);
            BitmapDrawable bitContact = ((BitmapDrawable) contact);
            Bitmap bmpContact = bitContact.getBitmap();
            ByteArrayOutputStream streamContact = new ByteArrayOutputStream();
            bmpContact.compress(Bitmap.CompressFormat.PNG, 100, streamContact);
            Image imageContact = Image.getInstance(streamContact.toByteArray());
            imageContact.scalePercent(30);
            imageContact.setAbsolutePosition(30f, 507f);
            imageContact.setAlignment(Element.ALIGN_LEFT);
            document.add(imageContact);
            addEmptyLine(prefaceClicable, 2);
            prefaceClicable.add(new Phrase(""));
            prefaceClicable.setIndentationLeft(50);
            Anchor anchor = new Anchor( preafManager.getActiveBrand().getPhonenumber(),clckableText);
            anchor.setReference(String.valueOf(Uri.parse("tel:"+91+preafManager.getActiveBrand().getPhonenumber())));
            prefaceClicable.add(anchor);

            Drawable email = act.getResources().getDrawable(R.drawable.email);
            BitmapDrawable bitEmail = ((BitmapDrawable) email);
            Bitmap bmpEmail = bitEmail.getBitmap();
            ByteArrayOutputStream streamEmail = new ByteArrayOutputStream();
            bmpEmail.compress(Bitmap.CompressFormat.PNG, 100, streamEmail);
            Image imageEmail = Image.getInstance(streamEmail.toByteArray());
            imageEmail.scalePercent(30);
            imageEmail.setAbsolutePosition(30f, 462f);
            imageEmail.setAlignment(Element.ALIGN_LEFT);
            document.add(imageEmail);
            addEmptyLine(prefaceClicable,1);
            prefaceClicable.add(new Phrase(""));
            prefaceClicable.setIndentationLeft(50);
            Anchor anchorEmail = new Anchor( preafManager.getActiveBrand().getEmail(),clckableText);
            anchorEmail.setReference(String.valueOf(Uri.parse("mailto:"+preafManager.getActiveBrand().getEmail())));
            prefaceClicable.add(anchorEmail);

            Drawable website = act.getResources().getDrawable(R.drawable.internet);
            BitmapDrawable bitWebsite = ((BitmapDrawable) website);
            Bitmap bmpWebsite = bitWebsite.getBitmap();
            ByteArrayOutputStream streamWebsite = new ByteArrayOutputStream();
            bmpWebsite.compress(Bitmap.CompressFormat.PNG, 100, streamWebsite);
            Image imageWebsite = Image.getInstance(streamWebsite.toByteArray());
            imageWebsite.scalePercent(30);
            imageWebsite.setAbsolutePosition(30f, 415f);
            imageWebsite.setAlignment(Element.ALIGN_LEFT);
            document.add(imageWebsite);
            addEmptyLine(prefaceClicable,1 );
            prefaceClicable.add(new Phrase(""));
            prefaceClicable.setIndentationLeft(50);
            Anchor anchorWebsite = new Anchor( preafManager.getActiveBrand().getWebsite(),clckableText);
            anchorWebsite.setReference(preafManager.getActiveBrand().getWebsite());
            prefaceClicable.add(anchorWebsite);
            document.add(prefaceClicable);


            ByteArrayOutputStream streamBack = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(act.getResources(), R.drawable.logoo);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100, streamBack);
            Image img;
            img = Image.getInstance(stream.toByteArray());
            img.setAbsolutePosition(100, 100);
            document.add(img);



            document.close();

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






    private void createPdf(){
//        // create a new document
        PdfDocument document = new PdfDocument();
        Paint mypaint=new Paint();
        Paint titlePaint=new Paint();
       TextPaint p = new TextPaint();
//        // crate a page description
       PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
     //   canvas.drawBitmap(scaledbmp,0,0,mypaint);

        mypaint.setColor(Color.rgb(0,113,188));
        mypaint.setTextAlign(Paint.Align.RIGHT);

        mypaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawBitmap(scaledbmp,500,100,mypaint);
//
//
//        titlePaint.setTextAlign(Paint.Align.LEFT);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
//        titlePaint.setTextSize(50);
//        canvas.drawText(preafManager.getActiveBrand().getName(),60,600,titlePaint);
//
//
//        titlePaint.setTextAlign(Paint.Align.LEFT);
//        titlePaint.setStyle(Paint.Style.FILL);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
//        titlePaint.setTextSize(40);
//        canvas.drawText(preafManager.getActiveBrand().getAddress(),60,660,titlePaint);
     //   canvas.drawLine(1140,700,60,700,titlePaint);
//
//
//        titlePaint.setTextAlign(Paint.Align.LEFT);
//        titlePaint.setStyle(Paint.Style.FILL);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
//        titlePaint.setTextSize(50);
//        canvas.drawText(preafManager.getActiveBrand().getName(),60,770,titlePaint);
//
//        titlePaint.setTextAlign(Paint.Align.LEFT);
//        titlePaint.setStyle(Paint.Style.FILL);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
//        titlePaint.setTextSize(40);
//        canvas.drawText("Director",60,820,titlePaint);
//
//
//
//
//        titlePaint.setTextAlign(Paint.Align.LEFT);
//        titlePaint.setStyle(Paint.Style.FILL);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
//        titlePaint.setTextSize(45);
//        canvas.drawText(preafManager.getActiveBrand().getEmail(),180,940,titlePaint);
//       // canvas.drawColor(R.color.colorPrimary);
//        canvas.drawBitmap(scaledbmpGmail,60,890,mypaint);
//
//
//
////        String link = "https://example.com";
////        SpannableString s = new SpannableString("some link");
////        s.setSpan(new URLSpan(link), 0, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////        canvas.drawText(s.toString(), 60, 990, titlePaint);
//
//
//        p.setTextAlign(Paint.Align.LEFT);
//        p.setStyle(Paint.Style.FILL);
//        p.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
//        p.setTextSize(45);
//        canvas.drawText("<a href=https://www.google.com/>https://www.google.com/</a>",180,1040,p);
//         canvas.drawColor(R.color.colorPrimary);
//        //canvas.drawText("<a href='https://example.com'>some link</a>", 30, 30, titlePaint);
////        String link = "https://www.google.com/";
////        SpannableString l = new SpannableString("https://www.google.com/");
////        l.setSpan(new URLSpan(link), 0, link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////        canvas.drawText(l.toString(), 180, 1040, p);
//        canvas.drawBitmap(scaledbmpPhone,60,990,mypaint);
//
//
//
//
//      //  canvas.drawText(, 30, 30, titlePaint);
//        if (preafManager.getActiveBrand().getWebsite()!=null) {
//            titlePaint.setTextAlign(Paint.Align.LEFT);
//            titlePaint.setStyle(Paint.Style.FILL);
//            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
//            titlePaint.setTextSize(45);
//            canvas.drawText(preafManager.getActiveBrand().getWebsite(), 180, 1140, titlePaint);
//            // canvas.drawColor(R.color.colorPrimary);
//            canvas.drawBitmap(scaledbmpWebsite, 60, 1090, mypaint);
//        }
//        else
//        {
//
//        }


        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.pdf_person_container, null);

//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//        ImageView Logo=(ImageView) view.findViewById(R.id.logo);
//        TextView BrandName=(TextView)view.findViewById(R.id.brandName);
//        TextView Address=(TextView)view.findViewById(R.id.address);
//        TextView ContactNumber=(TextView)view.findViewById(R.id.contactNumber);
//        TextView EmailId=(TextView)view.findViewById(R.id.gmailText);
//        TextView WebSite=(TextView)view.findViewById(R.id.websiteText);
//        Glide.with(act)
//                .load(preafManager.getActiveBrand().getLogo())
//                .into(Logo);
//        BrandName.setText(preafManager.getActiveBrand().getName());
//        Address.setText(preafManager.getActiveBrand().getAddress());
//        ContactNumber.setText(preafManager.getActiveBrand().getPhonenumber());


//        ContactNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(act, "bvnvbnvbn", Toast.LENGTH_SHORT).show();
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:0377778888"));
//
//                if (ActivityCompat.checkSelfPermission(act,
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                startActivity(callIntent);
//            }
//        });
//        EmailId.setText(preafManager.getActiveBrand().getEmail());
//        WebSite.setText(preafManager.getActiveBrand().getWebsite());
//        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
//        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
//        view.measure(measureWidth, measuredHeight);
//        view.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());
//        view.draw(page.getCanvas());








        document.finishPage(page);



       // document.finishPage(page);
        // write the document content
          String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BrandManiaPdf/";
          File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = "BrandManiaPdf" +System.currentTimeMillis()+ ".pdf";
        String targetPdf=file.getAbsolutePath() + "/" + name;
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(act, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(act, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

}
