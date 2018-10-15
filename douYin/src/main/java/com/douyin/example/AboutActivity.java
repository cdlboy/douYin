package com.douyin.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.douyin.R;

public class AboutActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("关于软件");
        ActivityCollector.addActivity(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);

    }
}
