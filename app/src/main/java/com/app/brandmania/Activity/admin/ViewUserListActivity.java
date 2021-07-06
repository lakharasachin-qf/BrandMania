package com.app.brandmania.Activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.app.brandmania.Adapter.BrandAdapter;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.CodeReUse;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.ActivityViewCategoryBinding;
import com.app.brandmania.databinding.ActivityViewUserBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewUserListActivity extends BaseActivity {
    Activity act;
    private ActivityViewUserBinding binding;
    ArrayList<UserListModel> multiListItems=new ArrayList<>();
    ArrayList<UserListModel> selectedUSer=new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act=this;
        binding= DataBindingUtil.setContentView(act,R.layout.activity_view_user);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
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
        final UserAdapter listViewAdapter = new UserAdapter(act, act, R.layout.item_user_list, multiListItems);
        binding.getBrandList.setAdapter(listViewAdapter);
        UserAdapter.handleSelectionEvent handleSelectionEvent = new UserAdapter.handleSelectionEvent() {
            @Override
            public void selectionEvent(UserListModel model, int position,String flag) {

            }
        };
        listViewAdapter.setInteface(handleSelectionEvent);

    }
    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_USER_ADMIN);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_USER_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_USER_ADMIN : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataJson=jsonObject.getJSONArray("data");
                    for (int i=0;i<dataJson.length();i++){
                        JSONObject userObject=dataJson.getJSONObject(i);
                        UserListModel userListModel=new UserListModel();
                        userListModel.setId(userObject.getString("id"));
                        userListModel.setFirstName(userObject.getString("first_name"));
                        userListModel.setLastName(userObject.getString("last_name"));
                        userListModel.setPhoneNo(userObject.getString("phone"));
                        userListModel.setEmailId(userObject.getString("email"));
                        userListModel.setFirebaseToken(userObject.getString("firebase_token"));
                        multiListItems.add(userListModel);
                    }


                    if (multiListItems != null && multiListItems.size() != 0) {
                         GetBrandAddaptor();
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.getBrandList.setVisibility(View.VISIBLE);
                        binding.emptyStateLayout.setVisibility(View.GONE);
                    }
                    if (multiListItems == null || multiListItems.size() == 0) {
                        binding.emptyStateLayout.setVisibility(View.VISIBLE);
                        binding.getBrandList.setVisibility(View.GONE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
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
                Log.e("Token",params.toString());
                return params;
            }


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }

    @Override public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

}