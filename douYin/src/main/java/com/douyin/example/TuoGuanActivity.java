package com.douyin.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TuoGuanActivity extends Activity implements View.OnClickListener {

    private String status;

    private TextView title;

    private CheckBox dayang;
    private CheckBox lvshui;
    private CheckBox weishi;

    private EditText dayang_long_edview;
    private EditText dayang_time_edview;
    private EditText lvshui_long_edview;
    private EditText lvshui_time_edview;
    private EditText weishi_time_edview;


    private Spinner weishi_spinner;
    private String weishi_spinner_text;


    private Button button_quxiao;
    private Button button_queding;


    private String tuoguan_oxygen;
    private String tuoguan_oxygenSettime;
    private String tuoguan_oxygenTime;
    private String tuoguan_flash;
    private String tuoguan_flashSettime;
    private String tuoguan_flashTime;
    private String tuoguan_feed;
    private String tuoguan_feedSettime;
    private String tuoguan_feedCount;

    private ArrayAdapter<String> adapter;

    private LinearLayout spinner_img;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (tuoguan_oxygen.equals("1")) {
                        dayang.setChecked(true);
                        dayang_time_edview.setText(tuoguan_oxygenSettime);
                        dayang_long_edview.setText(tuoguan_oxygenTime);
                    }
                    if (tuoguan_flash.equals("1")) {
                        lvshui.setChecked(true);
                        lvshui_time_edview.setText(tuoguan_flashSettime);
                        lvshui_long_edview.setText(tuoguan_flashTime);
                    }
                    if (tuoguan_feed.equals("1")) {
                        weishi.setChecked(true);
                        weishi_time_edview.setText(tuoguan_feedSettime);
                        //feed 投喂量 -1-少量,0-中量,1-大量
                        if (tuoguan_feedCount.equals("1"))
                            weishi_spinner.setSelection(0, true);
                        else if (tuoguan_feedCount.equals("2"))
                            weishi_spinner.setSelection(1, true);
                        else weishi_spinner.setSelection(2, true);
                    }

                    break;
                case 6:
                    dayang.setChecked(false);
                    weishi.setChecked(false);
                    lvshui.setChecked(false);
                    break;
                default:
                    break;
            }
        }
    };

    public interface AsyncResponse {
        void onDataReceivedSuccess(String cd);

        void onDataReceivedFailed();
    }

    class GetStatus extends AsyncTask<Void, Void, String> {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        protected String doInBackground(Void... params) {

            try {
                String cookie = getCookie();

                String rsl = AppInterface.getLastTemp(cookie);
                JSONObject json = new JSONObject(rsl);
                status = json.getString("status");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return status;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                asyncResponse.onDataReceivedSuccess(result);// ????????????????е????
            } else {
                asyncResponse.onDataReceivedFailed();
            }
        }
    }

    public String getCookie() {
        String rsl;
        String cookie = null;
        try {
            rsl = AppInterface.login(((DouYinApplication) getApplication()).getUser_name(),
                    ((DouYinApplication) getApplication()).getUser_password());
            JSONObject json = new JSONObject(rsl);
            cookie = json.getString("cookie");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cookie;
    }

    public void gettgInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String cookie = getCookie();
                    String rsl = AppInterface.getTgInfo(cookie);
                    JSONObject json = new JSONObject(rsl);
                    Log.d("data", json.getJSONObject("oxygen").getString("isOxygen"));

                    tuoguan_oxygen = json.getJSONObject("oxygen").getString("tg_oxygen");
                    tuoguan_oxygenSettime = json.getJSONObject("oxygen").getString("tg_oxygensettime");
                    tuoguan_oxygenTime = json.getJSONObject("oxygen").getString("tg_oxygentime");
                    tuoguan_flash = json.getJSONObject("flash").getString("tg_flash");
                    tuoguan_flashSettime = json.getJSONObject("flash").getString("tg_flashsettime");
                    tuoguan_flashTime = json.getJSONObject("flash").getString("tg_flashtime");
                    tuoguan_feed = json.getJSONObject("feed").getString("tg_feed");
                    tuoguan_feedSettime = json.getJSONObject("feed").getString("tg_feedsettime");
                    tuoguan_feedCount = json.getJSONObject("feed").getString("tg_feedcount");

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Message message = new Message();// ???????????????????????handleMessage??????????????????????
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuoguan);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("托管设置");

        //初始化

        spinner_img = (LinearLayout) findViewById(R.id.spinner_tu);
        spinner_img.setOnClickListener(this);

        weishi_spinner = (Spinner) findViewById(R.id.weishi_tuoguan_liang);

        adapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, R.id.tv_spinner);

        adapter
                .setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        adapter.add("少量");
        adapter.add("中量");
        adapter.add("大量");


        weishi_spinner.setAdapter(adapter);


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weishi_liang_data, android.R.layout.simple_spinner_dropdown_item);
//        weishi_spinner.setAdapter(adapter);
        weishi_spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

        dayang = (CheckBox) findViewById(R.id.tuoguan_dayang);
        lvshui = (CheckBox) findViewById(R.id.tuoguan_lvshui);
        weishi = (CheckBox) findViewById(R.id.tuoguan_weishi);

        dayang_long_edview = (EditText) findViewById(R.id.tuoguan_dayang_long);
        dayang_time_edview = (EditText) findViewById(R.id.tuoguan_dayang_time);
        lvshui_long_edview = (EditText) findViewById(R.id.tuoguan_lvshui_long);
        lvshui_time_edview = (EditText) findViewById(R.id.tuoguan_lvshui_time);
        weishi_time_edview = (EditText) findViewById(R.id.tuoguan_weishi_time);

        button_queding = (Button) findViewById(R.id.button_tuoguan_queding);
        button_quxiao = (Button) findViewById(R.id.button_tuoguan_zhongzhi);


        button_queding.setOnClickListener(this);
        button_quxiao.setOnClickListener(this);

        ActivityCollector.addActivity(this);

        gettgInfo();

    }

    public class MyOnItemSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position).toString().equals("中量"))
                weishi_spinner_text = "2";
            else if (parent.getItemAtPosition(position).toString().equals("少量"))
                weishi_spinner_text = "1";
            else weishi_spinner_text = "3";

            Log.d("data", weishi_spinner_text + "222");
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.spinner_tu:
                weishi_spinner.performClick();
                break;

            case R.id.button_tuoguan_zhongzhi:
                if (tuoguan_oxygen.equals("0") && tuoguan_flash.equals("0") && tuoguan_feed.equals("0")) {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(TuoGuanActivity.this);
                    dialog1.setTitle("提示");
                    dialog1.setMessage("当前没有可关闭的托管！");
                    dialog1.setCancelable(true);
                    dialog1.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            GetStatus GetStatus = new GetStatus();

                            GetStatus.execute();
                            GetStatus.setOnAsyncResponse(new AsyncResponse() {
                                public void onDataReceivedSuccess(String result2) {
                                    status = result2;

                                    Log.d("data", status + "wendu");

                                    if (status.equals("0")) {

                                        // Looper.prepare();
                                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(TuoGuanActivity.this);
                                        dialog2.setTitle("提示");
                                        dialog2.setMessage("当前设备离线！");
                                        dialog2.setCancelable(true);
                                        dialog2.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).create().show();
                                        // Looper.loop();
                                    } else {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {


                                                String dayang_int = "0";
                                                String lvshui_int = "0";
                                                String weishi_int = "0";

                                                String dayang_long_text = dayang_long_edview.getText().toString();//时长
                                                String dayang_time_text = dayang_time_edview.getText().toString();//间隔
                                                String lvshui_long_text = lvshui_long_edview.getText().toString();
                                                String lvshui_time_text = lvshui_time_edview.getText().toString();
                                                String weishi_time_text = weishi_time_edview.getText().toString();


                                                String cookie = getCookie();

                                                Map<String, String> tuoguan_info_map = new HashMap<>();
                                                tuoguan_info_map.put("tg_oxygen", dayang_int);
                                                tuoguan_info_map.put("tg_oxygenSettime", dayang_time_text);
                                                tuoguan_info_map.put("tg_oxygenTime", dayang_long_text);
                                                tuoguan_info_map.put("tg_flash", lvshui_int);
                                                tuoguan_info_map.put("tg_flashSettime", lvshui_time_text);
                                                tuoguan_info_map.put("tg_flashTime", lvshui_long_text);
                                                tuoguan_info_map.put("tg_feed", weishi_int);
                                                tuoguan_info_map.put("tg_feedSettime", weishi_time_text);
                                                tuoguan_info_map.put("tg_feedCount", weishi_spinner_text);


                                                try {
                                                    Log.d("data", tuoguan_info_map.toString());
                                                    String result = AppInterface.setTg(cookie, tuoguan_info_map);
                                                    JSONObject result_json = new JSONObject(result);
                                                    Log.d("data", result);
                                                    String error = result_json.getString("error");
                                                    if (error.equals("0")) {

                                                        Looper.prepare();


                                                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(TuoGuanActivity.this);
                                                        dialog2.setTitle("提示");
                                                        dialog2.setMessage("全部取消成功！");
                                                        dialog2.setCancelable(true);
                                                        dialog2.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                            }
                                                        }).create().show();
                                                        Message message = new Message();// ???????????????????????handleMessage??????????????????????
                                                        message.what = 6;
                                                        handler.sendMessage(message);
                                                        Looper.loop();


                                                    } else {
                                                        Looper.prepare();
                                                        AlertDialog.Builder warm_dialog = new AlertDialog.Builder(TuoGuanActivity.this);
                                                        warm_dialog.setMessage("取消失败！请检查设备");
                                                        warm_dialog.setCancelable(true);
                                                        warm_dialog
                                                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                    }
                                                                }).create().show();
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

                                public void onDataReceivedFailed() {

                                }

                            });

                        }
                    }).start();
                }
                break;

            case R.id.button_tuoguan_queding:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        GetStatus GetStatus = new GetStatus();

                        GetStatus.execute();
                        GetStatus.setOnAsyncResponse(new AsyncResponse() {
                            public void onDataReceivedSuccess(String result2) {
                                status = result2;

                                Log.d("data", status + "wendu");

                                if (status.equals("0")) {

                                    // Looper.prepare();
                                    AlertDialog.Builder tuoguan_dialog = new AlertDialog.Builder(TuoGuanActivity.this);
                                    tuoguan_dialog.setTitle("提示");
                                    tuoguan_dialog.setMessage("当前设备离线！");
                                    tuoguan_dialog.setCancelable(true);
                                    tuoguan_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create().show();
                                    // Looper.loop();
                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {


                                            String dayang_int = "0";
                                            String lvshui_int = "0";
                                            String weishi_int = "0";

                                            String dayang_long_text = dayang_long_edview.getText().toString();//时长
                                            String dayang_time_text = dayang_time_edview.getText().toString();//间隔
                                            String lvshui_long_text = lvshui_long_edview.getText().toString();
                                            String lvshui_time_text = lvshui_time_edview.getText().toString();
                                            String weishi_time_text = weishi_time_edview.getText().toString();


                                            if (dayang.isChecked())
                                                dayang_int = "1";
                                            if (lvshui.isChecked())
                                                lvshui_int = "1";
                                            if (weishi.isChecked())
                                                weishi_int = "1";

                                            String cookie = getCookie();

                                            Map<String, String> tuoguan_info_map = new HashMap<>();
                                            tuoguan_info_map.put("tg_oxygen", dayang_int);
                                            tuoguan_info_map.put("tg_oxygenSettime", dayang_time_text);
                                            tuoguan_info_map.put("tg_oxygenTime", dayang_long_text);
                                            tuoguan_info_map.put("tg_flash", lvshui_int);
                                            tuoguan_info_map.put("tg_flashSettime", lvshui_time_text);
                                            tuoguan_info_map.put("tg_flashTime", lvshui_long_text);
                                            tuoguan_info_map.put("tg_feed", weishi_int);
                                            tuoguan_info_map.put("tg_feedSettime", weishi_time_text);
                                            tuoguan_info_map.put("tg_feedCount", weishi_spinner_text);


                                            try {
                                                Log.d("data", tuoguan_info_map.toString());
                                                String result = AppInterface.setTg(cookie, tuoguan_info_map);
                                                JSONObject result_json = new JSONObject(result);
                                                Log.d("data", result);
                                                String error = result_json.getString("error");
                                                if (error.equals("0")) {

                                                    Looper.prepare();
                                                    gettgInfo();
                                                    Toast.makeText(TuoGuanActivity.this, "托管设定成功！", Toast.LENGTH_LONG).show();
                                                    Looper.loop();

                                                } else {
                                                    Looper.prepare();
                                                    AlertDialog.Builder warm_dialog = new AlertDialog.Builder(TuoGuanActivity.this);
                                                    warm_dialog.setMessage("托管设定失败！请检查设备");
                                                    warm_dialog.setCancelable(true);
                                                    warm_dialog
                                                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            }).create().show();
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

                            public void onDataReceivedFailed() {

                            }

                        });

                    }
                }).start();
                break;

            default:
                break;

        }
    }


    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
