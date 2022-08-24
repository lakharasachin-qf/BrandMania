package com.app.brandmania.Fragment.bottom;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Activity.PdfActivity;
import com.app.brandmania.Activity.ViewNotificationActivity;
import com.app.brandmania.Activity.basics.ReferNEarnActivity;
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Activity.brand.UpdateBandList;
import com.app.brandmania.Activity.custom.CustomViewAllActivit;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.DasboardAddaptor;
import com.app.brandmania.Adapter.ViewPagerAdapter;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.AddBrandFragment;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Fragment.UserNewRegistrationFragment;
import com.app.brandmania.Fragment.UserRegistrationFragment;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Interface.ItemMultipleSelectionInterface;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.Model.ViewPagerItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.DialogOfferBinding;
import com.app.brandmania.databinding.DialogRequestBusinessCategoryRemarksBinding;
import com.app.brandmania.databinding.FragmentHomeBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import angtrim.com.fivestarslibrary.FiveStarsDialog;
import angtrim.com.fivestarslibrary.NegativeReviewListener;
import angtrim.com.fivestarslibrary.ReviewListener;


public class HomeFragment extends BaseFragment implements ItemMultipleSelectionInterface, ImageCateItemeInterFace, NegativeReviewListener, ReviewListener, SwipeRefreshLayout.OnRefreshListener {
    public static int BUSINESS_TYPE = 1;
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    DashBoardItem apiResponse;
    BrandListItem brandListItem;
    ArrayList<BrandListItem> multiListItems = new ArrayList<>();
    FiveStarsDialog fiveStarsDialog;
    private DasboardAddaptor dasboardAddaptor;
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
    Activity act;
    PreafManager preafManager;
    private String deviceToken = "";
    private String popupImg;
    private String isActivityStatus;
    private String targetLink;
    private FragmentHomeBinding binding;
    private boolean LIVE_MODE = true;
    private Timer timer;
    private HomeFragment homeFragment;
    static Calendar now = Calendar.getInstance();
    private SelectBrandListBottomFragment bottomSheetFragment;


    public interface CUSTOM_TAB_CHANGE_INTERFACE {
        void makeTabChange(int i);
    }

    public void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                deviceToken = task.getResult();

