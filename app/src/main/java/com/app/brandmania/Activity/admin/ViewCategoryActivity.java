package com.app.brandmania.Activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.app.brandmania.Activity.brand.AddBrandMultipleActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.SliderItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityViewBrandBinding;
import com.app.brandmania.databinding.ActivityViewCategoryBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ViewCategoryActivity extends BaseActivity {
    Activity act;
    private ActivityViewCategoryBinding binding;
    ArrayList<CatModel> multiListItems=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_view_category);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.getBrandList.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }
    private void GetBrandAddaptor() {
        CategoryAdapter MenuAddaptor = new CategoryAdapter(multiListItems, this);
        CategoryAdapter.BRANDBYIDIF brandbyidif=new CategoryAdapter.BRANDBYIDIF() {
            @Override
            public void fireBrandList(int position, BrandListItem model) {
               // getBrandById(model);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_CATEGORY_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("Category : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray festivalArray = jsonObject.getJSONObject("data").getJSONArray("Festival Images");
                    JSONArray dailyArray = jsonObject.getJSONObject("data").getJSONArray("Daily Images");
                    JSONArray businessArray = jsonObject.getJSONObject("data").getJSONArray("Business Images");
                    for (int i=0;i<festivalArray.length();i++){
                        JSONObject inn=festivalArray.getJSONObject(i);
                        CatModel catModel=new CatModel();
                        catModel.setImageType("Festival Images");
                        catModel.setId(inn.getString("id"));
                        catModel.setCategoryName(inn.getString("name"));
                        catModel.setActive(!inn.getString("is_active").equals("0"));
                        catModel.setFree(!inn.getString("is_free").equals("0"));
                        multiListItems.add(catModel);
                    }


                    if (multiListItems != null && multiListItems.size() != 0) {
                      //  GetBrandAddaptor();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.getBrandList.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                    }
                    if (multiListItems == null || multiListItems.size() == 0) {
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.getBrandList.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                Log.e("Token",params.toString());
                return params;
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

    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

}