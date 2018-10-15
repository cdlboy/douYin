package com.douyin.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.douyin.R;

/**
 *
 */
public class WifiActivity2 extends Activity {

    private WebView wifi;
    private TextView title;
    private ProgressDialog dialog;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi2);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("局域网控制");

        wifi = (WebView) findViewById(R.id.wifi_webview2);


        WebSettings webSettings = wifi.getSettings();


        webSettings.setJavaScriptEnabled(true);


        wifi.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {


                super.onPageFinished(view, url);
                dialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                super.onPageStarted(view, url, favicon);
                dialog = ProgressDialog.show(WifiActivity2.this, "",

                        "加载网页中，请稍等...", true, true);
            }

        });

        wifi.loadUrl("http://192.168.4.1/operation");


        ActivityCollector.addActivity(this);
    }


    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
