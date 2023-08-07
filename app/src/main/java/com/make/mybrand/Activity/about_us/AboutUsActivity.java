package com.make.mybrand.Activity.about_us;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.make.mybrand.Connection.BaseActivity;
import com.make.mybrand.R;
import com.make.mybrand.databinding.ActivityAboutUsBinding;
import com.make.mybrand.utils.Utility;

public class AboutUsActivity extends BaseActivity {
    Activity act;
    private ActivityAboutUsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        act = this;
        binding = DataBindingUtil.setContentView(act, R.layout.activity_about_us);
        Utility.isLiveModeOff(act);
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        if (getIntent().hasExtra("termsNCondition")) {
//            binding.webView.loadUrl("http://site.queryfinders.com/brandmania/privacy_policy.html");
//            binding.toolbarTitle.setText("Terms & Conditions");
//        } else {
//            binding.webView.loadUrl("http://site.queryfinders.com/brandmania/index.html");
//            binding.toolbarTitle.setText("About us");
//        }
//        binding.webView.setWebViewClient(new MyWebViewClient(this));

        loadData();
    }


    boolean loadBrandmaniaSite = false;


    @SuppressLint("SetJavaScriptEnabled")
    public void loadData() {

        //binding.webView.setWebChromeClient(new MyWebChromeClient());
        binding.webView.setWebViewClient(new webClient());
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);

        String url = "";
        if (!loadBrandmaniaSite) {
            if (getIntent().hasExtra("termsNCondition")) {
                url = "http://site.queryfinders.com/brandmania/privacy_policy.html";
                binding.toolbarTitle.setText("Terms & Conditions");
            } else {
                url = "http://site.queryfinders.com/brandmania/index.html";
                binding.toolbarTitle.setText("About us");
            }
        } else {
            loadBrandmaniaSite = false;
            url = "http://brandmaniaapp.in/";
        }
        binding.webView.setVisibility(View.GONE);
        binding.webView.loadUrl(url);

    }

    public class MyWebChromeClient extends WebChromeClient {

        public void onProgressChanged(WebView view, int newProgress) {
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
            binding.webView.setVisibility(View.VISIBLE);
            binding.simpleProgressBar.setProgress(newProgress);
        }

    }

    public class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.simpleProgressBar.setVisibility(View.GONE);
            binding.webView.setVisibility(View.VISIBLE);
            binding.webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (errorResponse.getStatusCode() == 500) {
                binding.simpleProgressBar.setVisibility(View.VISIBLE);
                binding.webView.setVisibility(View.GONE);
                loadBrandmaniaSite = true;
                loadData();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //Log.e("Error",gson.toJson(error));
            binding.simpleProgressBar.setVisibility(View.VISIBLE);
            loadBrandmaniaSite = true;
            loadData();
        }
    }

}