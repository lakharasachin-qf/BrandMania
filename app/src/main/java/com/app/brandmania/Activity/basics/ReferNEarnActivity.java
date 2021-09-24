package com.app.brandmania.Activity.basics;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Common.MakeMyBrandApp;
import com.app.brandmania.Common.ObserverActionID;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Common.ResponseHandler;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityReferBinding;
import com.app.brandmania.utils.APIs;
import com.app.brandmania.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferNEarnActivity extends BaseActivity {

    Activity act;
    private String deviceToken = "";
    private ActivityReferBinding binding;
    PreafManager preafManager;

    public void getDeviceToken(Activity act) {
        Utility.showProgress(act);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                deviceToken = task.getResult();
                UpdateToken();
            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        act = this;
        getDeviceToken(act);
        binding = DataBindingUtil.setContentView(act, R.layout.activity_refer);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        preafManager = new PreafManager(act);
        Log.w(preafManager.getReferCode(), "data");
        binding.referalCodeTxt.setText(preafManager.getReferCode());
        binding.walletMoney.setText(preafManager.getWallet());
        binding.referalCodeTxt.setTextIsSelectable(true);
        binding.msgTxt.setText("Refer a friend earn discount of all your friends purchases for 1 year. your friends also earn " + act.getString(R.string.Rs) + preafManager.getWallet() + " on Sign-up.");
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(act, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        binding.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sharebody = preafManager.getReferCode();
                String sharebody = preafManager.getReferCode();
                // shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = "https://play.google.com/store/apps/details?id=com.make.mybrand";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, app_url);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharebody);
                startActivity(Intent.createChooser(shareIntent, "Share via"));  */
                shortenLongLink();
            }
        });

        binding.referalCodeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", binding.referalCodeTxt.getText().toString());
                if (clipboard == null || clip == null) return;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(act, "Copied", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void shortenLongLink() {

        String shareLinkText = "https://brandmania.page.link/?" +
                "link=http://www.queryfinders.com?refer=" + preafManager.getReferCode() +
                "&apn=" + getPackageName() +
                "&st=" + "Referral Code" +
                "&sd=" + "Earn Rewards" +
                "&si=" + "http://queryfinders.com/brandmania/brandMania/website/images/refer_and_earn_brandmania_mobile_app_play_store.png";


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinkText))
                .buildShortDynamicLink()

                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("shortLink", String.valueOf(shortLink));
                            Log.e("flowchartLink", String.valueOf(flowchartLink));
                            shareLink(shortLink);
                        } else {
                            Log.e("error", gson.toJson(task));
                        }
                    }
                });
    }

    private void UpdateToken() {
        Utility.Log("TokenURL", APIs.UPDATE_TOKEN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, APIs.UPDATE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utility.Log("UPDATE_TOKENnn", response);
                Utility.dismissProgress();
                // {"status":true,"data":[{"id":1,"video_url_path":"http:\/\/queryfinders.com\/brandmania_uat\/public\/storage\/uploads\/video\/Skype_Video.mp4"}],"message":"Device Token Updated."}
                JSONObject jsonObject = ResponseHandler.createJsonObject(response);
                try {
                    JSONObject jsonArray1 = jsonObject.getJSONObject("message");
                    preafManager.setWallet(jsonArray1.getString("user_total_coin"));
                    if (jsonArray1.getString("reference_code").equals("null"))
                        jsonArray1.put("reference_code", "");

                    preafManager.setReferCode(jsonArray1.getString("referal_code"));
                    preafManager.setSpleshReferrer(jsonArray1.getString("reference_code"));
                    preafManager.setReferrerCode(jsonArray1.getString("reference_code"));
                    binding.referalCodeTxt.setText(jsonArray1.getString("referal_code"));
                    binding.walletMoney.setText(jsonArray1.getString("user_total_coin"));
                    //setupReferrerCode();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.dismissProgress();
                error.printStackTrace();
            }
        }) {
            /**
             * Passing some request headers*
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer" + preafManager.getUserToken());
                return params;

            }


            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("firebase_token", deviceToken);
                Utility.Log("Verify-Param", hashMap.toString());
                return hashMap;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(act);
        queue.add(stringRequest);
    }

    public void shareLink(Uri myDynamicLink) {
        // [START ddl_share_link]
        Intent sendIntent = new Intent();
        String msg = "Hello!! I invite you to use Brandmania app.\n" + "\n" +
                "Use bellow link to download and get assured discount on your payment.\n" +
                "\n" +
                "Brand mania app is used to make social media marketing image for your business in 5 minutes. Here Festival images, National and International Days images will be provided.\n" +
                "\n" + "Referral code: " +
                preafManager.getReferCode() + "\n" + "Try it now:\n" + myDynamicLink;
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        // [END ddl_share_link]
    }
}
