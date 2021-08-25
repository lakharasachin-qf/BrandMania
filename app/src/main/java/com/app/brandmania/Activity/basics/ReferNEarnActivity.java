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

import com.app.brandmania.Activity.HomeActivity;
import com.app.brandmania.Common.PreafManager;
import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityReferBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class ReferNEarnActivity extends BaseActivity {

    Activity act;
    private ActivityReferBinding binding;
    PreafManager preafManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        Window w = getWindow();
           /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);*/
        binding = DataBindingUtil.setContentView(act, R.layout.activity_refer);
        preafManager = new PreafManager(act);
        Log.w(preafManager.getReferCode(), "data");
        binding.referalCodeTxt.setText(preafManager.getReferCode());
        binding.referalCodeTxt.setTextIsSelectable(true);
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
                "link=http://www.queryfinders.com?refer="+preafManager.getActiveBrand().getId() +preafManager.getReferCode()+
                "&apn=" + getPackageName() +
                "&st=" + "Referral Code" +
                "&sd=" + "Reward 20" +
                "&si=" + "https://www.blueappsoftware.com/wp-content/uploads/2018/06/blueapp-software-144-350.png";


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

    public void shareLink(Uri myDynamicLink) {
        // [START ddl_share_link]
        Intent sendIntent = new Intent();
        String msg = "Hey, check this out: " + myDynamicLink;
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
        // [END ddl_share_link]
    }
}
