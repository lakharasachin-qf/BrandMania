package com.make.mybrand.Activity.about_us;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Adapter.MenuAddaptor;
import com.make.mybrand.Adapter.MultiListItem;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.R;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.make.mybrand.databinding.ActivityFaqBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FaqActivity extends BaseActivity {

    Activity act;
    private ActivityFaqBinding binding;
    private ArrayList<MultiListItem> faqList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_faq);
        Utility.isLiveModeOff(act);
        binding.BackButtonIcon.setOnClickListener(v -> onBackPressed());

        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_dark);

        binding.swipeContainer.setOnRefreshListener(() -> {
            startAnimation();
            getFaq();
        });
        startAnimation();
        getFaq();
    }

    private void setAdapters() {
        MenuAddaptor faqAdapter = new MenuAddaptor(faqList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.faqRecycler.setHasFixedSize(true);
        binding.faqRecycler.setLayoutManager(mLayoutManager);
        binding.faqRecycler.setVisibility(View.VISIBLE);
        binding.faqRecycler.setAdapter(faqAdapter);
    }

    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.faqRecycler.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }

    private void getFaq() {
        Utility.Log("API : ", APIs.FAQ);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.FAQ, response -> {
            binding.swipeContainer.setRefreshing(false);
            try {
                JSONObject jsonObject = new JSONObject(response);

                Utility.Log("Get faq : ", jsonObject.toString());
                faqList = ResponseHandler.HandleFaqResponse(jsonObject);

                if (faqList != null && faqList.size() != 0) {
                    setAdapters();
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    binding.faqRecycler.setVisibility(View.VISIBLE);
                    binding.emptyStateLayout.setVisibility(View.GONE);
                } else {
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                    binding.faqRecycler.setVisibility(View.GONE);
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        },
                error -> {
                    error.printStackTrace();
                    binding.swipeContainer.setRefreshing(false);
                    binding.swipeContainer.setRefreshing(false);
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                    binding.faqRecycler.setVisibility(View.GONE);
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                // params.put("X-Authorization", "Bearer"+preafManager.getUserToken());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // params.put("image_category_id", imageList.getId());

                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
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