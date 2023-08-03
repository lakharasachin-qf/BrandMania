package com.app.brandmania.Activity.details;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.brandmania.Adapter.BusinessCategoryAdapter;
import com.app.brandmania.Common.MySingleton;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityViewBusinessCategoryBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessCategoryListActivity extends BaseActivity {
    private Activity act;
    private ActivityViewBusinessCategoryBinding binding;
    private DashBoardItem apiModel;
    private ArrayList<ImageList> rootList;
    private ArrayList<ImageList> menuModels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_business_category);
        binding.BackButton.setOnClickListener(v -> onBackPressed());

        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorsecond, R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(() -> {
            startAnimation();
            getBusinessCategory();
        });

        startAnimation();
        getBusinessCategory();

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
        binding.progressBar.setVisibility(View.GONE);
        Utility.Log("API : ", APIs.BUSINESS_CATEGORY + "?page=1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.BUSINESS_CATEGORY + "?page=1", response -> {
            binding.swipeContainer.setRefreshing(false);
            Utility.Log("response : ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (ResponseHandler.isSuccess(null, jsonObject)) {
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
                } else {
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.recyclerList.setVisibility(View.GONE);
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                    binding.emptyStateMsg.setText("No Data Found");
                    binding.progressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.recyclerList.setVisibility(View.GONE);
                binding.emptyStateLayout.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.emptyStateMsg.setText("No Data Found");
            }


        },
                error -> {
                    binding.swipeContainer.setRefreshing(false);
                    error.printStackTrace();
                    binding.progressBar.setVisibility(View.GONE);
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.recyclerList.setVisibility(View.GONE);
                    //  binding.view.setVisibility(View.VISIBLE);
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                    binding.emptyStateMsg.setText("No Data Found");

                }
        ) {

            @Override
            public Map getHeaders() {
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                String keywords = binding.searchEdt.getText().toString().replace(" ", ",").replace(",", ",");
                map.put("tag", keywords);
                Utility.Log("pram", map.toString());
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
                    } else {
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        //    binding.view.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    //    binding.view.setVisibility(View.VISIBLE);
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

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                String keywords = binding.searchEdt.getText().toString().replace(" ", ",").replace(",", ",");
                map.put("tag", keywords);
                Utility.Log("pram", map.toString());
                return map;
            }
        };
        stringRequest.setTag("search");
        MySingleton.getInstance(act).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }


}