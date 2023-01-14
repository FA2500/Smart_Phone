package com.example.smart_phone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class BillPlz extends AppCompatActivity {
    private String ID;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_plz);

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("ID");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Map<String, String> map = new HashMap<>();
        map.put("email", userInfo.getEmail());
        map.put("phone", "01156403489");
        map.put("name", userInfo.getName());
        map.put("amount", String.valueOf(1 * 100));
        map.put("desc"," Hotel Booking");

        myWebView.loadUrl("https://afcgame.online/pay",map);

        myWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                Log.d("Webview","Current URL = "+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(url.contains("true"))
                {
                    nav( "True",url);
                }
                else if(url.contains("false"))
                {
                    nav( "Failed",url);
                }
            }
        });
    }

    public void nav(String status,String query)
    {
        if(status=="True")
        {
            Intent intent = new Intent(BillPlz.this, Receipt.class);
            intent.putExtra("status",status);
            intent.putExtra("query",query);
            startActivity(intent);
        }
        else if(status=="False")
        {
            Intent intent = new Intent(BillPlz.this, RoomDetail.class);
            intent.putExtra("ID",ID);
            startActivity(intent);
        }


    }
}