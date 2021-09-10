package com.app.brandmania.Fragment.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.PdfActivity;
import com.app.brandmania.Activity.ViewNotificationActivity;
import com.app.brandmania.Activity.basics.LoginActivity;
import com.app.brandmania.Activity.basics.ReferNEarnActivity;
import com.app.brandmania.Activity.brand.UpdateBandList;
import com.app.brandmania.Activity.custom.CustomViewAllActivit;
import com.app.brandmania.Adapter.DasboardAddaptor;
import com.app.brandmania.Adapter.ViewPagerAdapter;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Interface.ItemMultipleSelectionInterface;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.DialogRequestBusinessCategoryRemarksBinding;
import com.app.brandmania.databinding.FragmentHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;


public class HomeFragment extends BaseFragment implements ItemMultipleSelectionInterface, ImageCateItemeInterFace, NegativeReviewListener, ReviewListener, SwipeRefreshLayout.OnRefreshListener {
    public static int BUSINESS_TYPE = 1;
    private String BusinessTitle;
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    DashBoardItem apiResponse;
    BrandListItem brandListItem;
    public String referralCode;
    private int[] layouts;
    AlertDialog.Builder alertDialogBuilder;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    FiveStarsDialog fiveStarsDialog;
    private DasboardAddaptor dasboardAddaptor;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    private RelativeLayout mTitleContainer;
    Activity act;
    public String Wallet;
    public String ReferalCode;
    PreafManager preafManager;
    private String deviceToken = "";
    private FragmentHomeBinding binding;
    Timer timer;
    private HomeFragment homeFragment;
    private SelectBrandListBottomFragment bottomSheetFragment;


    public interface CUSTOM_TAB_CHANGE_INTERFACE {
        void makeTabChange(int i);
    }

