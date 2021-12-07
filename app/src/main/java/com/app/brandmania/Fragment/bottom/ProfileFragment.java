package com.app.brandmania.Fragment.bottom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.app.brandmania.Activity.basics.ReferNEarnActivity;
import com.app.brandmania.Activity.brand.EditActivity;
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
import com.app.brandmania.databinding.DialogFacebookLikesBinding;
import com.app.brandmania.databinding.FragmentProfileBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ProfileFragment extends BaseFragment {
    private Activity act;
    private FragmentProfileBinding binding;

    @Override
    public View provideFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        act = getActivity();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, parent, false);

        if (prefManager.getActiveBrand() != null)
            binding.businessName.setText(prefManager.getActiveBrand().getName());

        binding.editProfile.setOnClickListener(v -> {
            Intent i = new Intent(act, EditActivity.class);
            startActivity(i);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        binding.mybusinessRelative.setOnClickListener(v -> {
            Intent i = new Intent(act, ViewBrandActivity.class);
            startActivity(i);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });

        binding.introLayout.setOnClickListener(v -> {
            Intent i = new Intent(act, AppIntroActivity.class);
            startActivity(i);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });

        if (!prefManager.getAppTutorial().isEmpty()) {
            binding.introLayout.setVisibility(View.VISIBLE);
            binding.videoLine.setVisibility(View.VISIBLE);
        }

        binding.logoutRelative.setOnClickListener(v -> {
            prefManager.Logout();
            Intent i = new Intent(act, LoginActivity.class);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            act.finish();
        });

        if (prefManager.getActiveBrand() != null) {
            binding.referNEarnLayout.setOnClickListener(v -> HELPER.ROUTE(act, ReferNEarnActivity.class));
        } else {
            binding.referNEarnLayout.setVisibility(View.GONE);
        }

        binding.helpandsupportLayout.setOnClickListener(v -> HELPER.ROUTE(act, HelpAndSupport.class));
        binding.partnerProgRelative.setOnClickListener(v -> HELPER.ROUTE(act, PartnerProgramActivity.class));
        binding.myFaqRelative.setOnClickListener(v -> HELPER.ROUTE(act, FaqActivity.class));
        binding.aboutUsRelative.setOnClickListener(v -> {
            Intent i = new Intent(act, AboutUsActivity.class);
            i.putExtra("aboutUs", "aboutUs");
            startActivity(i);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

        });
        binding.shareText.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            // shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
            String app_url = "https://play.google.com/store/apps/details?id=com.make.mybrand";
            shareIntent.putExtra(Intent.EXTRA_TEXT, app_url);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
        binding.packageRelative.setOnClickListener(view -> {
            Intent intent = new Intent(act, PackageActivity.class);
            intent.putExtra("Profile", "1");
            startActivity(intent);
            act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
        });
        binding.rateUsLayout.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.make.mybrand");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        binding.reportbugsLayout.setOnClickListener(view -> HELPER.ROUTE(act, AddReportAndBug.class));
        binding.appVersionTxt.setText("App Version " + Constant.F_VERSION);

        binding.contactTxtLayout.setOnClickListener(v -> {
            if (prefManager.getActiveBrand() != null) {
                HELPER.WHATSAPP_REDIRECTION_2(act, prefManager.getActiveBrand().getName(), prefManager.getMobileNumber());
            } else {
                HELPER.WHATSAPP_REDIRECTION_2(act, "", prefManager.getMobileNumber());
            }
        });
        binding.visitFacebook.setOnClickListener(v -> facebookPageDialog());
        binding.websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri webpage = Uri.parse("http://brandmaniaapp.in");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                //intent.setPackage("com.android.chrome");
                startActivity(intent);
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
    public static String FACEBOOK_PAGE_ID = "103655598316587";

    ArrayList<BrandListItem> multiListItems = new ArrayList<>();

    private void getBrandList() {

        Utility.Log("API : ", APIs.GET_BRAND);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.GET_BRAND, response -> {
            Utility.Log("GET_BRAND : ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                multiListItems = ResponseHandler.HandleGetBrandList(jsonObject);
                if (multiListItems != null && multiListItems.size() != 0) {
                    prefManager.setAddBrandList(multiListItems);
                    for (int i = 0; i < multiListItems.size(); i++) {
                        if (multiListItems.get(i).getId().equalsIgnoreCase(prefManager.getActiveBrand().getId())) {
                            prefManager.setActiveBrand(multiListItems.get(i));
                            break;
                        }
                    }

                    //FirstLogin
                    if (act.getIntent().hasExtra("FirstLogin")) {
                        prefManager.setIS_Brand(true);
                        if (multiListItems.size() != 0) {
                            prefManager.setActiveBrand(multiListItems.get(0));
                        }
                    }

                    if (prefManager.getActiveBrand() == null) {
                        if (multiListItems.size() != 0) {
                            prefManager.setActiveBrand(multiListItems.get(0));
                        }
                    }
                    prefManager = new PreafManager(act);
                    binding.businessName.setText(prefManager.getActiveBrand().getName());
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
        if (MakeMyBrandApp.getInstance().getObserver().getValue() == ObserverActionID.APP_INTRO_REFRESH) {
            prefManager = new PreafManager(act);
            if (!prefManager.getAppTutorial().isEmpty()) {
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
                facebookIntent.setData(Uri.parse(FACEBOOK_URL));
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
