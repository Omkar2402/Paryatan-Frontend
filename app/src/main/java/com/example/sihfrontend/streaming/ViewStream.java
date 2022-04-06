package com.example.sihfrontend.streaming;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.example.sihfrontend.R;
import com.example.sihfrontend.user.monument.MonumentDescription;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.graphics.Point;
import android.view.Display;
import android.widget.MediaController;
import android.view.MotionEvent;

public class ViewStream extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        WebView browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new MyBrowser());
        button  = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Adding this line will prevent taking screenshot in your app

                    String url = "https://cdn.bambuser.net/player/?resourceUri=https%3A%2F%2Fcdn.bambuser.net%2Fgroups%2F105119%2Fbroadcasts%3Fby_authors%3D%26title_contains%3D%26has_any_tags%3D%26has_all_tags%3D%26da_id%3D4da957b0-c068-f529-1f39-e3136f98aa82%26da_timestamp%3D1649128470%26da_signature_method%3DHMAC-SHA256%26da_ttl%3D0%26da_static%3D1%26da_signature%3D71eae07c4f9d566fd961d2d5743700c7a3da83a4dee497f450ce1885c0536cbd";
//                    String url = "http://www.google.com";
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browser.getSettings().setLoadsImagesAutomatically(true);
                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                    browser.loadUrl(url);
//                    startActivity(browserIntent);
//                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//                    CustomTabsIntent customTabsIntent = builder.build();
//                    customTabsIntent.launchUrl(ViewStream.this, Uri.parse(url));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}