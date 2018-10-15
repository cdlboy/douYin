package com.douyin.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.douyin.R;

public class lastActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private RelativeLayout relativeLayout_wifi1;
    private RelativeLayout relativeLayout_wifi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("Wifi模块");
        relativeLayout_wifi1 = (RelativeLayout) findViewById(R.id.wifi1);
        relativeLayout_wifi1.setOnClickListener(lastActivity.this);
        relativeLayout_wifi2 = (RelativeLayout) findViewById(R.id.wifi2);
        relativeLayout_wifi2.setOnClickListener(lastActivity.this);
        ActivityCollector.addActivity(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wifi1:
                Intent intent = new Intent(lastActivity.this, WifiActivity.class);
                startActivity(intent);
                break;
            case R.id.wifi2:
                Intent intent2 = new Intent(lastActivity.this, WifiActivity2.class);
                startActivity(intent2);
        }
    }
}
