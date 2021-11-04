package com.app.brandmania.Activity.basics;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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
        binding.referralCodeTxt.setText(preafManager.getReferCode());
        binding.walletMoney.setText(preafManager.getWallet());
        binding.referralCodeTxt.setTextIsSelectable(true);
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flipping);
        anim.setTarget(binding.coinImg);
        anim.setDuration(3000);
        anim.start();
        //Invite your friends to BrandMania by sharing your referral code and Earn Discount on All your Friend's First Payment.Your friend also get assured discount on their first payment
        //change msg, add (-) to discount ,sticky bar in refer
        binding.msgTxt.setText("Invite your friends to BrandMania by sharing your referral code and Earn Discount on All your Friend's First Payment.Your friend also get assured discount on their first payment.");
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
                shortenLongLink();
            }
        });

        binding.referralCodeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", binding.referralCodeTxt.getText().toString());
                clipboard.setPrimaryClip(clip);
                showDialog(binding.referralCodeTxt.getText().toString());
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
                         //   Log.e("error", gson.toJson(task));
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
                    binding.referralCodeTxt.setText(jsonArray1.getString("referal_code"));
                    binding.walletMoney.setText(jsonArray1.getString("user_total_coin"));
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


    private void showDialog(String code) {

        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        TextView textCode = dialog.findViewById(R.id.text);
        textCode.setText(code);
        dialog.show();

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.cancel();
                dialog.dismiss();
            }
        }, 1500);

    }
}
