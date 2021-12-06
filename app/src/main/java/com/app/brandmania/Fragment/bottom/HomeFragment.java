package com.app.brandmania.Fragment.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

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
import com.app.brandmania.Activity.brand.EditActivity;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Timer timer;
    private HomeFragment homeFragment;
    private SelectBrandListBottomFragment bottomSheetFragment;


    public interface CUSTOM_TAB_CHANGE_INTERFACE {
        void makeTabChange(int i);
    }

    public void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            deviceToken = task.getResult();
            UpdateToken();
        });
    }

    @Override
    public void onResume() {
        act = getActivity();
        assert act != null;
        preafManager = new PreafManager(act);
        super.onResume();
    }

    @SuppressLint({"CheckResult", "SimpleDateFormat"})
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, parent, false);
        homeFragment = this;

        fiveStarsDialog = new FiveStarsDialog(act, "brandmania@gmail.com");
        preafManager = new PreafManager(act);
        binding.infoMsg.setSelected(true);

        if (preafManager.getAddBrandList() != null && preafManager.getAddBrandList().size() != 0) {
            if (preafManager.getActiveBrand() == null) {
                preafManager.setActiveBrand(preafManager.getAddBrandList().get(0));
                preafManager = new PreafManager(act);
            }
        }

        if (preafManager.getActiveBrand() != null) {
            Glide.with(act).load(preafManager.getActiveBrand().getLogo());
            binding.businessName.setText(preafManager.getActiveBrand().getName());
        }

        if (preafManager.getActiveBrand() == null) {
            if (!HomeActivity.isAddBrandDialogDisplayed) {
                HomeActivity.isAddBrandDialogDisplayed = true;
                addBrandList();
            }
        }
        RateUs();


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
            HELPER.ROUTE(act, PdfActivity.class);
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
        getDeviceToken();
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
        return binding.getRoot();
    }

    AddBrandFragment addBrandFragment;

    public void addBrandList() {
        if (addBrandFragment != null) {
            if (addBrandFragment.isVisible()) {
                addBrandFragment.dismiss();
            }
        }

        addBrandFragment = new AddBrandFragment();
        addBrandFragment.show(getParentFragmentManager(), "");
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
        Utility.Log("TokenURL", APIs.UPDATE_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_TOKEN, response -> {

            JSONObject jsonObject = ResponseHandler.createJsonObject(response);
            try {
                if (jsonObject != null) {
                    preafManager.setAppTutorial(ResponseHandler.getString(ResponseHandler.getJSONArray(jsonObject, "data").getJSONObject(0), "video_url_path"));

                    JSONObject jsonArray1 = jsonObject.getJSONObject("message");
                    preafManager.setWallet(jsonArray1.getString("user_total_coin"));
                    preafManager.setReferCode(jsonArray1.getString("referal_code"));
                    popupImg = jsonArray1.getString("popup_img");
                    isActivityStatus = jsonArray1.getString("is_activity");
                    targetLink = jsonArray1.getString("target_link");
                    if (jsonArray1.getString("reference_code").equals("null"))
                        jsonArray1.put("reference_code", "");

                    preafManager.setSpleshReferrer(jsonArray1.getString("reference_code"));
                    preafManager.setReferrerCode(jsonArray1.getString("reference_code"));

                    MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.APP_INTRO_REFRESH);
                    setupReferralCode();
                    if (!act.isFinishing() && !act.isDestroyed() && homeFragment.isVisible() && !HomeActivity.isAlreadyDisplayed) {
                        if (popupImg != null && !popupImg.isEmpty()) {
                            setOfferCode();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Utility.Log("responseBody", responseBody);

                    JSONObject data = new JSONObject(responseBody);
                    JSONArray errors = data.getJSONArray("errors");
                    JSONObject jsonMessage = errors.getJSONObject(0);
                    String message = jsonMessage.getString("message");
                    Toast.makeText(act, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                } catch (UnsupportedEncodingException errorr) {
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
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

        dialogOfferBinding.closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                binding.swipeContainer.setRefreshing(false);
                multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
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
