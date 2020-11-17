package com.app.brandmania.Activity;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class MyWebViewClient extends WebViewClient {

    private Context context;
    public MyWebViewClient(Context context) {
        this.context = context;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(url.equals("hrupin://second_activity")){

            Intent i = new Intent(context, HomeActivity.class);

            context.startActivity(i);

            return true;

        }

        return super.shouldOverrideUrlLoading(view, url);

    }
}
