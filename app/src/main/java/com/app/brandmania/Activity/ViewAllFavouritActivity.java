package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.app.brandmania.Adapter.DownloadFavoriteAdapter;
import com.app.brandmania.Adapter.FrameCateItemeInterFace;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Model.DownloadFavoriteItemList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityVIewAllDownloadImageBinding;
import com.app.brandmania.databinding.ActivityViewAllFavouritBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAllFavouritActivity extends AppCompatActivity implements FrameCateItemeInterFace {
    Activity act;
    private ActivityViewAllFavouritBinding binding;
    DownloadFavoriteItemList selectedModelFromView;
    ArrayList<DownloadFavoriteItemList> menuModels = new ArrayList<>();
    PreafManager preafManager;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        act=this;
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        captureScreenShort();
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_all_favourit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        gson=new Gson();
        preafManager=new PreafManager(act);
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getFavoritListItem();
        selectedModelFromView = gson.fromJson(getIntent().getStringExtra("detailsObjj"), DownloadFavoriteItemList.class);
        Glide.with(act)
                .load(selectedModelFromView.getImage())
                .placeholder(R.drawable.placeholder)
                .into(binding.recoImage);

        // if (selectedModelFromView.getFrame()!=null)
        Glide.with(act)
                .load(selectedModelFromView.getFrame())
                .into(binding.recoFrame);

    }
    public void setAdapter() {
        DownloadFavoriteAdapter menuAddaptor = new DownloadFavoriteAdapter(menuModels, act);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act,4);
        binding.viewRecoRecycler.setLayoutManager(mLayoutManager);
        binding.viewRecoRecycler.setHasFixedSize(true);
        binding.viewRecoRecycler.setAdapter(menuAddaptor);
    }
    private void getFavoritListItem() {
        Utility.Log("API : ", APIs.GET_FAVORITLIST_ITEM);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FAVORITLIST_ITEM , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FAVORITLIST_ITEM : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    menuModels = ResponseHandler.HandleGetIFavoritListGrid(jsonObject);


                        setAdapter();

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
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                Log.e("Token", params.toString());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",preafManager.getActiveBrand().getId());
//                if (imageList != null)
//                    params.put("image_category_id", imageList.getId());
//                else
//                    params.put("image_category_id", selectedObject.getId());

                Utility.Log("POSTED-PARAMS-", params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }
    @Override
    public void FrameCateonItemSelection(int position, DownloadFavoriteItemList listModel) {
        Glide.with(act)
                .load(listModel.getImage())
                .placeholder(R.drawable.placeholder)
                .into(binding.recoImage);

        // if (selectedModelFromView.getFrame()!=null)
        Glide.with(act)
                .load(listModel.getFrame())
                .into(binding.recoFrame);
    }
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }
    public void captureScreenShort()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
}