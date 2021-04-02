package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.app.brandmania.Activity.ViewNotificationActivity;
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
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentHomeBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class HomeFragment extends BaseFragment implements ItemMultipleSelectionInterface , ImageCateItemeInterFace, NegativeReviewListener, ReviewListener,SwipeRefreshLayout.OnRefreshListener {
    public static int BUSINESS_TYPE = 1;
    private String BusinessTitle;
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    DashBoardItem apiResponse;
    BrandListItem brandListItem;
    private int[] layouts;
    AlertDialog.Builder alertDialogBuilder;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    FiveStarsDialog fiveStarsDialog;

    private DasboardAddaptor dasboardAddaptor;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    private RelativeLayout mTitleContainer;
    Activity act;
    PreafManager preafManager;
    private String deviceToken = "";
    private FragmentHomeBinding binding;
    Timer timer;
    private HomeFragment homeFragment;
    private SelectBrandListBottomFragment bottomSheetFragment;


    public interface CUSTOM_TAB_CHANGE_INTERFACE{
        void makeTabChange(int i);
    }

    public void getDeviceToken(Activity act) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(act, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        deviceToken = instanceIdResult.getToken();
                        UpdateToken();
                    }
                });
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
        Glide.with(act).load(preafManager.getActiveBrand().getLogo()).into(binding.pdfLogo);
        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }
        //requestAgain();
        RateUs();
        Glide.with(act).load(preafManager.getActiveBrand().getLogo());
        mTitleContainer = act.findViewById(R.id.main_linearlayout_title);
        // binding.alertText.setSelected(true);
        binding.showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HELPER.ROUTE(act, ViewNotificationActivity.class);
            }
        });

        binding.createDigitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!preafManager.getActiveBrand().getLogo().isEmpty()) {
                    HELPER.generatePDF(act, preafManager, ((BitmapDrawable) binding.pdfLogo.getDrawable()).getBitmap());
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
        // AddUserActivity();

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
        binding.rocommRecycler.setVisibility(View.GONE);
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
                            binding.rocommRecycler.setVisibility(View.VISIBLE);
                        }
                    }

                    if (apiResponse.getLinks() != null) {

                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
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
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
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

    /* private void requestAgain() {
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
 */
    @Override
    public void onNegativeReview(int stars) {
        Log.d(TAG, "Negative review " + stars);
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
        Log.d(TAG, "Review " + stars);
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





}
