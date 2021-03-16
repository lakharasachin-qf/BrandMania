package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.app.brandmania.Activity.custom.CustomViewAllActivit;
import com.app.brandmania.Adapter.CustomDashbordAddapter;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentCustomBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class CustomFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Activity act;
    private FragmentCustomBinding binding;
    Timer timer;
    DashBoardItem apiResponse;
    ArrayList<DashBoardItem> menuModels = new ArrayList<>();
    PreafManager preafManager;
    private String is_frame="";
    ArrayList<FrameItem> brandListItems = new ArrayList<>();
    private CustomDashbordAddapter dasboardAddaptor;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom,container,false);
        binding.customImageRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CustomViewAllActivit.class);
                startActivity(intent);
            }
        });
        preafManager=new PreafManager(act);
        startAnimation();
        getImageCtegory();
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary,R.color.colorsecond, R.color.colorthird);
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                startAnimation();
                getFrame();
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer"+preafManager.getUserToken());
                Log.e("Token", params.toString());
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
                        Log.e("APIIII", new Gson().toJson(apiResponse.getLinks()));
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

//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
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
        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    private void startAnimation() {
        binding.shimmerViewContainer.startShimmer();
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.frameRecycler.setVisibility(View.GONE);

    }
    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FRAME : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    brandListItems = ResponseHandler.HandleGetFrame(jsonObject);
                    JSONObject datajsonobjecttt =ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame= datajsonobjecttt.getString("is_frame");



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("brand_id",preafManager.getActiveBrand().getId());
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    @Override public void onRefresh() {
        startAnimation();
        getFrame();
        getImageCtegory();


    }
}
