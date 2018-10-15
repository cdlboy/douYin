package com.douyin.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.douyin.R;
import com.douyin.model.DouYinApplication;

public class PeopleActivity extends Activity {
	
	private TextView user_name;
	private TextView user_tel;
	private TextView user_addtime;
	private TextView device_id;
	private TextView device_addtime;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_people);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("信息详情");
		user_name = (TextView) findViewById(R.id.text_user_name);
		user_tel = (TextView) findViewById(R.id.text_user_tel);
		user_addtime = (TextView) findViewById(R.id.text_user_addtime);
		device_id = (TextView) findViewById(R.id.text_device_id);
		device_addtime = (TextView) findViewById(R.id.text_device_addtime);
		user_name.setText(((DouYinApplication) getApplication()).getUser_name());
		user_tel.setText(((DouYinApplication) getApplication()).getUser_tele());
		user_addtime.setText(((DouYinApplication) getApplication()).getUser_addtime());
		device_id.setText(((DouYinApplication) getApplication()).getDevice_id());
		device_addtime.setText(((DouYinApplication) getApplication()).getDevice_addtime());
		ActivityCollector.addActivity(this);
	}

	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.people, menu);
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
