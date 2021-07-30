package com.app.brandadmin.Activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.app.brandadmin.Connection.BaseActivity;
import com.app.brandadmin.R;
import com.app.brandadmin.Utils.APIs;
import com.app.brandadmin.Utils.CodeReUse;
import com.app.brandadmin.Utils.Utility;
import com.app.brandadmin.databinding.ActivityViewUserBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewUserListActivity extends BaseActivity {
    Activity act;
    private ActivityViewUserBinding binding;
    ArrayList<UserListModel> multiListItems = new ArrayList<>();
    ArrayList<UserListModel> selectedUSer = new ArrayList<>();
    ArrayList<UserListModel> addremoveUser = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_view_user);

        binding.useradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();

                Intent intent = new Intent(act,SendNotification.class);
                intent.putExtra("addremoveUser", new Gson().toJson(addremoveUser));
                startActivity(intent);
                Log.e("addremovelist", new Gson().toJson(addremoveUser));
            }
        });

        binding.mainLinearlayoutTitle.setOnClickListener(new View.OnClickListener() {
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
        final User1Adapter listViewAdapter = new User1Adapter(multiListItems,act);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act, RecyclerView.VERTICAL, false);
        binding.getBrandList.setHasFixedSize(true);
        binding.getBrandList.setLayoutManager(mLayoutManager);
        binding.getBrandList.setAdapter(listViewAdapter);
        User1Adapter.OnCheckedItem onCheckedItem=new User1Adapter.OnCheckedItem() {
            @Override
            public void onCheck(String flag, UserListModel model, int pos) {
                if (flag.equals("add")) {
                    addremoveUser.add(model);
                    Log.e("OnAdd" , new Gson().toJson(addremoveUser));

                } else {
                    for (int i = 0; i < addremoveUser.size(); i++)
                        if (addremoveUser.get(i).getId().equalsIgnoreCase(model.getId())) {
                            addremoveUser.get(i).setSelected(false);
                            addremoveUser.remove(i);
                            Log.e("OnRemove", new Gson().toJson(addremoveUser));
                            break;
                        }
                }
            }
        };
        listViewAdapter.setOnCheckedItem(onCheckedItem);



        binding.searchHere.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence keyword, int start, int before, int count) {
                listViewAdapter.getFilter().filter(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getBrandList() {
        Utility.Log("API : ", APIs.GET_USER_ADMIN);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, APIs.GET_USER_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Utility.Log("GET_USER_ADMIN : ", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray dataJson = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataJson.length(); i++) {
                        JSONObject userObject = dataJson.getJSONObject(i);
                        UserListModel userListModel = new UserListModel();
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
                Log.e("Token", params.toString());
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

    @Override
    public void onBackPressed() {
        CodeReUse.activityBackPress(act);
    }

}