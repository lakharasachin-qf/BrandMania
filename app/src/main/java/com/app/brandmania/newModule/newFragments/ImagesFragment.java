package com.app.brandmania.newModule.newFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentImagesBinding;
import com.app.brandmania.newModule.BaseInterface;
import com.app.brandmania.newModule.adpatersBase.BusinessCategoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

public class ImagesFragment extends BaseFragment {
    private FragmentImagesBinding binding;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    ImageList apiObject;
    private ImageList selectedImageCategory;
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_images, parent, false);
        selectedImageCategory = gson.fromJson(act.getIntent().getStringExtra("selectedimage"), ImageList.class);
        binding.shimmerForPagination.startShimmer();
        binding.shimmerForPagination.setVisibility(View.VISIBLE);
        getImageCtegory();
        prefManager = new PreafManager(act);
        return binding.getRoot();
    }

    BusinessCategoryAdapter businessCategoryAdapter;

    public void setAdapter() {
        businessCategoryAdapter = new BusinessCategoryAdapter(menuModels, act);
        businessCategoryAdapter.setLayoutType(FROM_VIEWALL);
        ((BaseInterface) act).loadFirstImageEvent(0, menuModels.get(0));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 4);
        binding.viewRecoRecycler.setLayoutManager(mLayoutManager);
        binding.viewRecoRecycler.setHasFixedSize(true);
        binding.viewRecoRecycler.setAdapter(businessCategoryAdapter);
        binding.viewRecoRecycler.setVisibility(View.VISIBLE);
    }

    private void getImageCtegory() {
        Utility.Log("API: ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("ImagesFragments:", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    apiObject = ResponseHandler.HandleGetImageByIdCategory(jsonObject);
                    if (apiObject.getCatogaryImagesList() != null) {
                        menuModels = apiObject.getCatogaryImagesList();
                        if (menuModels != null && menuModels.size() != 0) {
                            setAdapter();
                        } else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }


                        if (apiObject.getLinks() != null) {
                            if (apiObject.getLinks().getNextPageUrl() != null && !apiObject.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiObject.getLinks().getNextPageUrl().isEmpty()) {
                                binding.shimmerForPagination.startShimmer();
                                binding.shimmerForPagination.setVisibility(View.VISIBLE);
                                getPagination(apiObject.getLinks().getNextPageUrl());
                            } else {
                                binding.shimmerForPagination.stopShimmer();
                                binding.shimmerForPagination.setVisibility(View.GONE);
                            }
                        } else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    } else {
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
                        error.printStackTrace();
//                        String body;
//                        body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                        Log.e("Load-Get_Exam ", body);

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer" + prefManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (act.getIntent().hasExtra("notification")) {
                    params.put("image_category_id", act.getIntent().getStringExtra("cat_id"));
                } else {
                    params.put("image_category_id", selectedImageCategory.getId());
                }


                Utility.Log("params", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }


    private void getPagination(String nextPageUrl) {
        Utility.Log("API : ", nextPageUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, nextPageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ImageList apiObject = ResponseHandler.HandleGetImageByIdCategory(jsonObject);
                    if (apiObject.getCatogaryImagesList() != null) {
                        if (menuModels != null && menuModels.size() != 0) {
                            int lastPos = menuModels.size();
                            menuModels.addAll(menuModels.size(), apiObject.getCatogaryImagesList());
                            businessCategoryAdapter.notifyItemRangeInserted(lastPos, apiObject.getCatogaryImagesList().size());
                        } else {
                            menuModels = new ArrayList<>();
                            menuModels.addAll(0, apiObject.getCatogaryImagesList());
                        }
                    }
                    if (apiObject.getLinks() != null) {
                        if (apiObject.getLinks().getNextPageUrl() != null && !apiObject.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiObject.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getPagination(apiObject.getLinks().getNextPageUrl());
                        } else {
                            binding.shimmerForPagination.stopShimmer();
                            binding.shimmerForPagination.setVisibility(View.GONE);
                        }
                    }

                    if (apiObject.getCatogaryImagesList() == null || apiObject.getCatogaryImagesList().size() == 0) {
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
                params.put("Authorization", "Bearer" + prefManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (act.getIntent().hasExtra("notification")) {
                    params.put("image_category_id", act.getIntent().getStringExtra("cat_id"));
                } else {
                    params.put("image_category_id", selectedImageCategory.getId());
                }
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
}