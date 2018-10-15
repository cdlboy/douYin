package com.douyin.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity implements OnClickListener {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText EditText_name;

    private EditText EditText_password;



    private Button button;


    private TextView wangji_button;
    private TextView zhuce_button;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.login_btn);

        EditText_name = (EditText) findViewById(R.id.login_input_name);
        EditText_password = (EditText) findViewById(R.id.login_input_password);
        button.setOnClickListener(this);

        zhuce_button = (TextView) findViewById(R.id.zhuce_textview);
        zhuce_button.setOnClickListener(this);
        wangji_button = (TextView) findViewById(R.id.wangji_textview);
        wangji_button.setOnClickListener(this);
        ActivityCollector.addActivity(this);



    }

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce_textview:
                Intent intent3 = new Intent(MainActivity.this, ZhuCeActivity.class);
                startActivity(intent3);
                break;
            case R.id.wangji_textview:
                Intent intent2 = new Intent(MainActivity.this, WangJi1Activity.class);
                startActivity(intent2);
                break;
            case R.id.login_btn:
                if (!isNetworkAvailable(MainActivity.this))
                {
                    AlertDialog.Builder warm_dialog = new AlertDialog.Builder(MainActivity.this);
                    warm_dialog.setTitle("提示");
                    warm_dialog.setMessage("当前网络不可用！");
                    warm_dialog.setCancelable(true);
                    warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                }
                else {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    String wifi_name = wifiInfo.getSSID();
                    String wifi_name2 = wifi_name.replaceAll("\"", "");
                    if (wifi_name2.equals("myssid")) {
                        AlertDialog.Builder warm_dialog = new AlertDialog.Builder(MainActivity.this);
                        warm_dialog.setTitle("提示");
                        warm_dialog.setMessage("当前网络不可用！");
                        warm_dialog.setCancelable(true);
                        warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String name = EditText_name.getText().toString();
                                    String password = EditText_password.getText().toString();
                                    String rsl = AppInterface.login(name, password);

                                    JSONObject json = new JSONObject(rsl);

                                    String error = json.getString("error");

                                    if (error.equals("0")) {
                                        String cookie = json.getString("cookie");

                                        editor = getSharedPreferences("data", 0).edit();
                                        editor.putString("name", name);
                                        editor.putString("password", password);
                                        editor.commit();
                                        setUserValues(cookie);

                                        Looper.prepare();
                                        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "",

                                                "加载中，请稍等...", true, true);

                                        Thread t = new Thread(new Runnable() {

                                            @Override

                                            public void run() {

                                                try {

                                                    Thread.sleep(3000);//让他显示10秒后，取消ProgressDialog

                                                } catch (InterruptedException e) {

// TODO Auto-generated catch block

                                                    e.printStackTrace();

                                                }

                                                dialog.dismiss();
                                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                                                startActivity(intent);
                                            }

                                        });

                                        t.start();

                                        Looper.loop();


                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(MainActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }

                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void setUserValues(String cookie) {
        try {

            String userInfo = AppInterface.getUserInfo(cookie);
            String deviceInfo = AppInterface.getDeviceInfo(cookie);
            JSONObject json_user = new JSONObject(userInfo);
            JSONObject json_device = new JSONObject(deviceInfo);

            String user_id = json_user.getString("id");
            String user_name = json_user.getString("username");
            String user_password = json_user.getString("password");
            String user_tele = json_user.getString("tele");
            String user_addtime = json_user.getString("addtime");
            String user_picture = json_user.getString("user_pic");

            ((DouYinApplication) getApplication()).setUser_id(user_id);
            ((DouYinApplication) getApplication()).setUser_name(user_name);
            ((DouYinApplication) getApplication()).setUser_password(user_password);
            ((DouYinApplication) getApplication()).setUser_tele(user_tele);
            ((DouYinApplication) getApplication()).setUser_addtime(user_addtime);
            ((DouYinApplication) getApplication()).setUser_picture(user_picture);

            String device_id = json_device.getString("device_id");
            String device_addtime = json_device.getString("addtime");
            String device_status = json_device.getString("status");

            ((DouYinApplication) getApplication()).setDevice_id(device_id);
            ((DouYinApplication) getApplication()).setDevice_addtime(device_addtime);
            ((DouYinApplication) getApplication()).setDevice_status(device_status);

            editor = getSharedPreferences("data", 0).edit();
            editor.putString("id", user_id);
            editor.putString("name", user_name);
            editor.putString("password", user_password);
            editor.putString("tele", user_tele);
            editor.putString("addtime", user_addtime);
            editor.putString("user_pic", user_picture);
            editor.putString("device_id", device_id);
            editor.putString("addtime", device_addtime);
            editor.putString("status", device_status);
            editor.commit();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
