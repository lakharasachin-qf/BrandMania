package com.app.brandmania.Activity.packages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Adapter.PackageRecyclerAdapter;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MySingleton;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPackageBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.views.MyBounceInterpolator;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends BaseActivity {
    private Activity act;
    private ActivityPackageBinding binding;
    private int[] layouts;

    private boolean isLoading = false;
    ArrayList<SliderItem> sliderItems = new ArrayList<>();
    BrandListItem selectedBrand;
    Gson gson;
    int layoutType = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        gson = new Gson();

        if (getIntent().hasExtra("fromBrandList")) {
            layoutType = 2;
            selectedBrand = gson.fromJson(getIntent().getStringExtra("detailsObj"), BrandListItem.class);
        } else if (prefManager.getActiveBrand() != null) {
            layoutType = 0;
            selectedBrand = prefManager.getActiveBrand();
        } else {
            layoutType = 0;
            selectedBrand = null;
        }
        binding = DataBindingUtil.setContentView(act, R.layout.activity_package);
        binding.BackButton.setOnClickListener(v -> onBackPressed());
        GetPackageList();
        if (prefManager.getActiveBrand() == null) {
            binding.addBrand.setVisibility(View.VISIBLE);
            animateButton();
            binding.addBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HELPER.ROUTE(act, AddBrandMultipleActivity.class);
                }
            });
            //binding.brandTxt.setText(Html.fromHtml("  Add" + "<font color=\"#faa81e\">" + "<b>" + "<br>" + "Brand" + "</b>" + "</font>"));
        }

    }

    void animateButton() {
        final Animation myAnim = AnimationUtils.loadAnimation(act, R.anim.bounce_outer);
        double animationDuration = 4 * 1000;
        myAnim.setRepeatCount(Animation.INFINITE);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(1, 10);
        myAnim.setInterpolator(interpolator);
        binding.addBrand.startAnimation(myAnim);
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

    private void GetPackage() {
        binding.viewPagerImageSlider.setVisibility(View.GONE);
        binding.recyclerList.setVisibility(View.VISIBLE);
        Collections.reverse(sliderItems);
        PackageRecyclerAdapter dashboardAdaptor = new PackageRecyclerAdapter(sliderItems, act, selectedBrand);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(mLayoutManager);
        binding.recyclerList.setAdapter(dashboardAdaptor);
    }

    private void GetPackageList() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_PACKAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    sliderItems = ResponseHandler.HandleGetPackageList(jsonObject);
                    if (sliderItems != null && sliderItems.size() != 0) {
                        GetPackage();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isLoading = false;
                        Utility.dismissProgress();
                        error.printStackTrace();
                    }
                }
        ) {
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
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);

    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}