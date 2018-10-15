package com.douyin.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class QiDongActivity extends Activity {

    private SharedPreferences pref;


    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_qidong);


        pref = getSharedPreferences("data", 0);
        final String SPname1 = pref.getString("name", "");
        final String SPpassword1 = pref.getString("password", "");
        if (SPname1.isEmpty() && SPpassword1.isEmpty()) {

            setUserValues();
            final Intent intent = new Intent(QiDongActivity.this, MainActivity.class);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            };
            timer.schedule(task, 1000 * 2); // 2秒后

            // startActivity(intent);
        } else {

            if (!isNetworkAvailable(QiDongActivity.this))
            {
                setUserValues();
                final Intent wifi_intent1 = new Intent(QiDongActivity.this, HomeActivity.class);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(wifi_intent1);
                    }
                };
                timer.schedule(task, 1000 * 2); // 10秒后
            }
            else {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                String wifi_name = wifiInfo.getSSID();
                String wifi_name2 = wifi_name.replaceAll("\"", "");

                if (wifi_name2.equals("myssid")) {
                    setUserValues();
                    final Intent wifi_intent2 = new Intent(QiDongActivity.this, HomeActivity.class);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(wifi_intent2);
                        }
                    };
                    timer.schedule(task, 1000 * 2); // 10秒后
                } else {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String rsl = AppInterface.login(SPname1, SPpassword1);
                                JSONObject json = new JSONObject(rsl);
                                String error = json.getString("error");
                                if (error.equals("0")) {
                                    String cookie = json.getString("cookie");
                                    setUserValues(cookie);
                                    final Intent intent = new Intent(QiDongActivity.this, HomeActivity.class);
                                    Timer timer = new Timer();
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(intent);
                                        }
                                    };
                                    timer.schedule(task, 1000 * 2); // 10秒后
                                } else {
                                    final Intent intent = new Intent(QiDongActivity.this, MainActivity.class);

                                    Timer timer = new Timer();
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            startActivity(intent);
                                        }
                                    };
                                    timer.schedule(task, 1000 * 2); // 2秒后
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
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qi_dong, menu);
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

    /**
     *
     */
    public void setUserValues() {

        String user_id = pref.getString("id", "");
        String user_name = pref.getString("name", "");
        String user_password = pref.getString("password", "");
        String user_tele = pref.getString("tele", "");
        String user_addtime = pref.getString("addtime", "");
        String user_picture = pref.getString("user_pic", "");
        String device_id = pref.getString("device_id", "");
        String device_addtime = pref.getString("addtime", "");
        String device_status = pref.getString("status", "");
        ((DouYinApplication) getApplication()).setUser_id(user_id);
        ((DouYinApplication) getApplication()).setUser_name(user_name);
        ((DouYinApplication) getApplication()).setUser_password(user_password);
        ((DouYinApplication) getApplication()).setUser_tele(user_tele);
        ((DouYinApplication) getApplication()).setUser_addtime(user_addtime);
        ((DouYinApplication) getApplication()).setUser_picture(user_picture);
        ((DouYinApplication) getApplication()).setDevice_id(device_id);
        ((DouYinApplication) getApplication()).setDevice_addtime(device_addtime);
        ((DouYinApplication) getApplication()).setDevice_status(device_status);



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

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


    }
}
