package com.make.mybrand.Fragment.top;

import static com.make.mybrand.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;
import static com.make.mybrand.Adapter.LanguageFilterAdaptor.FROM_VIEWALLS;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.make.mybrand.Adapter.ImageCategoryAddaptor;
import com.make.mybrand.Adapter.LanguageFilterAdaptor;
import com.make.mybrand.Common.ResponseHandler;
import com.make.mybrand.Fragment.BaseFragment;
import com.make.mybrand.Interface.ImageCateItemeInterFace;
import com.make.mybrand.Model.DashBoardItem;
import com.make.mybrand.Model.ImageList;
import com.make.mybrand.R;
import com.make.mybrand.databinding.CategoryTabBinding;
import com.make.mybrand.utils.APIs;
import com.make.mybrand.utils.CodeReUse;
import com.make.mybrand.utils.Utility;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryTab extends BaseFragment {
    Activity act;
    private CategoryTabBinding binding;
    private DashBoardItem imageList;
    private ImageList selectedObject;
    ImageList apiObject;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    Gson gson;
    int objectSelectedPosition = 0;
    boolean isViewAll = false;


    public CategoryTab setViewAll(boolean viewAll) {
        isViewAll = viewAll;
        return this;
    }


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.category_tab, parent, false);
        gson = new Gson();
        queue = Volley.newRequestQueue(act);
        imageList = gson.fromJson(act.getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        selectedObject = gson.fromJson(act.getIntent().getStringExtra("selectedimage"), ImageList.class);
        binding.shimmerForPagination.startShimmer();
        binding.shimmerForPagination.setVisibility(View.VISIBLE);
        getImageCtegory("");
        if(selectedObject!=null && selectedObject.getLanguageData()!=null && selectedObject.getLanguageData().size()!=0)
            setLanguageFilterAdapter();
        return binding.getRoot();

    }

    String data;
    LanguageFilterAdaptor filterAdaptor;

    public void setLanguageFilterAdapter() {
        filterAdaptor = new LanguageFilterAdaptor(selectedObject.getLanguageData(), act);
        filterAdaptor.setLayoutType(FROM_VIEWALLS);
        RecyclerView.LayoutManager hLayoutManager = new LinearLayoutManager(act, RecyclerView.HORIZONTAL, false);
        binding.languageFilterRecycler.setLayoutManager(hLayoutManager);

        LanguageFilterAdaptor.onItemSelectListener onItemSelectListener = new LanguageFilterAdaptor.onItemSelectListener() {
            @Override
            public void onItemSelect(String Data, int position) {
                data = selectedObject.getLanguageData().get(position);
                getImageCtegory(data);
                if (data.contains("All")) {
                    getImageCtegory("");
                }
            }
        };
        binding.languageFilterRecycler.setHasFixedSize(true);
        filterAdaptor.setOnItemSelectListener(onItemSelectListener);
        binding.languageFilterRecycler.setAdapter(filterAdaptor);
        binding.languageFilter.setVisibility(View.VISIBLE);
    }

    ImageCategoryAddaptor menuAddaptor;

    public void setAdapter() {
        menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
        //  if (isViewAll)
        ((ImageCateItemeInterFace) act).ImageCateonItemSelection(0, menuModels.get(0));
        menuAddaptor.setLayoutType(FROM_VIEWALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 3);
        binding.viewRecoRecycler.setLayoutManager(mLayoutManager);
        binding.viewRecoRecycler.setHasFixedSize(true);
        binding.viewRecoRecycler.setAdapter(menuAddaptor);
        binding.viewRecoRecycler.setVisibility(View.VISIBLE);

    }

    RequestQueue queue;
    private void getImageCtegory(String filterData) {

        queue.cancelAll("categoryCall");
        queue.cancelAll("categoryCallPage");

        binding.viewRecoRecycler.setVisibility(View.GONE);
        binding.shimmerViewContainer.setVisibility(View.VISIBLE);
        binding.shimmerViewContainer.startShimmer();

        Utility.Log("API : ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                binding.shimmerViewContainer.stopShimmer();
                binding.shimmerViewContainer.setVisibility(View.GONE);
                binding.viewRecoRecycler.setVisibility(View.VISIBLE);

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
                                getImageCtegoryNextPage(apiObject.getLinks().getNextPageUrl(), filterData);
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
                if (!act.getIntent().hasExtra("notification")) {
                    if (imageList != null) {
                        params.put("image_category_id", imageList.getId());
                    } else {
                        params.put("image_category_id", selectedObject.getId());
                    }
//                    if (!selectedObject.getLanguageData().isEmpty()) {
//                        params.put("lang", filterData);
//                    }
                    if (filterData != null)
                        params.put("lang", filterData);

                    if (act.getIntent().hasExtra("dailyImages")) {
                        params.put("image_category_id", selectedObject.getId());
                    }
                } else {
                    params.put("image_category_id", act.getIntent().getStringExtra("cat_id"));
                }
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };


        stringRequest.setTag("categoryCall");

        queue.add(stringRequest);
    }


    private void getImageCtegoryNextPage(String nextPageUrl, String filterData) {
        queue.cancelAll("categoryCallPage");
        queue.cancelAll("categoryCall");

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
                            menuAddaptor.notifyItemRangeInserted(lastPos, apiObject.getCatogaryImagesList().size());
                        } else {
                            menuModels = new ArrayList<>();
                            menuModels.addAll(0, apiObject.getCatogaryImagesList());
                        }
                    }
                    if (apiObject.getLinks() != null) {
                        if (apiObject.getLinks().getNextPageUrl() != null && !apiObject.getLinks().getNextPageUrl().equalsIgnoreCase("null") && !apiObject.getLinks().getNextPageUrl().isEmpty()) {
                            binding.shimmerForPagination.startShimmer();
                            binding.shimmerForPagination.setVisibility(View.VISIBLE);
                            getImageCtegoryNextPage(apiObject.getLinks().getNextPageUrl(), filterData);
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

                return getHeader(CodeReUse.GET_FORM_HEADER);
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (!act.getIntent().hasExtra("notification")) {
                    if (imageList != null) {
                        params.put("image_category_id", imageList.getId());
                    } else {
                        params.put("image_category_id", selectedObject.getId());
                    }
                    if (filterData != null)
                        params.put("lang", filterData);

                    if (act.getIntent().hasExtra("dailyImages")) {
                        params.put("image_category_id", selectedObject.getId());
                    }
                } else {
                    params.put("image_category_id", act.getIntent().getStringExtra("cat_id"));
                }
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        stringRequest.setTag("categoryCallPage");

        queue.add(stringRequest);
    }

}