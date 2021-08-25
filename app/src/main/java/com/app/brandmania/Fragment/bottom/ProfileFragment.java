package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.about_us.AboutUsActivity;
import com.app.brandmania.Activity.about_us.AddReportAndBug;
import com.app.brandmania.Activity.about_us.AppIntroActivity;
import com.app.brandmania.Activity.about_us.FaqActivity;
import com.app.brandmania.Activity.about_us.HelpAndSupport;
import com.app.brandmania.Activity.about_us.PartnerProgramActivity;
import com.app.brandmania.Activity.basics.LoginActivity;
import com.app.brandmania.Activity.brand.ViewBrandActivity;
import com.app.brandmania.Activity.packages.PackageActivity;
import com.app.brandmania.Common.Constant;
import com.app.brandmania.Common.HELPER;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Fragment.BaseFragment;
import com.app.brandmania.Model.BrandListItem;
import com.app.brandmania.R;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.DialogFacebookLikesBinding;
import com.app.brandmania.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ProfileFragment extends BaseFragment {
    Activity act;
    private FragmentProfileBinding binding;
    PreafManager preafManager;


    @Override public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
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
        binding.introLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AppIntroActivity.class);
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

            }
        });
        if (!preafManager.getAppTutorial().isEmpty()){
            binding.introLayout.setVisibility(View.VISIBLE);
            binding.videoLine.setVisibility(View.VISIBLE);
        }

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
                i.putExtra("aboutUs","aboutUs");
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
        binding.appVersionTxt.setText("App Version "+Constant.F_VERSION);

        binding.contactTxtLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HELPER.WHATSAPP_REDIRECTION_2(act,preafManager.getActiveBrand().getName(),preafManager.getMobileNumber());
            }
        });
        binding.visitFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookPageDialog();
            }
        });

        binding.privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, AboutUsActivity.class);
                i.putExtra("termsNCondition", "aboutUs");
                startActivity(i);
                act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });

        return binding.getRoot();
    }
    public static String FACEBOOK_URL = "https://www.facebook.com/brandmania2020";
    public static String FACEBOOK_PAGE_ID = "brandmania2020";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
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
    @Override public void update(Observable observable, Object data) {

        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.REFRESH_BRAND_NAME) {


            getBrandList();
        }
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.APP_INTRO_REFRESH) {
            preafManager=new PreafManager(act);
            if (!preafManager.getAppTutorial().isEmpty()){
                binding.introLayout.setVisibility(View.VISIBLE);
                binding.videoLine.setVisibility(View.VISIBLE);
            }
        }
    }

    public DialogFacebookLikesBinding facebookLikesBinding;
    private void facebookPageDialog() {
        facebookLikesBinding = DataBindingUtil.inflate(LayoutInflater.from(act), R.layout.dialog_facebook_likes, null, false);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(act, R.style.MyAlertDialogStyle_extend);
        builder.setView(facebookLikesBinding.getRoot());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.setContentView(facebookLikesBinding.getRoot());

        facebookLikesBinding.viewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(act);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        facebookLikesBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
}
