package com.app.brandmania.Fragment.top;

import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.app.brandmania.Adapter.InnerFragmentAdpaters;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Interface.ImageCateItemeInterFace;
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.databinding.CategoryTabBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryTab extends FrameTab {
    Activity act;
    private CategoryTabBinding binding;
    private int mColorCode;
    private DashBoardItem imageList;
    private ImageList selectedObject;
    private ColorTab context;
    PreafManager preafManager;
    ImageList apiObject;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    Gson gson;
    boolean isViewAll = false;

    public CategoryTab setViewAll(boolean viewAll) {
        isViewAll = viewAll;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.category_tab, container, false);
        gson = new Gson();

        imageList = gson.fromJson(act.getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        selectedObject = gson.fromJson(act.getIntent().getStringExtra("selectedimage"), ImageList.class);
        binding.shimmerForPagination.startShimmer();
        binding.shimmerForPagination.setVisibility(View.VISIBLE);
        getImageCtegory();
        preafManager = new PreafManager(getActivity());
        return binding.getRoot();
    }

    boolean isPostAvailable = false;
    boolean isGIFAvailable = false;
    boolean isVideoAvailable = false;

    public void loadViewPager() {


        for (ImageList model : menuModels) {
            if (model.getImageType() == ImageList.IMAGE) {
                isPostAvailable = true;
            }
            if (model.getImageType() == ImageList.GIF) {
                isGIFAvailable = true;
            }
            if (model.getImageType() == ImageList.VIDEO) {
                isVideoAvailable = true;
            }
        }


        ArrayList<Fragment> fragments = new ArrayList<>();
        if (isPostAvailable) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Post"));
            fragments.add(new PostFragment());
        }

        if (isGIFAvailable) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("GIF"));
            fragments.add(new GifFragment());
        }

        if (isVideoAvailable) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"));
            fragments.add(new VideoFragment());
        }

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (fragments.size() != 0) {
            binding.viewRecoRecycler.setVisibility(View.GONE);
            binding.viewPagerLayout.setVisibility(View.VISIBLE);

            final InnerFragmentAdpaters adapter = new InnerFragmentAdpaters(getChildFragmentManager(), fragments);
            binding.viewPager.setAdapter(adapter);
            binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    binding.viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
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

    private void getImageCtegory() {

        Utility.Log("API : ", APIs.GET_IMAGEBUID_CATEGORY);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_IMAGEBUID_CATEGORY + "/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Utility.Log("GET_IMAGE_CATEGORYyyyyyyyyyyyy : ", response);

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
                                getImageCtegoryNextPage(apiObject.getLinks().getNextPageUrl());
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/x-www-form-urlencoded");//application/json
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("X-Authorization", "Bearer" + preafManager.getUserToken());
                return params;
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

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }


    private void getImageCtegoryNextPage(String nextPageUrl) {
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
                            getImageCtegoryNextPage(apiObject.getLinks().getNextPageUrl());
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
                params.put("X-Authorization", "Bearer" + preafManager.getUserToken());
                return params;
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

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

}