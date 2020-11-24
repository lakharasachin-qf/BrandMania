package com.app.brandmania.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.app.brandmania.Connection.BaseActivity;
import com.app.brandmania.R;
import com.app.brandmania.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity {
    Activity act;
    ImageView BackButton;
    WebView webViewhtml;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_material_theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        BackButton=findViewById(R.id.BackButton);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webViewhtml = (WebView) findViewById(R.id.webView);

        webViewhtml.loadUrl("http://site.queryfinders.com/brandmania/index.html");
        webViewhtml.setWebViewClient(new MyWebViewClient(this));
    }
}