    public void getDeviceToken(Activity act) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                deviceToken = task.getResult();
                UpdateToken();
            }
        });
    }

    @Override
    public void onResume() {
        preafManager = new PreafManager(act);
        super.onResume();
    }

    @SuppressLint("CheckResult")
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, parent, false);
        homeFragment = this;
        fiveStarsDialog = new FiveStarsDialog(Objects.requireNonNull(getActivity()), "brandmania@gmail.com");
        preafManager = new PreafManager(act);

        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }
        if (preafManager.getActiveBrand() != null) {
            Glide.with(act).load(preafManager.getActiveBrand().getLogo()).into(binding.pdfLogo);
            //requestAgain();
            Glide.with(act).load(preafManager.getActiveBrand().getLogo());
        }
        RateUs();


        mTitleContainer = act.findViewById(R.id.main_linearlayout_title);
        // binding.alertText.setSelected(true);
        binding.showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HELPER.ROUTE(act, ViewNotificationActivity.class);
            }
        });
        binding.referCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, ReferNEarnActivity.class);
                startActivity(intent);
            }
        });

        binding.referralcodeTxt.setText(preafManager.getReferCode());

        binding.createDigitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preafManager.getActiveBrand().getLogo().isEmpty()) {
                    //layoutToImage();
                    HELPER.ROUTE(act, PdfActivity.class);
                    //HELPER.generatePDF(act, preafManager, ((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap());
                } else {
                    alertDialogBuilder = new AlertDialog.Builder(act);
                    alertDialogBuilder.setTitle("Save image");
                    alertDialogBuilder.setMessage("Your Logo is empty..!");
                    alertDialogBuilder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    HELPER.ROUTE(act, UpdateBandList.class);
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        binding.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequestForm();
            }
        });

        binding.createCustomImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.ROUTE(act, CustomViewAllActivit.class);
            }
        });

        binding.createGreetingImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CUSTOM_TAB_CHANGE_INTERFACE) Objects.requireNonNull(getActivity())).makeTabChange(1);
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
                loadImagesCategory();

            }
        });
        getDeviceToken(act);

        binding.businessNameDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentList(BUSINESS_TYPE, BusinessTitle);
            }
        });

        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.WHATSAPP_REDIRECTION(act, preafManager.getActiveBrand().getName(), preafManager.getMobileNumber());
            }
        });

        binding.contactTxtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.WHATSAPP_REDIRECTION(act, preafManager.getActiveBrand().getName(), preafManager.getMobileNumber());
            }
        });

        return binding.getRoot();
    }


    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.swipeContainer.setVisibility(View.GONE);
    }

    //Show Fragment For BrandList
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

    //Set Adaptor
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
//
//                        BannerAdapter bannerAdapter = new BannerAdapter(act,viewPagerItems);
//                        SnapHelper startSnapHelper = new PagerSnapHelper();
//                        binding.sliderRecycler.setHasFixedSize(true);
//                        binding.sliderRecycler.setOnFlingListener(null);
//                        startSnapHelper.attachToRecyclerView(binding.sliderRecycler);
//                        binding.sliderRecycler.setAdapter(bannerAdapter);

                        ViewPagerAdapter sliderAdapter = new ViewPagerAdapter(viewPagerItems, act);
                        binding.ViewPagerView.setAdapter(sliderAdapter);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                binding.ViewPagerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.ViewPagerView.setCurrentItem((binding.ViewPagerView.getCurrentItem() + 1) % sliderAdapter.getCount(), true);
                                    }
                                });
                            }
                        };

                        timer = new Timer();
                        timer.schedule(timerTask, 10000, 10000);

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
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    //GetImageCategory..................
    private void loadImagesCategory() {
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
                            binding.swipeContainer.setVisibility(View.VISIBLE);
                        }
                    }

                    if (apiResponse.getLinks() != null) {

                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            //  binding.shimmerForPagination.startShimmer();
                            // binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        } else {
                            // binding.shimmerForPagination.stopShimmer();
                            //  binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    } else {
                        //  binding.shimmerForPagination.stopShimmer();
                        //   binding.shimmerForPagination.setVisibility(View.GONE);
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
                        binding.swipeContainer.setVisibility(View.GONE);
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

    private void getImageCategoryNextPage(String nextPageUrl) {
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
                            // binding.shimmerForPagination.startShimmer();
                            //  binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        } else {
                            // binding.shimmerForPagination.stopShimmer();
                            // binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }
                    if (apiResponse.getDashBoardItems() == null || apiResponse.getDashBoardItems().size() == 0) {
                        //  binding.shimmerForPagination.stopShimmer();
                        // binding.shimmerForPagination.setVisibility(View.GONE);
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
                        //  binding.shimmerForPagination.stopShimmer();
                        // binding.shimmerForPagination.setVisibility(View.GONE);

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
                    JSONObject jsonArray1 = jsonObject.getJSONObject("message");
                    preafManager.setWallet(jsonArray1.getString("user_total_coin"));
                    preafManager.setReferCode(jsonArray1.getString("referal_code"));
                    if (jsonArray1.getString("reference_code").equals("null"))
                        jsonArray1.put("reference_code", "");

                    preafManager.setSpleshReferrer(jsonArray1.getString("reference_code"));
                    preafManager.setReferrerCode(jsonArray1.getString("reference_code"));

                    MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.APP_INTRO_REFRESH);
                    setupReferralCode();
                    //setupReferrerCode();
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

    public void setupReferralCode() {
        binding.referralcodeTxt.setText(preafManager.getReferCode());

        if (preafManager.getReferCode() != null && preafManager.getReferCode().isEmpty()) {
            binding.referralCardView.setVisibility(View.GONE);
        } else {
            binding.referralCardView.setVisibility(View.VISIBLE);
        }
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


    private void RateUs() {

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
                    if (preafManager.getActiveBrand() != null) {
                        for (int i = 0; i < multiListItems.size(); i++) {
                            if (multiListItems.get(i).getId().equalsIgnoreCase(preafManager.getActiveBrand().getId())) {
                                preafManager.setActiveBrand(multiListItems.get(i));
                                break;
                            }
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

                    startAnimation();
                    loadImagesCategory();
                    getBanner();

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
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    @Override
    public void onNegativeReview(int stars) {
        Log.d("TAG", "Negative review " + stars);
    }

    @Override
    public void onRefresh() {
        startAnimation();
        getFrame();
        loadImagesCategory();
        getBrandList();
        getBanner();
    }

    @Override
    public void onReview(int stars) {
        Log.d("TAG", "Review " + stars);
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


    private androidx.appcompat.app.AlertDialog alertDialog;
    private DialogRequestBusinessCategoryRemarksBinding reqBinding;

    public void showRequestForm() {

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();

        reqBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_request_business_category_remarks, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend2);
        builder.setView(reqBinding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(reqBinding.getRoot());

        Utility.RemoveError(reqBinding.nameTxt);
        reqBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        reqBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reqBinding.nameTxt.getText().toString().trim().length() == 0) {
                    reqBinding.nameTxt.setError("Enter category");
                    reqBinding.nameTxt.requestFocus();
                    return;
                }
                alertDialog.dismiss();
                apiForCategoryRequest(reqBinding.nameTxt.getText().toString());

            }
        });
        alertDialog.show();

    }


    private void apiForCategoryRequest(String catString) {
        Utility.Log("BusinessCategory", APIs.REQUEST_BUSINESS_CATEGORY);
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.REQUEST_BUSINESS_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("Request-Business-response", response);
                Utility.dismissProgress();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.dismissProgress();
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
                hashMap.put("cat_name", catString);
                Utility.Log("param", hashMap.toString());
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

}
