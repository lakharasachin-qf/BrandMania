package com.app.brandmania.Activity.details;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.app.brandmania.Adapter.BusinessCategoryAdapter;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.CommonListModel;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityViewBusinessCategoryBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessCategoryListActivity extends BaseActivity {
    Activity act;
    private ActivityViewBusinessCategoryBinding binding;
    private DashBoardItem apiModel;
    private ArrayList<ImageList> rootList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_material_theme);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_business_category);
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

        startAnimation();
        getBusinessCategory();

        binding.searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCountry(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    void filterCountry(String text) {
        ArrayList<ImageList> temp = new ArrayList<>();
        for (ImageList d : rootList) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        MenuAddaptor.updateList(temp);
    }

    BusinessCategoryAdapter MenuAddaptor;

    private void setAdapter() {
        MenuAddaptor = new BusinessCategoryAdapter(apiModel, this, apiModel.getDashBoardItems().get(0).getDailyImages());
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.BUSINESS_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("response : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    apiModel = ResponseHandler.handleBusinessCategory(act, jsonObject);

                    if (apiModel.getDashBoardItems() != null && apiModel.getDashBoardItems().size() != 0 && apiModel.getDashBoardItems().get(0).getDailyImages() != null && apiModel.getDashBoardItems().get(0).getDailyImages().size() != 0) {
                        rootList = ResponseHandler.handleBusinessCategory(act, jsonObject).getDashBoardItems().get(0).getDailyImages();
                        setAdapter();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.recyclerList.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                    } else {
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.recyclerList.setVisibility(View.GONE);
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.emptyStateMsg.setText("No Data Found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
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
                return new HashMap<>();
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }


}