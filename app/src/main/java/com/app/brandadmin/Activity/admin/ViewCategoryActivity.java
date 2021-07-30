package com.app.brandadmin.Activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

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
import com.app.brandadmin.Activity.admin.CategoryAdapter.handleSelectionEvent;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.APIs;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivityViewCategoryBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCategoryActivity extends BaseActivity {
    Activity act;
    private ActivityViewCategoryBinding binding;
    ArrayList<CatModel> multiListItems=new ArrayList<>();
    CatModel catlistmodel;


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

    private void categoryApi() {
        Utility.Log("API", APIs.ACTIVATE_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.ACTIVATE_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 Log.e("ACTIVATE_CATAGORY" , response);
               }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id" , catlistmodel.getId());
                if (catlistmodel.isActive())
                     params.put("status", String.valueOf(0));
                else {
                    params.put("status", String.valueOf(1));
                }
                Log.e("status_category" , params.toString());
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
        }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.getBrandList.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }
    private void GetBrandAddaptor() {
        CategoryAdapter MenuAddaptor = new CategoryAdapter(R.layout.item_catagory_list,multiListItems,act);

        CategoryAdapter.handleSelectionEvent handleSelectionEvent = new handleSelectionEvent() {
            @Override
            public void selectionEvent(CatModel itemmodel, int position, String flag) {
                catlistmodel = itemmodel;
               if (flag.equals("Active")){
                   categoryApi();
               }
               if (flag.equals("Inactive")){
                   categoryApi();
               }

//                Map<String, String> params = new HashMap<>();
//                params.put("id" , itemmodel.getId());
//
//                if (itemmodel.isActive())
//                     params.put("status", String.valueOf(0));
//                else {
//                    params.put("status", String.valueOf(1));
//                }
//                Log.e("GET_Category" , params.toString());
            }
        };

        MenuAddaptor.setInteface(handleSelectionEvent);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.getBrandList.setHasFixedSize(true);
        binding.getBrandList.setLayoutManager(mLayoutManager);
        binding.getBrandList.setAdapter(MenuAddaptor);


        binding.searchcategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MenuAddaptor.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void getBrandList(boolean wantToLoadHome) {
//        Utility.Log("API : ", APIs.GET_CATEGORY_ADMIN);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_CATEGORY_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.e("GET_CATEGORY_ADMIN" , response);
                binding.swipeContainer.setRefreshing(false);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");

                    JSONArray festivalArray = jsondata.getJSONArray("Festival Images");
                    JSONArray dailyArray = jsondata.getJSONArray("Daily Images");
                    JSONArray businessArray = jsondata.getJSONArray("Business Images");

//                    JSONArray festivalArray = jsonObject.getJSONObject("data").getJSONArray("Festival Images");
//                    JSONArray dailyArray = jsonObject.getJSONObject("data").getJSONArray("Daily Images");
//                    JSONArray businessArray = jsonObject.getJSONObject("data").getJSONArray("Business Images");

                    for (int i=0;i<festivalArray.length();i++){
                        JSONObject inn=festivalArray.getJSONObject(i);
                        CatModel catModel=new CatModel();
                        catModel.setImageType("Festival Images");
                        catModel.setlayoutType(0);
                        catModel.setId(inn.getString("id"));
                        catModel.setCategoryName(inn.getString("name"));
                        catModel.setThumbnailUrl(inn.getString("thumbnail_url"));
                        catModel.setActive(inn.getString("is_active").equals("0"));
                        catModel.setFree(!inn.getString("is_free").equals("0"));
                        multiListItems.add(catModel);
                         Log.e("categoryList" , new Gson().toJson(multiListItems));
                    }

                    for (int i=0;i<dailyArray.length();i++){
                        JSONObject inn=dailyArray.getJSONObject(i);
                        CatModel catModel=new CatModel();
                        catModel.setlayoutType(1);
                        catModel.setImageType("Daily Images");
                        catModel.setId(inn.getString("id"));
                        catModel.setCategoryName(inn.getString("name"));
                        catModel.setThumbnailUrl(inn.getString("thumbnail_url"));
                        catModel.setActive(!inn.getString("is_active").equals("0"));
                        catModel.setFree(!inn.getString("is_free").equals("0"));
                        multiListItems.add(catModel);
                    }

                    for (int i=0;i<businessArray.length();i++){
                        JSONObject inn=businessArray.getJSONObject(i);
                        CatModel catModel=new CatModel();
                        catModel.setImageType("Business Images");
                        catModel.setlayoutType(2);
                        catModel.setId(inn.getString("id"));
                        catModel.setCategoryName(inn.getString("name"));
                        catModel.setThumbnailUrl(inn.getString("thumbnail_url"));
                        catModel.setActive(!inn.getString("is_active").equals("0"));
                        catModel.setFree(!inn.getString("is_free").equals("0"));
                        multiListItems.add(catModel);
                    }


                    if (multiListItems != null && multiListItems.size() != 0) {
                        GetBrandAddaptor();
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