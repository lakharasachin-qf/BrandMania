package com.app.brandmania.Fragment.top;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Adapter.ImageCategoryAddaptor;
import com.app.brandmania.Adapter.MenuAddaptor;
import com.app.brandmania.Adapter.MultiListItem;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.Model.FrameItem;
import com.app.brandmania.Model.ImageList;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FrameTabBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.brandmania.Adapter.ImageCategoryAddaptor.FROM_VIEWALL;

public class FrameTab extends Fragment {

    Activity act;
    private FrameTabBinding binding;
    private String is_frame="";
    PreafManager preafManager;
    ArrayList<ImageList> menuModels = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater,R.layout.frame_tab,container,false);
        preafManager=new PreafManager(getActivity());
        getFrame();
        return binding.getRoot();
    }
//    public void FrameList() {
//
//        ArrayList<MultiListItem> menuModels = new ArrayList<>();
//        MultiListItem model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.img_one);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.imgd_o);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.img_three);
//        menuModels.add(model);
//
//        model = new MultiListItem();
//        model.setLayoutType(MultiListItem.ACTIVITY_VIEWALLFRAME);
//        model.setImage(R.drawable.img_four);
//        menuModels.add(model);
//
//        MenuAddaptor menuAddaptor = new MenuAddaptor(menuModels, act);
//        binding.frameRecycler.setLayoutManager(new GridLayoutManager(getActivity(),4));
//        binding.frameRecycler.setHasFixedSize(true);
//        binding.frameRecycler.setAdapter(menuAddaptor);
//    }

    public void setAdapter() {
        ImageCategoryAddaptor menuAddaptor = new ImageCategoryAddaptor(menuModels, act);
       // menuAddaptor.setLayoutType(FROM_VIEWALL);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(act, 4);
        binding.frameRecycler.setLayoutManager(mLayoutManager);
        binding.frameRecycler.setHasFixedSize(true);
        binding.frameRecycler.setAdapter(menuAddaptor);
    }

    private void getFrame() {
        Utility.Log("API : ", APIs.GET_FRAME);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_FRAME,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_FRAME : ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    menuModels = ResponseHandler.HandleGetFrameList(jsonObject);
                    JSONObject datajsonobjecttt =ResponseHandler.getJSONObject(jsonObject, "data");
                    is_frame= datajsonobjecttt.getString("is_frame");
                    if (is_frame.equals("1")) {
                        Log.e("Dataaaaa",new Gson().toJson(menuModels));
                        //  binding.customFrameRelative.setVisibility(View.GONE);
                        // Toast.makeText(act,brandListItems.size()+"",Toast.LENGTH_LONG).show();
                        setAdapter();
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
}
