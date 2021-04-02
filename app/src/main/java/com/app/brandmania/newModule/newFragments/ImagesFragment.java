package com.app.brandmania.newModule.newFragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.app.brandmania.Model.DashBoardItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentImagesBinding;
import com.google.gson.Gson;

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
    private DashBoardItem imageList;
    private ImageList selectedObject;
    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_images, parent, false);


        imageList = gson.fromJson(act.getIntent().getStringExtra("detailsObj"), DashBoardItem.class);
        selectedObject = gson.fromJson(act.getIntent().getStringExtra("selectedimage"), ImageList.class);
        Log.e("IMAGELIST--",new Gson().toJson(imageList));
        Log.e("selectedObject--",new Gson().toJson(selectedObject));
        // Toast.makeText(getActivity(),imageList.getId(),Toast.LENGTH_LONG).show();
        binding.shimmerForPagination.startShimmer();
        binding.shimmerForPagination.setVisibility(View.VISIBLE);
        getImageCtegory();
        prefManager = new PreafManager(act);
        return binding.getRoot();
    }

    ImageCategoryAddaptor menuAddaptor;

    public void setAdapter() {
        menuAddaptor = new ImageCategoryAddaptor(menuModels, act);


        menuAddaptor.setLayoutType(FROM_VIEWALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 4);
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
                Utility.Log("GET_IMAGE_CATEGORYyyyyyyyyyyyy : ", response);

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
//                        String body;
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
                params.put("Authorization", "Bearer" + prefManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                if (imageList != null) {
                    params.put("image_category_id", imageList.getId());
                } else {
                    params.put("image_category_id", selectedObject.getId());
                }

                if (act.getIntent().hasExtra("dailyImages")) {
                    params.put("image_category_id", selectedObject.getId());
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
                            Log.e("GGG", new Gson().toJson(menuModels));
                        } else {
                            menuModels = new ArrayList<>();
                            menuModels.addAll(0, apiObject.getCatogaryImagesList());
                        }
                    }
                    if (apiObject.getLinks() != null) {
                        Log.e("APIIII", new Gson().toJson(apiObject.getLinks()));
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
//                        String body;
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
                params.put("Authorization", "Bearer" + prefManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (imageList != null) {

                    params.put("image_category_id", imageList.getId());
                } else
                    params.put("image_category_id", selectedObject.getId());

                if (act.getIntent().hasExtra("dailyImages")) {
                    params.put("image_category_id", selectedObject.getId());
                }
                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
}