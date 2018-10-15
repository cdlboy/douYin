package com.douyin.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

public class WangJi3Activity extends Activity implements View.OnClickListener{

    private EditText xinmima_ed;
    private Button xinmima_bt;
    private String last_cookie;
    private String last_name;
    private String new_mima;

    private TextView title;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_ji3);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("找回密码");
        xinmima_ed=(EditText)findViewById(R.id.yanzheng_xinmima_edit);
        xinmima_bt=(Button)findViewById(R.id.xinmima_yanzheng_button);
        last_cookie = getIntent().getStringExtra("cookie");
        last_name = getIntent().getStringExtra("name");
        xinmima_bt.setOnClickListener(this);
        ActivityCollector.addActivity(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xinmima_yanzheng_button:
                new_mima=xinmima_ed.getText().toString();
                if (null == new_mima || TextUtils.isEmpty(new_mima)) {
                    Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            String rsl = AppInterface.newPass(last_cookie,new_mima);
                            Log.d("data",rsl);
                            JSONObject json = new JSONObject(rsl);
                            String error = json.getString("error");
                            if (error.equals("0")) {
                                Looper.prepare();
                                final AlertDialog.Builder normalDialog =
                                        new AlertDialog.Builder(WangJi3Activity.this);
                                normalDialog.setTitle("修改密码成功");
                                normalDialog.setMessage("是否立即用此号登陆？");
                                normalDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            String name =last_name;
                                                            String password =new_mima;
                                                            Log.d("data",name);
                                                            Log.d("data",password);
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
                                                                Intent intent = new Intent(WangJi3Activity.this, HomeActivity.class);

                                                                startActivity(intent);


                                                            } else {
                                                                Looper.prepare();
                                                                Toast.makeText(WangJi3Activity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
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
                                        });
                                normalDialog.setNegativeButton("关闭",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //...To-do
                                            }
                                        });
                                // 显示
                                normalDialog.show();
                                Looper.loop();
                            }
                            else{
                                Toast.makeText(WangJi3Activity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
                break;
            default:
                break;
        }

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
