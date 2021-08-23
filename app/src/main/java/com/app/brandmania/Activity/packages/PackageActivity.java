package com.app.brandmania.Activity.packages;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Adapter.SliderAdapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.ActivityPackageBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageActivity extends BaseActivity {
    private Activity act;
    private ActivityPackageBinding  binding;
    private int[] layouts;
    PreafManager preafManager;
    private boolean isLoading = false;
    ArrayList<SliderItem>sliderItems=new ArrayList<>();
    String selectedBrand;
    Gson gson;
    int layoutType=0;
    //0 = profile,viewall, 2= brand list
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        gson=new Gson();
        preafManager=new PreafManager(act);
        if (getIntent().hasExtra("fromBrandList")){
            layoutType=2;
            selectedBrand=gson.fromJson(getIntent().getStringExtra("detailsObj"),BrandListItem.class).getId();
        }
        else{
            //for profile
            layoutType=0;
            selectedBrand=preafManager.getActiveBrand().getId();
        }

      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_package);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GetPackageList();

    }
    private void GetPackage() {

        binding.viewPagerImageSlider.setAdapter(new SliderAdapter(sliderItems,act,selectedBrand));
        binding.viewPagerImageSlider.setClipToPadding(false);
        binding.viewPagerImageSlider.setClipChildren(false);
        binding.viewPagerImageSlider.setOffscreenPageLimit(2);
        binding.viewPagerImageSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.viewPagerImageSlider.setPageTransformer(compositePageTransformer);

    }


    private void GetPackageList() {
        if (isLoading)
            return;
        isLoading = true;
        Utility.showProgress(act);
        Utility.Log("API : ", APIs.GET_PACKAGE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_PACKAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading = false;
                Utility.dismissProgress();
                Utility.Log("GET_PACKAGE : ", response);
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
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}