            UpdateToken();
        });
    }

    @Override
    public void onResume() {
        act = getActivity();
        assert act != null;
        preafManager = new PreafManager(act);
        if (userRegistrationFragment != null) {
            if (userRegistrationFragment.isVisible()) {
                userRegistrationFragment.dismiss();
            }
        }
        super.onResume();
    }

    @SuppressLint({"CheckResult", "SimpleDateFormat"})
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, parent, false);
        homeFragment = this;
        fiveStarsDialog = new FiveStarsDialog(act, "brandmania@gmail.com");
        homeLoad();
        return binding.getRoot();
    }

    AddBrandFragment addBrandFragment;

    public UserNewRegistrationFragment userRegistrationFragment;

    public void homeLoad() {
        preafManager = new PreafManager(act);
        binding.infoMsg.setSelected(true);

        if (LIVE_MODE) {
            act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
        setActiveBrand();
        if (preafManager.getActiveBrand() != null) {
            Glide.with(act).load(preafManager.getActiveBrand().getLogo());
            binding.businessName.setText(preafManager.getActiveBrand().getName());
        }
        if (ContextCompat.checkSelfPermission(act, CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (preafManager.getUserName().isEmpty()) {
                newUserRegistration();
                //addUserInfo();
            } else if (preafManager.getActiveBrand() == null) {
                if (!HomeActivity.isAddBrandDialogDisplayed) {
                    HomeActivity.isAddBrandDialogDisplayed = true;
                    addBrandList();
                }
            } else {
                RateUs();
            }
        }
        Utility.Log("Token::::", prefManager.getUserToken());

        binding.showNotification.setOnClickListener(view -> HELPER.ROUTE(act, ViewNotificationActivity.class));
        binding.referCodeLayout.setOnClickListener(v -> {
            if (preafManager.getActiveBrand() != null) {
                HELPER.ROUTE(act, ReferNEarnActivity.class);
            } else {
                addBrandList();
            }
        });

        binding.referralcodeTxt.setText(preafManager.getReferCode());

        binding.createDigitalCard.setOnClickListener(view -> {
            if (preafManager.getActiveBrand() != null) {
                if (!preafManager.getActiveBrand().getLogo().isEmpty()) {
                    HELPER.ROUTE(act, PdfActivity.class);
                } else {
                    androidx.appcompat.app.AlertDialog AlertDialogBuilder = new MaterialAlertDialogBuilder(act, R.style.RoundShapeTheme)
                            .setTitle("Add Your Logo")
                            .setMessage("Your Logo is empty..!")
                            .setPositiveButton("OK", (dialogInterface, i) -> HELPER.ROUTE(act, UpdateBandList.class))
                            .setNeutralButton("LATER", (dialogInterface, i) -> {
                            })
                            .show();
                    AlertDialogBuilder.setCancelable(false);

//                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(act);
//                    alertDialogBuilder.setTitle("Add Your Logo");
//                    alertDialogBuilder.setMessage("Your Logo is empty..!");
//                    alertDialogBuilder.setPositiveButton("Ok", (arg0, arg1) -> HELPER.ROUTE(act, UpdateBandList.class));
//                    alertDialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.setCancelable(false);
//                    alertDialog.show();
                }
            } else {
                addBrandList();
            }
        });
        binding.request.setOnClickListener(v -> showRequestForm());
        binding.createCustomImages.setOnClickListener(v -> HELPER.ROUTE(act, CustomViewAllActivit.class));
        binding.createGreetingImages.setOnClickListener(v -> ((CUSTOM_TAB_CHANGE_INTERFACE) act).makeTabChange(1));
        startAnimation();
        loadImagesCategory();
        getBanner();
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorsecond, R.color.colorthird);

        binding.swipeContainer.setOnRefreshListener(() -> {
            startAnimation();
            loadImagesCategory();
        });

        //if (Utility.oneTimeCodeExecutes(act)) {
        //Toast.makeText(act, "Only Once A Day Code Run", Toast.LENGTH_LONG).show();
        getDeviceToken();
        //}
        //getDeviceToken();
        binding.businessNameDropDown.setOnClickListener(v -> showFragmentList(BUSINESS_TYPE, ""));

        if (!HomeActivity.isAlreadyDisplayedOffer) {
            try {
                String offerValidDate = "05/11/2021";
                Date calDate = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter;
                formatter = new SimpleDateFormat("dd/MM/yyyy");
                String calDateStr = formatter.format(calDate);

                Date calDateFF;
                calDateFF = formatter.parse(calDateStr);
                Date offerValidDateFF = formatter.parse(offerValidDate);
                assert calDateFF != null;
                if (calDateFF.compareTo(offerValidDateFF) < 0) {
                    diwaliOffer();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (preafManager.getActiveBrand() != null) {
            if (preafManager.getActiveBrand().getExpiery_date() != null && !preafManager.getActiveBrand().getExpiery_date().isEmpty() && Utility.isPackageExpired(act)) {
                binding.infoMsg.setText("                           Dear user, your current package is expired on date " + preafManager.getActiveBrand().getExpiery_date() + ". Please Upgrade your plan and enjoy downloading image, GIF and videos.");
                binding.tapActionBtn.setVisibility(View.VISIBLE);
                binding.easyMessage.setVisibility(View.VISIBLE);
                binding.easyMessage.setOnClickListener(v -> {
                    HELPER.ROUTE(act, PackageActivity.class);
                });
            }
        }
        setActiveBrandText();
    }


    public void setActiveBrandText() {

        if (preafManager.getActiveBrand() == null) {
            binding.businessNameDropDown.setVisibility(View.GONE);
            binding.firsttitle.setVisibility(View.GONE);
            binding.noBrandLayout.setVisibility(View.VISIBLE);
            animateButton();
            binding.noBrandLayout.setOnClickListener(v -> {
                HELPER.ROUTE(act, AddBrandMultipleActivity.class);
            });

        } else {
            binding.businessNameDropDown.setVisibility(View.VISIBLE);
            binding.firsttitle.setVisibility(View.VISIBLE);
            binding.noBrandLayout.setVisibility(View.GONE);

        }

    }

    public void newUserRegistration() {

        if (userRegistrationFragment != null) {
            if (userRegistrationFragment.isVisible()) {
                userRegistrationFragment.dismiss();
            }

        }
        userRegistrationFragment = new UserNewRegistrationFragment();
        userRegistrationFragment.show(getParentFragmentManager(), "");
    }

    public void setActiveBrand() {

        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }
    }

    public void addBrandList() {
        if (addBrandFragment != null) {
            if (bottomSheetFragment.isVisible()) {
                bottomSheetFragment.dismiss();
            }

        }
        addBrandFragment = new AddBrandFragment();
        addBrandFragment.show(getParentFragmentManager(), "");
    }

    UserRegistrationFragment registrationFragment;

    public void addUserInfo() {
        if (registrationFragment != null) {
            if (registrationFragment.isVisible()) {
                registrationFragment.dismiss();
            }
        }

        registrationFragment = new UserRegistrationFragment();
        registrationFragment.show(getParentFragmentManager(), "");
    }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.swipeContainer.setVisibility(View.GONE);
    }

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

    public void setAdapter() {
        dasboardAddaptor = new DasboardAddaptor(menuModels, act);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.rocommRecycler.setHasFixedSize(true);

        binding.rocommRecycler.setLayoutManager(mLayoutManager);
        binding.rocommRecycler.setAdapter(dasboardAddaptor);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void getBanner() {
        binding.swipeContainer.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_BANNER, response -> {
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
                            binding.ViewPagerView.post(() -> binding.ViewPagerView.setCurrentItem((binding.ViewPagerView.getCurrentItem() + 1) % sliderAdapter.getCount(), true));
                        }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask, 10000, 10000);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }
        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void loadImagesCategory() {
        Utility.Log("API : ", APIs.GET_IMAGE_CATEGORY + "?page=1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGE_CATEGORY + "?page=1", response -> {
            binding.swipeContainer.setRefreshing(false);
            try {
                //Log.e("dashboard", gson.toJson(response));
                JSONObject jsonObject = new JSONObject(response);
                apiResponse = ResponseHandler.HandleGetImageCategory(act, jsonObject);
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
                        getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();
                    binding.swipeContainer.setRefreshing(false);
                    binding.swipeContainer.setVisibility(View.GONE);
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }
        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void getImageCategoryNextPage(String nextPageUrl) {
        Utility.Log("API-", nextPageUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, nextPageUrl, response -> {
            binding.swipeContainer.setRefreshing(false);
            Utility.Log("GET_IMAGE_CATEGORY : ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                apiResponse = ResponseHandler.HandleGetImageCategory(act, jsonObject);
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
                    if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                        getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }
        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private void UpdateToken() {
        Utility.Log("UpdateToken", APIs.UPDATE_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_TOKEN, response -> {
            JSONObject jsonObject = ResponseHandler.createJsonObject(response);
            Log.e("UpdateToken::::::", gson.toJson(jsonObject));
            try {
                if (jsonObject != null) {
                    preafManager.setAppTutorial(ResponseHandler.getString(ResponseHandler.getJSONArray(jsonObject, "data").getJSONObject(0), "video_url_path"));

                    JSONObject jsonArray1 = jsonObject.getJSONObject("message");
                    preafManager.setUserName(jsonArray1.getString("name"));
                    preafManager.setUserEmail_Id(jsonArray1.getString("email"));
                    preafManager.setUserMobileNumber(jsonArray1.getString("phone"));
                    preafManager.setWallet(jsonArray1.getString("user_total_coin"));
                    preafManager.setReferCode(jsonArray1.getString("referal_code"));
                    popupImg = jsonArray1.getString("popup_img");
                    isActivityStatus = jsonArray1.getString("is_activity");
                    targetLink = jsonArray1.getString("target_link");
                    if (jsonArray1.getString("reference_code").equals("null"))
                        jsonArray1.put("reference_code", "");

                    if (!preafManager.getUserName().isEmpty()) {
                        preafManager.setSpleshReferrer(jsonArray1.getString("reference_code"));
                        preafManager.setReferrerCode(jsonArray1.getString("reference_code"));
                    }

                    //preafManager.setImageCounter("1");
                    preafManager.setImageCounter(jsonArray1.getString("image_counter"));
                    preafManager.setDaysCounter(jsonArray1.getString("days_counter"));
                    //preafManager.setDaysCounter("15");

                    MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.APP_INTRO_REFRESH);
                    setupReferralCode();
                    if (!act.isFinishing() && !act.isDestroyed() && homeFragment.isVisible() && !HomeActivity.isAlreadyDisplayed) {
                        if (popupImg != null && !popupImg.isEmpty()) {
                            Handler handler = null;
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // Utility.Log("OfferDialog","yessssssss");
                                    setOfferCode();
                                }
                            }, 10000);

                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                //Utility.Log("UpdateTokenError","Occurred");
                //if Errors occurred than go to Login Page

//                prefManager.Logout();
//                Intent i = new Intent(act, LoginActivity.class);
//                i.addCategory(Intent.CATEGORY_HOME);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
//                act.finish();

//                try {
//                    if (error.networkResponse.statusCode == 500 && error.networkResponse.data != null) {
//                        String responseBody = new String(error.networkResponse.data, "utf-8");
//                        Utility.Log("responseBody", responseBody);
//
//                        JSONObject data = new JSONObject(responseBody);
//                        JSONArray errors = data.getJSONArray("errors");
//                        JSONObject jsonMessage = errors.getJSONObject(0);
//                        String message = jsonMessage.getString("message");
//                    }
//                } catch (JSONException | UnsupportedEncodingException ignored) {
//                    ignored.printStackTrace();
//                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                if (deviceToken != null)
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

    public DialogOfferBinding dialogOfferBinding;

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce);
        double animationDuration = 4 * 1000;
        //myAnim.setDuration((long)animationDuration);
        myAnim.setRepeatCount(Animation.INFINITE);

        // Use custom animation interpolator to achieve the bounce effect
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.noBrandLayout.startAnimation(myAnim);


        // Run button animation again after it finished
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                animateButton();
            }
        });
    }

    public void setOfferCode() {
        HomeActivity.isAlreadyDisplayed = true;
        dialogOfferBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_offer, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle);
        builder.setView(dialogOfferBinding.getRoot());
        alertDialog = builder.create();
        alertDialog.setContentView(dialogOfferBinding.getRoot());
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Glide.with(act).load(popupImg).placeholder(R.drawable.place_holder_vertical).into(dialogOfferBinding.offerImage);
        alertDialog.show();

        dialogOfferBinding.offerImageLayout.setOnClickListener(v -> {
            alertDialog.dismiss();

            if (!popupImg.isEmpty() && !popupImg.equals("null")) {
                if (isActivityStatus.equalsIgnoreCase("0")) {
                    Uri webpage = Uri.parse(targetLink);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    intent.setPackage("com.android.chrome");
                    startActivity(intent);

                } else {

                    String nameOfActivity = targetLink;
                    //String nameOfActivity = "com.app.brandmania.Activity.packages.PackageActivity";
                    try {
                        Class<?> aClass = Class.forName(nameOfActivity);
                        Intent i = new Intent(act, aClass);
                        act.startActivity(i);
                        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
        });
        dialogOfferBinding.closeLayout.setOnClickListener(v -> alertDialog.dismiss());
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
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
        Utility.Log("GetBrandList:::", APIs.GET_BRAND);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, response -> {
            try {

                JSONObject jsonObject = new JSONObject(response);
                binding.swipeContainer.setRefreshing(false);
                multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                Utility.Log("GetBrandList:::", gson.toJson(multiListItems));

                if (multiListItems != null && multiListItems.size() != 0)
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
                if (preafManager.getActiveBrand() != null)

                    binding.businessName.setText(preafManager.getActiveBrand().getName());

                if (act.getIntent().hasExtra("FirstLogin")) {
                    preafManager.setIS_Brand(true);
                    if (multiListItems.size() != 0) {
                        preafManager.setActiveBrand(multiListItems.get(0));
                    }
                }
                if (preafManager.getActiveBrand() == null) {
                    if (multiListItems.size() != 0) {
                        preafManager.setActiveBrand(multiListItems.get(0));
                    }
                }
                //setActiveBrand();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();
                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
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
        //getFrame();
        loadImagesCategory();
        //getBrandList();
        getBanner();
    }

    @Override
    public void onReview(int stars) {
    }

    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", preafManager.getActiveBrand().getId());
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
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_IMAGE_CATEGORY_DATA) {
            if (preafManager.getActiveBrand() != null) {
                act.recreate();
                setActiveBrandText();
            }
        }

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.NOTIFY) {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dasboardAddaptor != null) {
                        dasboardAddaptor.notifyDataSetChanged();
                    }
                }
            });
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
        reqBinding.close.setOnClickListener(v -> alertDialog.dismiss());
        reqBinding.submit.setOnClickListener(v -> {
            if (reqBinding.nameTxt.getText().toString().trim().length() == 0) {
                reqBinding.nameTxt.setError("Enter category");
                reqBinding.nameTxt.requestFocus();
                return;
            }
            alertDialog.dismiss();
            apiForCategoryRequest(reqBinding.nameTxt.getText().toString());
            Toast.makeText(act, "Thanks for request we will contact you soon", Toast.LENGTH_SHORT).show();
        });
        alertDialog.show();

    }

    private void apiForCategoryRequest(String catString) {
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.REQUEST_BUSINESS_CATEGORY, response -> Utility.dismissProgress(), error -> {
            Utility.dismissProgress();
            error.printStackTrace();
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cat_name", catString);
                return hashMap;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    private androidx.appcompat.app.AlertDialog offerAlert;
    private DialogOfferBinding offerBinding;

    public void diwaliOffer() {
        if (offerAlert != null && offerAlert.isShowing())
            offerAlert.dismiss();
        HomeActivity.isAlreadyDisplayedOffer = true;
        offerBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_offer, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend2);
        builder.setView(offerBinding.getRoot());
        offerAlert = builder.create();
        offerAlert.setContentView(offerBinding.getRoot());
        offerBinding.closeLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.colorthird)));
        offerBinding.closeView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(act, R.color.white)));
        offerBinding.offerImage.setImageDrawable(ContextCompat.getDrawable(act, R.drawable.diwali_offer));
        offerBinding.offerImage.setOnClickListener(v -> {
            offerAlert.dismiss();
            Intent intent = new Intent(act, PackageActivity.class);
            startActivity(intent);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        offerBinding.closeLayout.setOnClickListener(v -> offerAlert.dismiss());
        offerAlert.show();
    }

}
