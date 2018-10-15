package com.douyin.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ZhuCeActivity extends Activity implements View.OnClickListener {

    private EditText name_ed;
    private EditText password_ed;
    private EditText phone_ed;
    private EditText shebei_ed;
    private EditText yz_ed;
    private ImageView yz_img;

    private String codeStr;
    private com.example.codedemo.CodeUtils codeUtils;

    private String name;
    private String password;
    private String phone;
    private String shebei;
    private String yanzhengma;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Button zc_button;
    private TextView denglu;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("注册");
        name_ed = (EditText) findViewById(R.id.zhuce_name_edit);
        password_ed = (EditText) findViewById(R.id.zhuce_passord_edit);
        phone_ed = (EditText) findViewById(R.id.zhuce_phone_edit);
        shebei_ed = (EditText) findViewById(R.id.zhuce_shebei_edit);
        yz_ed = (EditText) findViewById(R.id.zhuce_yanzheng_edit);
        yz_img = (ImageView) findViewById(R.id.zhuce_yanzheng_img);
        zc_button = (Button) findViewById(R.id.zhuce_button);
        zc_button.setOnClickListener(this);


        yz_img.setOnClickListener(this);

        denglu=(TextView) findViewById(R.id.zhuce_denglu_txt);
        denglu.setOnClickListener(this);

        codeUtils = com.example.codedemo.CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        yz_img.setImageBitmap(bitmap);
        ActivityCollector.addActivity(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhuce_denglu_txt:
                Intent intent = new Intent(ZhuCeActivity.this,MainActivity.class);

                startActivity(intent);
            case R.id.zhuce_yanzheng_img:
                codeUtils = com.example.codedemo.CodeUtils.getInstance();
                Bitmap bitmap = codeUtils.createBitmap();
                yz_img.setImageBitmap(bitmap);
                break;
            case R.id.zhuce_button:
                name = name_ed.getText().toString();
                password = password_ed.getText().toString();
                phone = phone_ed.getText().toString();
                shebei = shebei_ed.getText().toString();
                yanzhengma = yz_ed.getText().toString();


                if (null == name || TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null == password || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null == phone || TextUtils.isEmpty(phone)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null == shebei || TextUtils.isEmpty(shebei)) {
                    Toast.makeText(this, "请输入设备号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null == yanzhengma || TextUtils.isEmpty(yanzhengma)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            String rsl = AppInterface.register(name, password, phone, shebei);
                            JSONObject json = new JSONObject(rsl);
                            String error = json.getString("error");
                            if (!error.equals("0")) {

                                Looper.prepare();
                                final AlertDialog.Builder normalDialog =
                                        new AlertDialog.Builder(ZhuCeActivity.this);
                                normalDialog.setTitle("注册成功");
                                normalDialog.setMessage("是否立即用此号登陆？");
                                normalDialog.setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            String name1 = name_ed.getText().toString();
                                                            String password1 = password_ed.getText().toString();
                                                            Log.d("data",name1);
                                                            String rsl = AppInterface.login(name1, password1);
                                                            JSONObject json = new JSONObject(rsl);

                                                            String error = json.getString("error");

                                                            if (error.equals("0")) {
                                                                String cookie = json.getString("cookie");
                                                                editor = getSharedPreferences("data", 0).edit();
                                                                editor.putString("name", name1);
                                                                editor.putString("password", password1);
                                                                editor.commit();
                                                                setUserValues(cookie);
                                                                Intent intent = new Intent(ZhuCeActivity.this, HomeActivity.class);

                                                                startActivity(intent);


                                                            } else {
                                                                Looper.prepare();
                                                                Toast.makeText(ZhuCeActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
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
