package com.app.brandmania.Activity.brand;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

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
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityViewBrandBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ViewBrandActivity extends BaseActivity {
    Activity act;
    private ActivityViewBrandBinding binding;
    private static final int REQUEST_CALL = 1;

    ArrayList<BrandListItem> multiListItems = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;

        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_brand);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Utility.isLiveModeOff(act);
        if (prefManager.getActiveBrand() != null) {

            binding.addBrandImage.setVisibility(View.VISIBLE);
            binding.swipeContainer.setVisibility(View.VISIBLE);
            binding.addBrandImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AddBrandMultipleActivity.class);
                    startActivity(intent);
                    act.overridePendingTransition(R.anim.right_enter, R.anim.left_out);
                }
            });

            binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                    R.color.colorsecond,
                    R.color.colorthird);
            binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    startAnimation();
                    getBrandList(false);
                }
            });

            startAnimation();
            getBrandList(false);

        } else {
            binding.addBrandImage.setVisibility(View.GONE);
            binding.swipeContainer.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);
            binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });
            animateButton();
        }

    }

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce_two);
        double animationDuration = 4 * 1000;
        myAnim.setRepeatCount(Animation.INFINITE);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);

        myAnim.setInterpolator(interpolator);

        // Animate the button
        binding.continueBtn.startAnimation(myAnim);


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

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.getBrandList.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }

    private void GetBrandAddaptor() {
        BrandAdapter MenuAddaptor = new BrandAdapter(multiListItems, this);
        BrandAdapter.BRANDBYIDIF brandbyidif = new BrandAdapter.BRANDBYIDIF() {
            @Override
            public void fireBrandList(int position, BrandListItem model) {
                getBrandById(model);
            }
        };
        MenuAddaptor.setBrandbyidif(brandbyidif);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.getBrandList.setHasFixedSize(true);
        binding.getBrandList.setLayoutManager(mLayoutManager);
        binding.getBrandList.setAdapter(MenuAddaptor);

    }

    private void getBrandList(boolean wantToLoadHome) {
        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_BRAND : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    if (multiListItems != null && multiListItems.size() != 0) {
                        GetBrandAddaptor();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.getBrandList.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                        prefManager.setAddBrandList(multiListItems);
                    }
                    if (multiListItems == null || multiListItems.size() == 0) {
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.getBrandList.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                    }
                    if (wantToLoadHome)
                        MakeMyBrandApp.getInstance().getObserver().setValue(ObserverActionID.REFRESH_BRAND_NAME);


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
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.getBrandList.setVisibility(View.GONE);
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
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  makePhoneCall();
            } else {
                Toast.makeText(act, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

    private void getBrandById(BrandListItem model) {

        Utility.Log("API : ", APIs.GET_BRAND_BY_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND_BY_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_BRAND_BY_ID : ", response);
                ArrayList<BrandListItem> brandListItems = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandById(jsonObject);


                    SliderItem sliderItem = new SliderItem();
                    sliderItem.setPriceForPay(multiListItems.get(0).getRate());
                    sliderItem.setPackageTitle(multiListItems.get(0).getPackagename());
                    sliderItem.setPackageid(multiListItems.get(0).getPackage_id());
                    sliderItem.setTemplateTitle(multiListItems.get(0).getNo_of_frame());
                    sliderItem.setImageTitle(multiListItems.get(0).getNo_of_total_image());
                    sliderItem.setBrandId(multiListItems.get(0).getId());

                    Gson gson = new Gson();

                    Intent i = new Intent(act, PackageActivity.class);

                    i.putExtra("detailsObj", gson.toJson(sliderItem));

                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

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
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id", model.getId());
                //Log.e("DateNdClass", params.toString());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.JUSTBRAND) {
            getBrandList(true);

        }
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.RELOAD_BRANDS) {
            getBrandList(false);
        }
    }
}