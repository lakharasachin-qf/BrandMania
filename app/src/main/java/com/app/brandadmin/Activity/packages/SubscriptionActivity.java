package com.app.brandadmin.Activity.packages;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandadmin.Adapter.BrandAdapter;
import com.app.brandadmin.Common.PreafManager;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.Model.BrandListItem;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.APIs;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivitySubscriptionBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionActivity extends BaseActivity {
    Activity act;
    private ActivitySubscriptionBinding binding;
    ArrayList<BrandListItem> multiListItems=new ArrayList<>();
    PreafManager preafManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        preafManager=new PreafManager(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding= DataBindingUtil.setContentView(act,R.layout.activity_subscription);
        binding.BackButtonMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        startAnimation();
        getBrandList();
    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.getBrandList.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);
    }
    private void GetBrandAddaptor() {

        BrandAdapter MenuAddaptor = new BrandAdapter(multiListItems, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.getBrandList.setHasFixedSize(true);
        binding.getBrandList.setLayoutManager(mLayoutManager);
        binding.getBrandList.setAdapter(MenuAddaptor);

    }
    private void getBrandList() {

        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_BRAND : ", response);

//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
//
//                    if (multiListItems != null && multiListItems.size() != 0) {
//                        GetBrandAddaptor();
//                        binding.shimmerViewContainer.stopShimmer();
//                        binding.shimmerViewContainer.setVisibility(View.GONE);
//                        binding.getBrandList.setVisibility(View.VISIBLE);
//                        binding.emptyStateLayout.setVisibility(View.GONE);
//                    }
//                    if (multiListItems == null || multiListItems.size() == 0) {
//                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
//                        binding.getBrandList.setVisibility(View.GONE);
//                        binding.shimmerViewContainer.stopShimmer();
//                        binding.shimmerViewContainer.setVisibility(View.GONE);
//                    }
//
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                Log.e("DateNdClass", params.toString());
                //params.put("upload_type_id", String.valueOf(Constant.ADD_NOTICE));
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