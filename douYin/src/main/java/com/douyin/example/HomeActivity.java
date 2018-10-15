package com.douyin.example;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.douyin.R;

public class HomeActivity extends Activity implements OnClickListener {

	private LinearLayout Layout_control, Layout_setup, Layout_data;
	private ImageView img_control, img_setup, img_data, img_current;
	private TextView text_control, text_setup, text_data, text_current;
	private FragmentManager fragmentManager;
	private FragmentTransaction beginTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		Layout_control = (LinearLayout) findViewById(R.id.layout_control);
		Layout_setup = (LinearLayout) findViewById(R.id.layout_setup);
		Layout_data = (LinearLayout) findViewById(R.id.layout_data);
		

		Layout_control.setOnClickListener(this);
		Layout_setup.setOnClickListener(this);
		Layout_data.setOnClickListener(this);

		img_control = (ImageView) findViewById(R.id.img_control);
		img_setup = (ImageView) findViewById(R.id.img_setup);
		img_data = (ImageView) findViewById(R.id.img_data);

		text_control = (TextView) findViewById(R.id.text_control);
		text_setup = (TextView) findViewById(R.id.text_setup);
		text_data = (TextView) findViewById(R.id.text_data);

		img_control.setSelected(true);
		text_control.setSelected(true);
		img_current = img_control;
		text_current = text_control;

		fragmentManager = getFragmentManager();
		beginTransaction = fragmentManager.beginTransaction();
		beginTransaction.replace(R.id.fragment_main, new ControlFragment());
		beginTransaction.commit();
		ActivityCollector.addActivity(this);
	}

	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onClick(View v) {
		img_current.setSelected(false);
		text_current.setSelected(false);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.layout_control:
			beginTransaction.replace(R.id.fragment_main, new ControlFragment());
			img_control.setSelected(true);
			img_current = img_control;
			text_control.setSelected(true);
			text_current = text_control;
			break;
		case R.id.layout_data:
			beginTransaction.replace(R.id.fragment_main, new DataFragment());
			img_data.setSelected(true);
			img_current = img_data;
			text_data.setSelected(true);
			text_current = text_data;
			break;
		case R.id.layout_setup:
			beginTransaction.replace(R.id.fragment_main, new SetupFragment());
			img_setup.setSelected(true);
			img_current = img_setup;
			text_setup.setSelected(true);
			text_current = text_setup;
			break;
		default:
			break;
		}
		beginTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
