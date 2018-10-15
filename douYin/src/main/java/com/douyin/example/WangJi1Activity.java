package com.douyin.example;

import android.app.Activity;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WangJi1Activity extends Activity implements View.OnClickListener{

    private EditText name_ed;
    private EditText yangzheng_ed;
    private Button yg_button;
    private ImageView yz_img;

    private TextView title;

    private String user_name;
    private String codeStr;
    private com.example.codedemo.CodeUtils codeUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_ji1);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("找回密码");
        name_ed=(EditText)findViewById(R.id.yanzheng_name_edit);
        yangzheng_ed=(EditText)findViewById(R.id.yanzheng_edit);
        yz_img=(ImageView)findViewById(R.id.yanzheng_img);
        yg_button=(Button)findViewById(R.id.yanzheng_button);
        yg_button.setOnClickListener(this);
        yz_img.setOnClickListener(this);
        codeUtils =com.example.codedemo.CodeUtils.getInstance();
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
            case R.id.yanzheng_button:

                codeStr = yangzheng_ed.getText().toString().trim();
                if (null == codeStr || TextUtils.isEmpty(codeStr)) {
                    Toast.makeText(this, "请输入验证码", 0).show();
                    return;
                }
                String code = codeUtils.getCode();
                Log.e("code", code);
                if (code.equalsIgnoreCase(codeStr)) {
                    user_name=name_ed.getText().toString();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                    try {
                        String rsl =  AppInterface.checkUser(user_name);
                        Log.d("data",rsl);
                        JSONObject json = new JSONObject(rsl);
                        String error= json.getString("error");
                        String phone1 = json.getString("tele");
                        String cookie1= json.getString("cookie");
                        if (error.equals("0")) {

                            Intent intent = new Intent(WangJi1Activity.this,WangJi2Activity.class);
                            intent.putExtra("name",user_name);
                            intent.putExtra("cookie",cookie1);
                            intent.putExtra("phone",phone1);
                            startActivity(intent);


                        } else {
                            Looper.prepare();
                            Toast.makeText(WangJi1Activity.this, "用户名错误！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "验证码错误", 0).show();
                }
                break;

            case R.id.yanzheng_img:
                codeUtils =com.example.codedemo.CodeUtils.getInstance();
                Bitmap bitmap = codeUtils.createBitmap();
                yz_img.setImageBitmap(bitmap);
            default:
                break;
        }

    }
}
