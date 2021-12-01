package com.app.brandmania.Activity.packages;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.app.brandmania.Adapter.PackageRecyclerAdapter;
import com.app.brandmania.Adapter.SliderAdapter;
import com.app.brandmania.Common.MySingleton;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityPackageBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
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
    PreafManager preafManager;
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

        preafManager = new PreafManager(act);

        if (getIntent().hasExtra("fromBrandList")) {
            layoutType = 2;
            selectedBrand = gson.fromJson(getIntent().getStringExtra("detailsObj"), BrandListItem.class);
        } else {
            layoutType = 0;
            selectedBrand = preafManager.getActiveBrand();
        }

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
        binding.viewPagerImageSlider.setVisibility(View.GONE);
        binding.recyclerList.setVisibility(View.VISIBLE);

        Collections.reverse(sliderItems);

        PackageRecyclerAdapter dasboardAddaptor = new PackageRecyclerAdapter(sliderItems, act, selectedBrand);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);

        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(mLayoutManager);
        binding.recyclerList.setAdapter(dasboardAddaptor);

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("X-Authorization", "Bearer " + preafManager.getUserToken());
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Utility.Log("POSTED-PARAMS-", params.toString());
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