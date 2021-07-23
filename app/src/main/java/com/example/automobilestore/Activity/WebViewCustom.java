package com.example.automobilestore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.automobilestore.R;

public class WebViewCustom extends AppCompatActivity {
    private WebView webview;
    String link="https://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_custom);
//        final ProgressDialog pd;
//        pd = new ProgressDialog(WebViewCustom.this);
//        pd.setMessage("Loading...");
//        pd.show();
        Intent intent = getIntent();
            if(intent.getStringExtra("Link").startsWith("http")) {
                link = intent.getStringExtra("Link");
            }else{
                Toast.makeText(this, "Link  Expire", Toast.LENGTH_SHORT).show();
            }

        webview =(WebView)findViewById(R.id.webView);
//        webview.getSettings().setBlockNetworkImage(true);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(link);

//        SystemClock.sleep(3000);
//        pd.dismiss();
    }
}