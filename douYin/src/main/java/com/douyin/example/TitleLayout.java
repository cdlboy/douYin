package com.douyin.example;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.douyin.R;

public class TitleLayout extends LinearLayout {

	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.title, this);
		RelativeLayout title_back = (RelativeLayout) findViewById(R.id.title_back);
		title_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((Activity) getContext()).finish();
			}
		});
	}
}
