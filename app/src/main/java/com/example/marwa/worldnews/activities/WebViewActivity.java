package com.example.marwa.worldnews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.marwa.worldnews.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra("news");
        WebView web = (WebView) findViewById(R.id.webView);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(url);
    }
}
