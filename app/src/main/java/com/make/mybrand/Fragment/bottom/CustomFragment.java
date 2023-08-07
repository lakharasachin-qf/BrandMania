package com.make.mybrand.Fragment.bottom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.make.mybrand.Activity.custom.CustomViewAllActivit;
import com.make.mybrand.Adapter.CustomDashbordAddapter;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Fragment.BaseFragment;
import com.make.mybrand.Model.DashBoardItem;
import com.make.mybrand.Model.FrameItem;
import com.make.mybrand.R;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.make.mybrand.databinding.FragmentCustomBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class CustomFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    Activity act;
    private FragmentCustomBinding binding;
    Timer timer;
    DashBoardItem apiResponse;
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    private String is_frame="";
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    private CustomDashbordAddapter dasboardAddaptor;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom,parent,false);
        binding.customImageRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomViewAllActivit.class);
                startActivity(intent);
            }
        });

        startAnimation();
        getImageCtegory();
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,R.color.colorsecond, R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startAnimation();
                //getFrame();
                getImageCtegory();
            }
        });
        return binding.getRoot();
    }

    public void setAdapter() {
        dasboardAddaptor = new CustomDashbordAddapter(menuModels, act);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.frameRecycler.setHasFixedSize(true);
        binding.frameRecycler.setLayoutManager(mLayoutManager);
        binding.frameRecycler.setAdapter(dasboardAddaptor);

    }
    private void getImageCtegory() {
        Utility.Log("API : ", APIs.GET_CUSTOME_FRAME_CATEGORY + "?page=1");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_CUSTOME_FRAME_CATEGORY + "?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_CUSTOME_FRAME_CATEGORY : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    apiResponse = ResponseHandler.HandleGetFrameCategory(jsonObject);
                    if (apiResponse.getDashBoardItems() != null) {
                        menuModels = apiResponse.getDashBoardItems();
                        if (menuModels != null && menuModels.size() != 0) {
                            setAdapter();
                            binding.shimmerViewContainer.stopShimmer();
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.frameRecycler.setVisibility(View.VISIBLE);
                        }
                    }

                    if (apiResponse.getLinks() != null) {

                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCtegoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        }else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }else {
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);
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
                        binding.frameRecycler.setVisibility(View.GONE);
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
                return getHeader(CodeReUse.GET_FORM_HEADER);
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void getImageCtegoryNextPage(String nextPageUrl) {
        Utility.Log("API-", nextPageUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, nextPageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.swipeContainer.setRefreshing(false);
                Utility.Log("GET_IMAGE_CATEGORY : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    apiResponse = ResponseHandler.HandleGetFrameCategory(jsonObject);
                    if (apiResponse.getDashBoardItems() != null) {
                        if (menuModels != null && menuModels.size() != 0) {
                            int lastPos = menuModels.size();
                            menuModels.addAll(menuModels.size(), apiResponse.getDashBoardItems());
                            dasboardAddaptor.notifyItemRangeInserted(lastPos, apiResponse.getDashBoardItems().size());
                        } else {
                            menuModels = new ArrayList<>();
                            menuModels.addAll(0, apiResponse.getDashBoardItems());
                        }
                    }
                    if (apiResponse.getLinks() != null) {
                        if (apiResponse.getLinks().getNextPageUrl() != null && !apiResponse.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiResponse.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCtegoryNextPage(apiResponse.getLinks().getNextPageUrl());
                        }else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }
                    if (apiResponse.getDashBoardItems()==null ||apiResponse.getDashBoardItems().size()==0) {
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);
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
                        binding.shimmerForPagination.stopShimmer();
                        binding.shimmerForPagination.setVisibility(View.GONE);


                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
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
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.frameRecycler.setVisibility(View.GONE);

    }
    @Override public void onRefresh() {
        startAnimation();
        getImageCtegory();
    }
}