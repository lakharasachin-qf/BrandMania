package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.AboutUsActivity;
import com.app.brandmania.Activity.AddReportAndBug;
import com.app.brandmania.Activity.FaqActivity;
import com.app.brandmania.Activity.LoginActivity;
import com.app.brandmania.Activity.PackageActivity;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Activity.HelpAndSupport;
import com.app.brandmania.Activity.PartnerProgramActivity;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.Activity.ViewBrandActivity;
import com.app.brandmania.Utils.APIs;
import com.app.brandmania.Utils.Utility;
import com.app.brandmania.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ProfileFragment extends BaseFragment {
    Activity act;
    private FragmentProfileBinding binding;
    PreafManager preafManager;


    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_profile,parent,false);
        preafManager=new PreafManager(act);
        binding.businessName.setText(preafManager.getActiveBrand().getName());
        binding.mybusinessRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, ViewBrandActivity.class);

                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        binding.logoutRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preafManager.Logout();
                Intent i = new Intent(act, LoginActivity.class);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                act.finish();
            }
        });
        binding.helpandsupportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, HelpAndSupport.class);

                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        binding.partnerProgRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(act, PartnerProgramActivity.class);

                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        binding.myFaqRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, FaqActivity.class);
                 startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        binding.aboutUsRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AboutUsActivity.class);

                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        binding.shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                // shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = "https://play.google.com/store/apps/details?id=com.make.mybrand";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        binding.packageRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(act, PackageActivity.class);
                intent.putExtra("Profile","1");
                startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        binding.rateUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.make.mybrand");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                try {

                    startActivity(intent);
                }
                catch (Exception e)
                {

                }

            }
        });
        binding.reportbugsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(act, AddReportAndBug.class);
                startActivity(intent);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
        return binding.getRoot();
    }

    ArrayList<BrandListItem> multiListItems=new ArrayList<>();
    private void getBrandList() {

        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("GET_BRAND : ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                    preafManager.setAddBrandList(multiListItems);
                    for (int i=0;i<multiListItems.size();i++){
                        if (multiListItems.get(i).getId().equalsIgnoreCase(preafManager.getActiveBrand().getId())){
                            preafManager.setActiveBrand(multiListItems.get(i));
                            break;
                        }
                    }

                    //FirstLogin
                    if (act.getIntent().hasExtra("FirstLogin")){
                        preafManager.setIS_Brand(true);

                        if (multiListItems.size() != 0) {
                            preafManager.setActiveBrand(multiListItems.get(0));
                        }

                    }
                    preafManager=new PreafManager(act);
                    binding.businessName.setText(preafManager.getActiveBrand().getName());
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
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("Authorization","Bearer "+preafManager.getUserToken());
                Log.e("Token",params.toString());
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

    @Override
    public void update(Observable observable, Object data) {

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_BRAND_NAME) {


            getBrandList();
        }

    }
}
