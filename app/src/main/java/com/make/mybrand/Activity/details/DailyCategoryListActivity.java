package com.make.mybrand.Activity.details;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.make.mybrand.Adapter.BusinessCategoryAdapter;
import com.make.mybrand.Common.MySingleton;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.Model.DashBoardItem;
import com.make.mybrand.Model.ImageList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityViewBusinessCategoryBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DailyCategoryListActivity extends BaseActivity {
    Activity act;
    private ActivityViewBusinessCategoryBinding binding;
    private DashBoardItem apiModel;
    private ArrayList<ImageList> menuModels;
    private ArrayList<ImageList> rootList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_business_category);

        if (getIntent().hasExtra("title"))
            binding.toolbarTitle.setText(getIntent().getStringExtra("title"));

        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorsecond, R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startAnimation();
                getBusinessCategory();
            }
        });

        binding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                startAnimation();
                getBusinessCategory();
            }
        });

        startAnimation();
        getBusinessCategory();
    }

    BusinessCategoryAdapter MenuAddaptor;

    private void setAdapter() {
        MenuAddaptor = new BusinessCategoryAdapter(apiModel, act, menuModels);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 3);
        binding.recyclerList.setHasFixedSize(true);
        binding.recyclerList.setLayoutManager(mLayoutManager);
        binding.recyclerList.setAdapter(MenuAddaptor);
    }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.recyclerList.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }

    private void getBusinessCategory() {

        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.DAILY_CATEGORY+"?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("response : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (ResponseHandler.isSuccess(null,jsonObject)) {

                        apiModel = ResponseHandler.handleBusinessCategory(act, jsonObject);

                        if (apiModel.getDashBoardItems() != null && apiModel.getDashBoardItems().size() != 0 && apiModel.getDashBoardItems().get(0).getDailyImages() != null && apiModel.getDashBoardItems().get(0).getDailyImages().size() != 0) {
                            rootList = ResponseHandler.handleBusinessCategory(act, jsonObject).getDashBoardItems().get(0).getDailyImages();
                            menuModels = ResponseHandler.handleBusinessCategory(act, jsonObject).getDashBoardItems().get(0).getDailyImages();
                            setAdapter();
                            binding.recyclerList.setVisibility(View.VISIBLE);
                            binding.emptyStateLayout.setVisibility(View.GONE);
                            binding.shimmerViewContainer.stopShimmer();
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                        } else {
                            binding.recyclerList.setVisibility(View.GONE);
                            binding.emptyStateLayout.setVisibility(View.VISIBLE);
                            binding.emptyStateMsg.setText("No Data Found");
                        }

                        if (apiModel.getLinks() != null) {
                            if (apiModel.getLinks().getNextPageUrl() != null && !apiModel.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiModel.getLinks().getNextPageUrl().isEmpty()) {
                                getImageCategoryNextPage(apiModel.getLinks().getNextPageUrl());
                            } else {
                                binding.shimmerViewContainer.stopShimmer();
                                binding.shimmerViewContainer.setVisibility(View.GONE);
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            binding.shimmerViewContainer.stopShimmer();
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }else{
                        binding.recyclerList.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.emptyStateMsg.setText("No Data Found");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerList.setVisibility(View.GONE);
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                    binding.emptyStateMsg.setText("No Data Found");
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        binding.swipeContainer.setRefreshing(false);
                        error.printStackTrace();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.recyclerList.setVisibility(View.GONE);
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.emptyStateMsg.setText("No Data Found");


                    }
                }
        ) {
            @Override
            public Map getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                String keywords = binding.searchEdt.getText().toString().replace(" ",",").replace(",",",");
                map.put("tag", keywords);
                Utility.Log("pram",map.toString());
                return map;
            }

        };
        stringRequest.setTag("search");
        MySingleton.getInstance(act).cancelPendingRequests("search");
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }
    private void getImageCategoryNextPage(String nextPageUrl) {
        Utility.Log("API-", nextPageUrl);
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, nextPageUrl, response -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.swipeContainer.setRefreshing(false);
            Utility.Log(" data : ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                DashBoardItem apiResponse = ResponseHandler.handleBusinessCategory(act, jsonObject);
                if (apiResponse.getDashBoardItems() != null) {
                    if (menuModels != null && menuModels.size() != 0) {
                        int lastPos = menuModels.size();
                        menuModels.addAll(menuModels.size(), apiResponse.getDashBoardItems().get(0).getDailyImages());
                        MenuAddaptor.notifyItemRangeInserted(lastPos, apiResponse.getDashBoardItems().size());

                    } else {
                        menuModels = new ArrayList<>();
                        menuModels.addAll(0, apiResponse.getDashBoardItems().get(0).getDailyImages());
                        MenuAddaptor.notifyItemRangeInserted(0, apiResponse.getDashBoardItems().size());
                    }
                }
                if (apiResponse.getLinks() != null) {
                    if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                        getImageCategoryNextPage(apiResponse.getLinks().getNextPageUrl());
                    }else{
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);

                    }
                }else{
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                String keywords = binding.searchEdt.getText().toString().replace(" ",",").replace(",",",");
                map.put("tag", keywords);
                Utility.Log("pram",map.toString());
                return map;
            }
        };
        stringRequest.setTag("search");
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }

    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
}