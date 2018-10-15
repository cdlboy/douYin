package com.douyin.example;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TongjiActivity extends Activity {


    private WebView webview;

    private TextView title;

    private ProgressDialog dialog;


    public interface AsyncResponse {
        void onDataReceivedSuccess(String cd);

        void onDataReceivedFailed();
    }

    class GetTemp extends AsyncTask<Void, String, String> {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }


        protected String doInBackground(Void... params) {


            String rsl;
            String cookie = null;
            try {
                rsl = AppInterface.login(((DouYinApplication) getApplication()).getUser_name(),
                        ((DouYinApplication) getApplication()).getUser_password());
                JSONObject json = new JSONObject(rsl);
                cookie = json.getString("cookie");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return cookie;


        }


        protected void onPostExecute(String result) {
            if (result != null) {
                asyncResponse.onDataReceivedSuccess(result);
            } else {
                asyncResponse.onDataReceivedFailed();
            }
        }

    }

    @JavascriptInterface
    private void initWebViewSettings() {
//        myWebView.getSettings().setSupportZoom(true);
//        myWebView.getSettings().setBuiltInZoomControls(true);
//        myWebView.getSettings().setDefaultFontSize(12);
//        myWebView.getSettings().setLoadWithOverviewMode(true);
        // 璁剧疆鍙互璁块棶鏂囦欢
        webview.getSettings().setAllowFileAccess(true);
        //濡傛灉璁块棶鐨勯〉闈腑鏈塉avascript锛屽垯webview蹇呴』璁剧疆鏀寔Javascript

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.getSettings().setUserAgentString(MyApplication.getUserAgent());
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);

        webview.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }



            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                super.onPageStarted(view, url, favicon);
                dialog = ProgressDialog.show(TongjiActivity.this, "",

                        "加载网页中，请稍等...", true, true);
            }

            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                super.onPageFinished(view, url);
            }

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_tongji);


        webview = (WebView) findViewById(R.id.webView);


        title = (TextView) findViewById(R.id.title_title);

        initWebViewSettings();
        GetTemp gettemp = new GetTemp();

        gettemp.execute();
        gettemp.setOnAsyncResponse(new AsyncResponse() {
            public void onDataReceivedSuccess(String result) {

                String cookie[] = result.split(";");
                Log.d("data", cookie[0] + "cookie1");
                Log.d("data", cookie[1] + "cookie2");
                CookieSyncManager.createInstance(TongjiActivity.this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                cookieManager.removeSessionCookie();
                cookieManager.setCookie("http://bysj.douyin.xyz/index.php/Admin/Data/showlist.html", cookie[0]);
                cookieManager.setCookie("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataTemp.html", cookie[0]);
                cookieManager.setCookie("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataBing.html", cookie[0]);
                cookieManager.setCookie("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataWeather.html", cookie[0]);
                Intent intent = getIntent();//获取传来的intent对象
                String style = intent.getStringExtra("webview");//获取键值对的键名
                if (style.equals("1")) {

                    title.setText("实时数据");










                    webview.loadUrl("http://bysj.douyin.xyz/index.php/Admin/Data/showlist.html");

                } else if (style.equals("2")) {

                    title.setText("各时段温度折线图");
                    webview.loadUrl("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataTemp.html");
                } else if (style.equals("3")) {
                    title.setText("各时段温度基础值");
                    webview.loadUrl("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataBing.html");
                } else {
                    title.setText("任务操作统计");
                    webview.loadUrl("http://bysj.douyin.xyz/index.php/Admin/Calculate/dataWeather.html");
                }


            }

            @Override
            public void onDataReceivedFailed() {

            }

        });

        ActivityCollector.addActivity(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tongji, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
