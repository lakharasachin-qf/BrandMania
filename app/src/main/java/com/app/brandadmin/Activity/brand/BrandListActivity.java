package com.app.brandadmin.Activity.brand;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandadmin.Adapter.BrandAdapter;
import com.app.brandadmin.Common.ResponseHandler;
import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.Model.BrandListItem;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.APIs;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivityBrandListBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrandListActivity extends BaseActivity {
    Activity act;
    private ActivityBrandListBinding binding;
    ArrayList<BrandListItem> multiListItems=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_brand_list);
        getBrandList();
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

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);

                    if (multiListItems != null && multiListItems.size() != 0) {

                     //   GetBrandAddaptor();

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
            public Map getHeaders() {



                Utility.Log("Verify-Header", getHeader(CodeReUse.GET_FORM_HEADER).toString());
                return getHeader(CodeReUse.GET_FORM_HEADER);

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
}