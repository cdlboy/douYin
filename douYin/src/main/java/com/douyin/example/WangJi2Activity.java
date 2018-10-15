package com.douyin.example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WangJi2Activity extends Activity implements View.OnClickListener {


    private String last_phone;
    private String last_cookie;
    private String last_name;
    private EditText sj_ed;
    private EditText yz_ed;
    private Button yz_button;
    private Button hq_button;
    private String phone_text;
    private String yanzheng_text;

    private TextView title;
    private TimeCount time;

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            hq_button.setBackgroundColor(Color.parseColor("#B6B6D8"));
            hq_button.setClickable(false);
            hq_button.setText("("+millisUntilFinished / 1000 +") 秒后重新发送");
        }

        @Override
        public void onFinish() {
            hq_button.setText("重新获取验证码");
            hq_button.setClickable(true);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wang_ji2);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("找回密码");
        last_phone = getIntent().getStringExtra("phone");
        last_cookie = getIntent().getStringExtra("cookie");
        last_name = getIntent().getStringExtra("name");
        sj_ed = (EditText) findViewById(R.id.yanzheng_shouji_edit);
        sj_ed.setText(last_phone);
        yz_ed = (EditText) findViewById(R.id.shouji_yanzheng_edit);
        yz_button = (Button) findViewById(R.id.shouji_yanzheng_button);
        yz_button.setOnClickListener(this);
        hq_button = (Button) findViewById(R.id.huoqu_yanzheng);
        hq_button.setOnClickListener(this);
        time = new TimeCount(60000, 1000);

        ActivityCollector.addActivity(this);
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huoqu_yanzheng:

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            String rsl = AppInterface.getPhoneVerify(last_cookie);
                            Log.d("data",rsl);
                            JSONObject json = new JSONObject(rsl);
                            String error = json.getString("error");
                            if (!error.equals("0")) {

                                Looper.prepare();
                                Toast.makeText(WangJi2Activity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                                Looper.loop();


                            }
                            else{
                                time.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
                break;
            case R.id.shouji_yanzheng_button:
                phone_text = sj_ed.getText().toString();
                yanzheng_text = yz_ed.getText().toString();
                if (null ==  phone_text || TextUtils.isEmpty( phone_text)) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (null == yanzheng_text || TextUtils.isEmpty(yanzheng_text)) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        String rsl = AppInterface.checkTeleCode(last_cookie,yanzheng_text);
                        JSONObject json = new JSONObject(rsl);
                        String error = json.getString("error");
                        if (error.equals("0")) {

                            Intent intent = new Intent(WangJi2Activity.this, WangJi3Activity.class);
                            intent.putExtra("cookie",last_cookie);
                            intent.putExtra("cookie",last_name);
                            startActivity(intent);


                        } else {
                            Looper.prepare();
                            Toast.makeText(WangJi2Activity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
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

}
