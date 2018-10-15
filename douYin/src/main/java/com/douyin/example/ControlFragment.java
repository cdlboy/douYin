package com.douyin.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.douyin.AppInterface.AppInterface;
import com.douyin.R;
import com.douyin.model.DouYinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlFragment extends Fragment implements OnClickListener {

    private String status_info;
    private ImageView warm_img;

    private EditText edittext1;
    private EditText edittext2;
    private TextView textview1;

    private String shangyu_time;
    private String oxygen;
    private String lvshui;
    private String status;
    private String weather;
    private Builder warm_dialog;

    private int dialog_flag;

    public static final int UPDATE_WENDU = 1;
    public static final int UPDATE_YANQI = 2;
    public static final int UPDATE_LVSHUI = 3;
    public static final int UPDATE_WEISHI = 4;
    public static final int UPDATE_YANQI_SHENGYU = 5;
    public static final int UPDATE_LVSHUI_SHENGYU = 6;
    public static final int UPDATE_WENDU_GUANBI = 7;


    private Button wendu_queding_button;
    private Button wendu_quxiao_button;
    private Button yanqi_queding_button;
    private Button lvshui_queding_button;
    private Button weishi_queding_button;


    private RadioButton weishi_button;
    private RadioButton weishi_button_xiao;
    private RadioButton weishi_button_zhong;
    private RadioButton weishi_button_da;

    private Button wd_button;
    private Button yq_button;
    private Button ws_button;
    private Button ls_button;
    private Button tg_button;

    private String tuoguan_oxygen_info;
    private String tuoguan_flash_info;
    private String tuoguan_feed_info;

    private TextView info_textview;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_WENDU:
                    edittext1.setText(((DouYinApplication) getActivity().getApplication()).getLast_wendu_min());
                    edittext2.setText(((DouYinApplication) getActivity().getApplication()).getLast_wendu_max());
                    break;

                case UPDATE_WENDU_GUANBI:
                    edittext1.setText(((DouYinApplication) getActivity().getApplication()).getLast_wendu_min());
                    edittext2.setText(((DouYinApplication) getActivity().getApplication()).getLast_wendu_max());
                    break;
                case UPDATE_YANQI:
                    textview1.setText(((DouYinApplication) getActivity().getApplication()).getLast_dayan());
                    edittext2.setText(((DouYinApplication) getActivity().getApplication()).getDanyan_time());
                    break;
                case UPDATE_LVSHUI:
                    textview1.setText(((DouYinApplication) getActivity().getApplication()).getLast_lvshui());
                    edittext2.setText(((DouYinApplication) getActivity().getApplication()).getLvshui_time());
                    break;
                case UPDATE_WEISHI:
                    textview1.setText(((DouYinApplication) getActivity().getApplication()).getLast_weishi());
                    weishi_button.setChecked(true);
                    break;
                case UPDATE_YANQI_SHENGYU:
                    textview1.setText(shangyu_time);
                    break;
                case UPDATE_LVSHUI_SHENGYU:
                    textview1.setText(shangyu_time);
                    break;
                case 666:
                    if(status_info.equals("1")){
                       info_textview.setText("设备状态：离线");
                   }
                   else if(status_info.equals("2")){
                       info_textview.setText("设备状态：在线");
                   }



                    if (tuoguan_oxygen_info.equals("1"))
                        yq_button.setText("已托管");
                    else yq_button.setText("设定");

                    if (tuoguan_flash_info.equals("1"))
                        ls_button.setText("已托管");
                    else ls_button.setText("设定");

                    if (tuoguan_feed_info.equals("1"))
                        ws_button.setText("已托管");
                    else ws_button.setText("设定");

                    break;


                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.control_fragment, null);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wd_button = (Button) getActivity().findViewById(R.id.wendu_button);
        yq_button = (Button) getActivity().findViewById(R.id.yanqi_button);
        ws_button = (Button) getActivity().findViewById(R.id.weishi_button);
        ls_button = (Button) getActivity().findViewById(R.id.lvshui_button);
        tg_button = (Button) getActivity().findViewById(R.id.tuoguan_button);
        warm_img=(ImageView)getActivity().findViewById(R.id.warm);
        info_textview = (TextView) getActivity().findViewById(R.id.info);
        wd_button.setOnClickListener(this);
        yq_button.setOnClickListener(this);
        ws_button.setOnClickListener(this);
        ls_button.setOnClickListener(this);
        tg_button.setOnClickListener(this);

        dialog_flag = 0;
        if (!isNetworkAvailable(getActivity())){
            wd_button.setBackgroundResource(R.drawable.un_btn);
            wd_button.setTextColor(this.getResources().getColor(R.color.colorUnBtn));
            wd_button.setEnabled(false);
            ws_button.setBackgroundResource(R.drawable.un_btn);
            ws_button.setTextColor(this.getResources().getColor(R.color.colorUnBtn));
            ws_button.setEnabled(false);
            ls_button.setBackgroundResource(R.drawable.un_btn);
            ls_button.setTextColor(this.getResources().getColor(R.color.colorUnBtn));
            ls_button.setEnabled(false);
            tg_button.setBackgroundResource(R.drawable.un_btn);
            tg_button.setTextColor(this.getResources().getColor(R.color.colorUnBtn));
            tg_button.setEnabled(false);
            yq_button.setBackgroundResource(R.drawable.un_btn);
            yq_button.setTextColor(this.getResources().getColor(R.color.colorUnBtn));
            yq_button.setEnabled(false);
            info_textview.setText("本地模式");
            warm_img.setVisibility(View.VISIBLE);
            warm_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "当前没有连接网络！", Toast.LENGTH_SHORT).show();
                }
            });
        }


        GetTg Gettg = new GetTg();

        Gettg.execute();
    }

    @Override
    public void onResume() {//重新加载时
        super.onResume();
        GetTg Gettg = new GetTg();
        Gettg.execute();

    }

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    class GetTg extends AsyncTask<Void, Void, Boolean> {

//        public AsyncResponse asyncResponse;
//
//        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
//            this.asyncResponse = asyncResponse;
//        }

        protected Boolean doInBackground(Void... params) {

            try {


                String cookie = getCookie();
                String rsl = AppInterface.getTgInfo(cookie);

                JSONObject json = new JSONObject(rsl);
                tuoguan_oxygen_info = json.getJSONObject("oxygen").getString("tg_oxygen");
                tuoguan_flash_info = json.getJSONObject("flash").getString("tg_flash");
                tuoguan_feed_info = json.getJSONObject("feed").getString("tg_feed");

                Log.d("data","1");


                    String rsl2 = AppInterface.getLastTemp(cookie);
                    JSONObject json2 = new JSONObject(rsl2);
                    String status2 = json2.getString("status");
                    Log.d("data", "data2" + status2);
                    if (status2.equals("0"))
                        status_info = "1";
                    else if (status2.equals("1"))
                        status_info = "2";


                onProgressUpdate();




            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }


        protected void onProgressUpdate() {


            Message message = new Message();// ???????????????????????handleMessage??????????????????????
            message.what = 666;
            handler.sendMessage(message);


        }

    }

    public String shengyuTime(String last_time, String control_time) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date last_date = sdf.parse(last_time);
        Calendar last_calendar = Calendar.getInstance();
        last_calendar.setTime(last_date);// ?????????ε????

        int control_time_int = Integer.valueOf(control_time.toString()).intValue();// ?????????

        last_calendar.add(Calendar.MINUTE, control_time_int);
        // ?????????
        Date date2 = new Date(System.currentTimeMillis());
        Calendar now_calendar = Calendar.getInstance();
        now_calendar.setTime(date2);

        if (last_calendar.before(now_calendar)) {
            return "";
        } else {
            long l = last_calendar.getTimeInMillis() - now_calendar.getTimeInMillis();
            int minutes = new Long(l / (1000 * 60)).intValue();
            return "" + minutes;
        }

    }

    public interface AsyncResponse {
        void onDataReceivedSuccess(String cd);

        void onDataReceivedFailed();
    }


    class GetWeahther extends AsyncTask<Void, Void, String> {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        protected String doInBackground(Void... params) {

            String cookie = getCookie();
            String oxygen = null;
            String rsl;
            try {
                rsl = AppInterface.getActionInfo(cookie);
                JSONObject json = new JSONObject(rsl);
                weather = json.getString("weather");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return weather;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                asyncResponse.onDataReceivedSuccess(result);// ????????????????е????
            } else {
                asyncResponse.onDataReceivedFailed();
            }
        }
    }

    class GetOxygen extends AsyncTask<Void, Void, String> {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        protected String doInBackground(Void... params) {

            String cookie = getCookie();
            String oxygen = null;
            String rsl;
            try {
                rsl = AppInterface.getActionInfo(cookie);
                JSONObject json = new JSONObject(rsl);
                oxygen = json.getString("oxygen");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return oxygen;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                asyncResponse.onDataReceivedSuccess(result);// ????????????????е????
            } else {
                asyncResponse.onDataReceivedFailed();
            }
        }
    }

    class GetLvshui extends AsyncTask<Void, Void, String> {

        public AsyncResponse asyncResponse;

        public void setOnAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        protected String doInBackground(Void... params) {

            String cookie = getCookie();
            String lvshui = null;
            String rsl;
            try {
                rsl = AppInterface.getActionInfo(cookie);
                JSONObject json = new JSONObject(rsl);
                lvshui = json.getString("flashwater");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return lvshui;
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                asyncResponse.onDataReceivedSuccess(result);// ????????????????е????
            } else {
                asyncResponse.onDataReceivedFailed();
            }
        }
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

                status_info = status;

                Log.d("data2", "data2+" + status_info);
                Message message = new Message();
                message.what = 777;
                handler.sendMessage(message);
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

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public String getCookie() {
        String rsl;
        String cookie = null;
        try {
            rsl = AppInterface.login(((DouYinApplication) getActivity().getApplication()).getUser_name(),
                    ((DouYinApplication) getActivity().getApplication()).getUser_password());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tuoguan_button:
                Intent intent = new Intent(getActivity(), TuoGuanActivity.class);

                startActivity(intent);
                break;
            case R.id.wendu_button:

                if (dialog_flag == 0) {


                    GetWeahther GetWea = new GetWeahther();

                    GetWea.execute();
                    GetWea.setOnAsyncResponse(new AsyncResponse() {
                        // ????????????????AsyncTask??onPostExecute???????????
                        @Override
                        public void onDataReceivedSuccess(String result) {
                            weather = result;

                            if (weather.equals("0")) {
                                dialog_flag = 1;

                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                final View layout = inflater.inflate(R.layout.wendu_tanchuang, null);

                                Builder builder = new Builder(getActivity());
                                builder.setCancelable(false); // ???e?????????????????,false?????????
                                final AlertDialog dialog = builder.create(); // ?????????
                                dialog.setView(layout, 0, 0, 0, 0);
                                dialog.show();

                                TextView title = (TextView) layout.findViewById(R.id.title_tanchaung_title);
                                title.setText("设定温度范围(℃)");
                                RelativeLayout back = (RelativeLayout) layout.findViewById(R.id.title_tanchaung_back);
                                back.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();
                                        dialog_flag = 0;
                                    }
                                });

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String cookie = getCookie();
                                            String rsl = AppInterface.getActionInfo(cookie);
                                            Log.d("data", rsl);
                                            JSONObject json = new JSONObject(rsl);
                                            ((DouYinApplication) getActivity().getApplication())
                                                    .setLast_wendu_min(json.getString("minweather"));
                                            ((DouYinApplication) getActivity().getApplication())
                                                    .setLast_wendu_max(json.getString("maxweather"));
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        edittext1 = (EditText) layout.findViewById(R.id.wendu_zuixiao);
                                        edittext2 = (EditText) layout.findViewById(R.id.wendu_zuida);
                                        Message message = new Message();// ???????????????????????handleMessage??????????????????????
                                        message.what = UPDATE_WENDU;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                                wendu_queding_button = (Button) layout.findViewById(R.id.button_wendu_queding);
                                wendu_queding_button.setOnClickListener(new OnClickListener() {

                                    public void onClick(View v) {
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
                                                            // Looper.loop();
                                                            Builder wendu_dialog = new Builder(getActivity());
                                                            wendu_dialog.setTitle("提示");
                                                            wendu_dialog.setMessage("当前设备离线！");
                                                            wendu_dialog.setCancelable(true);
                                                            wendu_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            }).create().show();


                                                        } else {
                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    edittext1 = (EditText) layout.findViewById(R.id.wendu_zuixiao);
                                                                    edittext2 = (EditText) layout.findViewById(R.id.wendu_zuida);

                                                                    if (TextUtils.isEmpty(edittext1.getText()) || TextUtils.isEmpty(edittext2.getText())) {
                                                                        Looper.prepare();
                                                                        warm_dialog = new Builder(getActivity());
                                                                        warm_dialog.setTitle("提示");
                                                                        warm_dialog.setMessage("输入不能为空！");
                                                                        warm_dialog.setCancelable(true);
                                                                        warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create().show();
                                                                        Looper.loop();

                                                                    }
                                                                    if (!isNumeric(edittext1.getText().toString()) || !isNumeric(edittext2.getText().toString())) {
                                                                        Looper.prepare();
                                                                        warm_dialog = new Builder(getActivity());
                                                                        warm_dialog.setTitle("提示");
                                                                        warm_dialog.setMessage("输入的应该为数字！");
                                                                        warm_dialog.setCancelable(true);
                                                                        warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create().show();
                                                                        Looper.loop();

                                                                    }

                                                                    final int min_wendu = Integer.valueOf(edittext1.getText().toString())
                                                                            .intValue();
                                                                    final int max_wendu = Integer.valueOf(edittext2.getText().toString())
                                                                            .intValue();

                                                                    if (min_wendu >= max_wendu) {
                                                                        Looper.prepare();
                                                                        warm_dialog = new Builder(getActivity());
                                                                        warm_dialog.setTitle("提示");
                                                                        warm_dialog.setMessage("最大值应该大于最小值！");
                                                                        warm_dialog.setCancelable(true);
                                                                        warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create().show();
                                                                        Looper.loop();
                                                                    }

                                                                    String cookie = getCookie();

                                                                    try {
                                                                        String result = AppInterface.setWeather(1, min_wendu, max_wendu, cookie);
                                                                        Log.d("data", result);
                                                                        JSONObject result_json = new JSONObject(result);
                                                                        String error = result_json.getString("error");
                                                                        if (error.equals("0")) {

                                                                            Looper.prepare();
                                                                            dialog.dismiss();
                                                                            dialog_flag = 0;
                                                                            Toast.makeText(getActivity(), "设定温度成功！", Toast.LENGTH_LONG).show();
                                                                            Looper.loop();

                                                                        } else {
                                                                            Looper.prepare();
                                                                            Builder wendu_dialog = new Builder(
                                                                                    getActivity());
                                                                            wendu_dialog.setMessage("设定温度失败！请检查设备");
                                                                            wendu_dialog.setCancelable(true);
                                                                            wendu_dialog
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

                                                    @Override
                                                    public void onDataReceivedFailed() {

                                                    }

                                                });

                                            }
                                        }).start();

                                    }
                                });
                            } else {


                                dialog_flag = 1;

                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                final View layout1 = inflater.inflate(R.layout.wendu_guanbi_tanchaung, null);

                                Builder builder = new Builder(getActivity());
                                builder.setCancelable(false); // ???e?????????????????,false?????????
                                final AlertDialog dialog1 = builder.create(); // ?????????
                                dialog1.setView(layout1, 0, 0, 0, 0);
                                dialog1.show();

                                TextView title = (TextView) layout1.findViewById(R.id.title_tanchaung_title);
                                title.setText("设定温度范围(℃)");
                                RelativeLayout back = (RelativeLayout) layout1.findViewById(R.id.title_tanchaung_back);
                                back.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog1.dismiss();
                                        dialog_flag = 0;
                                    }
                                });

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String cookie = getCookie();
                                            String rsl = AppInterface.getActionInfo(cookie);
                                            Log.d("data", rsl);
                                            JSONObject json = new JSONObject(rsl);
                                            ((DouYinApplication) getActivity().getApplication())
                                                    .setLast_wendu_min(json.getString("minweather"));
                                            ((DouYinApplication) getActivity().getApplication())
                                                    .setLast_wendu_max(json.getString("maxweather"));
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        edittext1 = (EditText) layout1.findViewById(R.id.guanbi_wendu_zuixiao);
                                        edittext2 = (EditText) layout1.findViewById(R.id.guanbi_wendu_zuida);

                                        Message message = new Message();// ???????????????????????handleMessage??????????????????????
                                        message.what = UPDATE_WENDU_GUANBI;
                                        handler.sendMessage(message);
                                    }
                                }).start();
                                wendu_queding_button = (Button) layout1.findViewById(R.id.button_yanqi_guanbi_zhongzhi);
                                wendu_queding_button.setOnClickListener(new OnClickListener() {

                                    public void onClick(View v) {


                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                edittext1 = (EditText) layout1.findViewById(R.id.guanbi_wendu_zuixiao);
                                                edittext2 = (EditText) layout1.findViewById(R.id.guanbi_wendu_zuida);
                                                final int min_wendu = Integer.valueOf(edittext1.getText().toString())
                                                        .intValue();
                                                final int max_wendu = Integer.valueOf(edittext2.getText().toString())
                                                        .intValue();
                                                String cookie = getCookie();

                                                try {
                                                    String result = AppInterface.setWeather(0, min_wendu, max_wendu, cookie);
                                                    Log.d("data", result);
                                                    JSONObject result_json = new JSONObject(result);
                                                    String error = result_json.getString("error");
                                                    if (error.equals("0")) {

                                                        Looper.prepare();
                                                        dialog1.dismiss();
                                                        dialog_flag = 0;
                                                        Toast.makeText(getActivity(), "取消温度成功！", Toast.LENGTH_LONG).show();
                                                        Looper.loop();

                                                    } else {
                                                        Looper.prepare();
                                                        Builder wendu_dialog = new Builder(
                                                                getActivity());
                                                        wendu_dialog.setMessage("取消温度失败！请检查设备");
                                                        wendu_dialog.setCancelable(true);
                                                        wendu_dialog
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
                                });


                                wendu_quxiao_button = (Button) layout1.findViewById(R.id.button_yanqi_guanbi_queding);
                                wendu_quxiao_button.setOnClickListener(new OnClickListener() {

                                    public void onClick(View v) {

                                        dialog1.dismiss();
                                        dialog_flag = 0;

                                    }
                                });

                            }

                        }

                        @Override
                        public void onDataReceivedFailed() {

                        }

                    });
                }

                break;
            case R.id.yanqi_button:
                if (tuoguan_oxygen_info.equals("1")) {
                    Intent intent1 = new Intent(getActivity(), TuoGuanActivity.class);

                    startActivity(intent1);
                } else {


                    if (dialog_flag == 0) {

                        dialog_flag = 1;
                        GetOxygen GetOxygen = new GetOxygen();

                        GetOxygen.execute();
                        GetOxygen.setOnAsyncResponse(new AsyncResponse() {
                            // ????????????????AsyncTask??onPostExecute???????????
                            @Override
                            public void onDataReceivedSuccess(String result) {
                                oxygen = result;

                                Log.d("data", oxygen);

                                if (oxygen.equals("1")) {
                                    LayoutInflater guanbi_inflater2 = getActivity().getLayoutInflater();
                                    final View guanbi_layout2 = guanbi_inflater2.inflate(R.layout.yanqi_guanbi_tanchuang, null);

                                    Builder guanbi_builder2 = new Builder(getActivity());
                                    guanbi_builder2.setCancelable(false);
                                    guanbi_builder2.setView(guanbi_layout2);
                                    final AlertDialog guanbi_dialog2 = guanbi_builder2.create();
                                    // ?????????
                                    guanbi_dialog2.show();
                                    //
                                    // WindowManager.LayoutParams guanbi_params2 =
                                    // guanbi_dialog2.getWindow().getAttributes();
                                    // guanbi_params2.width = 850;
                                    // guanbi_params2.height = 720;
                                    // guanbi_dialog2.getWindow().setAttributes(guanbi_params2);
                                    TextView title2 = (TextView) guanbi_layout2.findViewById(R.id.title_tanchaung_title);
                                    title2.setText("设定打氧时长(分钟)");
                                    RelativeLayout back = (RelativeLayout) guanbi_layout2.findViewById(R.id.title_tanchaung_back);
                                    back.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            guanbi_dialog2.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    Button queding = (Button) guanbi_layout2.findViewById(R.id.button_yanqi_queding);
                                    queding.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            guanbi_dialog2.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    Button zhongzhi = (Button) guanbi_layout2.findViewById(R.id.button_yanqi_zhongzhi);
                                    zhongzhi.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String cookie = getCookie();
                                                        String result = AppInterface.oxygenOn(0, 0, cookie);
                                                        JSONObject result_json = new JSONObject(result);
                                                        String error = result_json.getString("error");
                                                        Log.d("data", error + "cdfg");
                                                        if (error.equals("0")) {
                                                            Log.d("data", error + "cdfg");
                                                            Looper.prepare();
                                                            guanbi_dialog2.dismiss();
                                                            dialog_flag = 0;
                                                            Toast.makeText(getActivity(), "中止成功！", Toast.LENGTH_LONG).show();
                                                            Looper.loop();

                                                        } else {
                                                            Looper.prepare();
                                                            Builder dialog = new Builder(getActivity());
                                                            dialog.setMessage("中止失败！请检查设备");
                                                            dialog.setCancelable(true);
                                                            dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
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

                                            guanbi_dialog2.dismiss();
                                        }
                                    });

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String cookie = getCookie();
                                                String rsl = AppInterface.getActionInfo(cookie);
                                                JSONObject json = new JSONObject(rsl);
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLast_dayan(json.getString("oxygen_time"));
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setDanyan_time(json.getString("oxygentime"));
                                                shangyu_time = shengyuTime(
                                                        ((DouYinApplication) getActivity().getApplication()).getLast_dayan(),
                                                        ((DouYinApplication) getActivity().getApplication()).getDanyan_time());
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            textview1 = (TextView) guanbi_layout2.findViewById(R.id.dayan_chengyu_text);
                                            Message message = new Message();//
                                            message.what = UPDATE_YANQI_SHENGYU;
                                            handler.sendMessage(message);
                                        }
                                    }).start();

                                } else {
                                    Log.d("data", oxygen);
                                    // Looper.prepare();

                                    LayoutInflater inflater2 = getActivity().getLayoutInflater();
                                    final View layout2 = inflater2.inflate(R.layout.yanqi_tanchuang, null);

                                    Builder builder2 = new Builder(getActivity());
                                    builder2.setView(layout2);
                                    builder2.setCancelable(false); // ???e?????????????????,false?????????
                                    final AlertDialog dialog2 = builder2.create(); // ?????????
                                    dialog2.show();
                                    // WindowManager.LayoutParams params2 =
                                    // dialog2.getWindow().getAttributes();
                                    // params2.width = 850;
                                    // params2.height = 720;
                                    // dialog2.getWindow().setAttributes(params2);
                                    TextView title2 = (TextView) layout2.findViewById(R.id.title_tanchaung_title);
                                    title2.setText("设定打氧时长(分钟)");
                                    RelativeLayout back = (RelativeLayout) layout2.findViewById(R.id.title_tanchaung_back);
                                    back.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });
                                    // Looper.loop();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String cookie = getCookie();
                                                String rsl = AppInterface.getActionInfo(cookie);
                                                JSONObject json = new JSONObject(rsl);
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLast_dayan(json.getString("oxygen_time"));
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setDanyan_time(json.getString("oxygentime"));
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            textview1 = (TextView) layout2.findViewById(R.id.last_yanqi);
                                            edittext2 = (EditText) layout2.findViewById(R.id.now_yanqi);
                                            Message message = new Message();// ???????????????????????handleMessage??????????????????????
                                            message.what = UPDATE_YANQI;
                                            handler.sendMessage(message);
                                        }
                                    }).start();

                                    yanqi_queding_button = (Button) layout2.findViewById(R.id.button_yanqi_queding);
                                    yanqi_queding_button.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    GetStatus GetStatus = new GetStatus();

                                                    GetStatus.execute();
                                                    GetStatus.setOnAsyncResponse(new AsyncResponse() {
                                                        public void onDataReceivedSuccess(String result2) {
                                                            status = result2;
                                                            Log.d("data", status + "qwer");

                                                            if (status.equals("0")) {

                                                                // Looper.prepare();
                                                                Builder dayang_dialog = new Builder(
                                                                        getActivity());
                                                                dayang_dialog.setTitle("提示");
                                                                dayang_dialog.setMessage("当前设备离线！");
                                                                dayang_dialog.setCancelable(true);
                                                                dayang_dialog.setNegativeButton("确定",
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create().show();
                                                                // Looper.loop();
                                                            } else {

                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {


                                                                        if (TextUtils.isEmpty(edittext2.getText())) {
                                                                            Looper.prepare();
                                                                            warm_dialog = new Builder(getActivity());
                                                                            warm_dialog.setTitle("提示");
                                                                            warm_dialog.setMessage("输入不能为空！");
                                                                            warm_dialog.setCancelable(true);
                                                                            warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                }
                                                                            }).create().show();
                                                                            Looper.loop();

                                                                        }
                                                                        if (!isNumeric(edittext2.getText().toString())) {
                                                                            Looper.prepare();
                                                                            warm_dialog = new Builder(getActivity());
                                                                            warm_dialog.setTitle("提示");
                                                                            warm_dialog.setMessage("输入的应该为数字！");
                                                                            warm_dialog.setCancelable(true);
                                                                            warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                }
                                                                            }).create().show();
                                                                            Looper.loop();

                                                                        }


                                                                        final int dayan_time = Integer
                                                                                .valueOf(edittext2.getText().toString()).intValue();
                                                                        String cookie = getCookie();

                                                                        try {
                                                                            String result = AppInterface.oxygenOn(1, dayan_time,
                                                                                    cookie);
                                                                            JSONObject result_json = new JSONObject(result);
                                                                            String error = result_json.getString("error");
                                                                            Log.d("data", error + "cdfg");
                                                                            if (error.equals("0")) {
                                                                                Log.d("data", error + "cdfg");
                                                                                Looper.prepare();
                                                                                dialog2.dismiss();
                                                                                dialog_flag = 0;
                                                                                Toast.makeText(getActivity(), "打氧成功！",
                                                                                        Toast.LENGTH_LONG).show();
                                                                                Looper.loop();

                                                                            } else {
                                                                                Looper.prepare();
                                                                                Builder dayang_dialog = new Builder(
                                                                                        getActivity());
                                                                                dayang_dialog.setMessage("打氧失败！请检查设备");
                                                                                dayang_dialog.setCancelable(true);
                                                                                dayang_dialog.setNegativeButton("确定",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(
                                                                                                    DialogInterface dialog,
                                                                                                    int which) {
                                                                                            }
                                                                                        }).create().show();
                                                                                Looper.loop();

                                                                            }
                                                                        } catch (IOException e) {
                                                                            // TODO
                                                                            // Auto-generated
                                                                            // catch
                                                                            // block
                                                                            e.printStackTrace();
                                                                        } catch (JSONException e) {
                                                                            // TODO
                                                                            // Auto-generated
                                                                            // catch
                                                                            // block
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }).start();
                                                            }
                                                        }

                                                        @Override
                                                        public void onDataReceivedFailed() {

                                                        }

                                                    });
                                                }
                                            }).start();

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onDataReceivedFailed() {

                            }

                        });
                    }
                }

                break;
            case R.id.lvshui_button:
                if (tuoguan_flash_info.equals("1"))

                {
                    Intent intent2 = new Intent(getActivity(), TuoGuanActivity.class);

                    startActivity(intent2);
                } else

                {


                    if (dialog_flag == 0) {

                        dialog_flag = 1;
                        GetLvshui GetLvshui = new GetLvshui();

                        GetLvshui.execute();
                        GetLvshui.setOnAsyncResponse(new AsyncResponse() {
                            // ????????????????AsyncTask??onPostExecute???????????
                            @Override
                            public void onDataReceivedSuccess(String result) {
                                lvshui = result;

                                if (lvshui.equals("1")) {

                                    LayoutInflater guanbi_inflater3 = getActivity().getLayoutInflater();
                                    final View guanbi_layout3 = guanbi_inflater3.inflate(R.layout.lvshui_guanbi_tanchuang, null);

                                    Builder guanbi_builder3 = new Builder(getActivity());
                                    guanbi_builder3.setView(guanbi_layout3);
                                    guanbi_builder3.setCancelable(false);
                                    final AlertDialog guanbi_dialog3 = guanbi_builder3.create();
                                    // ?????????
                                    guanbi_dialog3.show();

                                    // WindowManager.LayoutParams guanbi_params3 =
                                    // guanbi_dialog3.getWindow().getAttributes();
                                    // guanbi_params3.width = 850;
                                    // guanbi_params3.height = 720;
                                    // guanbi_dialog3.getWindow().setAttributes(guanbi_params3);
                                    TextView title3 = (TextView) guanbi_layout3.findViewById(R.id.title_tanchaung_title);
                                    title3.setText("设定滤水时长(分钟)");
                                    RelativeLayout back = (RelativeLayout) guanbi_layout3.findViewById(R.id.title_tanchaung_back);
                                    back.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            guanbi_dialog3.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    Button queding = (Button) guanbi_layout3.findViewById(R.id.button_lvshui_queding);
                                    queding.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            guanbi_dialog3.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    Button zhongzhi = (Button) guanbi_layout3.findViewById(R.id.button_lvshui_zhongzhi);
                                    zhongzhi.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String cookie = getCookie();
                                                        String result = AppInterface.flashWaterOn(0, 0, cookie);
                                                        JSONObject result_json = new JSONObject(result);
                                                        String error = result_json.getString("error");
                                                        Log.d("data", error + "cdfg");
                                                        if (error.equals("0")) {
                                                            Looper.prepare();
                                                            guanbi_dialog3.dismiss();
                                                            dialog_flag = 0;
                                                            Toast.makeText(getActivity(), "中止成功！", Toast.LENGTH_LONG).show();
                                                            Looper.loop();

                                                        } else {
                                                            Looper.prepare();
                                                            Builder dialog = new Builder(getActivity());
                                                            dialog.setMessage("中止失败！请检查设备");
                                                            dialog.setCancelable(true);
                                                            dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
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

                                            guanbi_dialog3.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String cookie = getCookie();
                                                String rsl = AppInterface.getActionInfo(cookie);
                                                JSONObject json = new JSONObject(rsl);
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLast_lvshui(json.getString("flash_time"));
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLvshui_time(json.getString("flashtime"));
                                                shangyu_time = shengyuTime(
                                                        ((DouYinApplication) getActivity().getApplication()).getLast_lvshui(),
                                                        ((DouYinApplication) getActivity().getApplication()).getLvshui_time());
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            textview1 = (TextView) guanbi_layout3.findViewById(R.id.lvshui_chengyu_text);
                                            Message message = new Message();//
                                            message.what = UPDATE_LVSHUI_SHENGYU;
                                            handler.sendMessage(message);
                                        }
                                    }).start();
                                    lvshui_queding_button = (Button) guanbi_layout3.findViewById(R.id.button_lvshui_queding);
                                    lvshui_queding_button.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {

                                            guanbi_dialog3.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                } else {

                                    LayoutInflater inflater3 = getActivity().getLayoutInflater();

                                    final View layout3 = inflater3.inflate(R.layout.lvshui_tanchuang, null);

                                    Builder builder3 = new Builder(getActivity());
                                    builder3.setView(layout3);
                                    builder3.setCancelable(false); // ???e?????????????????,false?????????
                                    final AlertDialog dialog3 = builder3.create(); // ?????????
                                    dialog3.show();
                                    // WindowManager.LayoutParams params3 =
                                    // dialog3.getWindow().getAttributes();
                                    // dialog3.getWindow().getAttributes();
                                    // params3.width = 850;
                                    // params3.height = 720;
                                    // dialog3.getWindow().setAttributes(params3);
                                    TextView title3 = (TextView) layout3.findViewById(R.id.title_tanchaung_title);
                                    title3.setText("设定滤水时长(分钟)");
                                    RelativeLayout back = (RelativeLayout) layout3.findViewById(R.id.title_tanchaung_back);
                                    back.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog3.dismiss();
                                            dialog_flag = 0;
                                        }
                                    });

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                String cookie = getCookie();
                                                String rsl = AppInterface.getActionInfo(cookie);
                                                JSONObject json = new JSONObject(rsl);
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLast_lvshui(json.getString("flash_time"));
                                                ((DouYinApplication) getActivity().getApplication())
                                                        .setLvshui_time(json.getString("flashtime"));
                                            } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            textview1 = (TextView) layout3.findViewById(R.id.last_lvshui);
                                            edittext2 = (EditText) layout3.findViewById(R.id.now_lvshui);
                                            Message message = new Message();
                                            message.what = UPDATE_LVSHUI;
                                            handler.sendMessage(message);
                                        }
                                    }).start();

                                    lvshui_queding_button = (Button) layout3.findViewById(R.id.button_lvshui_queding);
                                    lvshui_queding_button.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    GetStatus GetStatus = new GetStatus();

                                                    GetStatus.execute();
                                                    GetStatus.setOnAsyncResponse(new AsyncResponse() {
                                                        public void onDataReceivedSuccess(String result2) {
                                                            status = result2;

                                                            if (status.equals("0")) {

                                                                Builder lvshui_dialog = new Builder(
                                                                        getActivity());
                                                                lvshui_dialog.setTitle("提示");
                                                                lvshui_dialog.setMessage("当前设备离线！");
                                                                lvshui_dialog.setCancelable(true);
                                                                lvshui_dialog.setNegativeButton("确定",
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                            }
                                                                        }).create().show();
                                                            } else {


                                                                new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {


                                                                        if (TextUtils.isEmpty(edittext2.getText())) {
                                                                            Looper.prepare();
                                                                            warm_dialog = new Builder(getActivity());
                                                                            warm_dialog.setTitle("提示");
                                                                            warm_dialog.setMessage("输入不能为空！");
                                                                            warm_dialog.setCancelable(true);
                                                                            warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                }
                                                                            }).create().show();
                                                                            Looper.loop();

                                                                        }
                                                                        if (!isNumeric(edittext2.getText().toString())) {
                                                                            Looper.prepare();
                                                                            warm_dialog = new Builder(getActivity());
                                                                            warm_dialog.setTitle("提示");
                                                                            warm_dialog.setMessage("输入的应该为数字！");
                                                                            warm_dialog.setCancelable(true);
                                                                            warm_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                }
                                                                            }).create().show();
                                                                            Looper.loop();

                                                                        }


                                                                        final int lvshui_time = Integer
                                                                                .valueOf(edittext2.getText().toString()).intValue();
                                                                        String cookie = getCookie();

                                                                        try {
                                                                            String result = AppInterface.flashWaterOn(1, lvshui_time,
                                                                                    cookie);
                                                                            JSONObject result_json = new JSONObject(result);
                                                                            String error = result_json.getString("error");
                                                                            if (error.equals("0")) {
                                                                                Looper.prepare();
                                                                                dialog3.dismiss();
                                                                                dialog_flag = 0;
                                                                                Toast.makeText(getActivity(), "滤水成功！", Toast.LENGTH_LONG)
                                                                                        .show();
                                                                                Looper.loop();

                                                                            } else {
                                                                                Looper.prepare();
                                                                                Builder lvshui_dialog = new Builder(
                                                                                        getActivity());
                                                                                lvshui_dialog.setMessage("滤水失败！请检查设备");
                                                                                lvshui_dialog.setCancelable(true);
                                                                                lvshui_dialog.setNegativeButton("确定",
                                                                                        new DialogInterface.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(DialogInterface dialog,
                                                                                                                int which) {
                                                                                            }
                                                                                        }).create().show();
                                                                                Looper.loop();

                                                                            }
                                                                        } catch (IOException e) {
                                                                            // TODO Auto-generated
                                                                            // catch
                                                                            // block
                                                                            e.printStackTrace();
                                                                        } catch (JSONException e) {
                                                                            // TODO Auto-generated
                                                                            // catch
                                                                            // block
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }).start();
                                                            }
                                                        }

                                                        @Override
                                                        public void onDataReceivedFailed() {

                                                        }

                                                    });
                                                }
                                            }).start();

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onDataReceivedFailed() {

                            }

                        });
                    }
                }
                break;
            case R.id.weishi_button:
                if (tuoguan_feed_info.equals("1"))

                {
                    Intent intent3 = new Intent(getActivity(), TuoGuanActivity.class);

                    startActivity(intent3);
                } else

                {


                    if (dialog_flag == 0) {

                        dialog_flag = 1;
                        LayoutInflater inflater4 = getActivity().getLayoutInflater();

                        final View layout4 = inflater4.inflate(R.layout.weishi_tanchaung, null);

                        Builder builder4 = new Builder(getActivity());
                        builder4.setView(layout4);
                        final AlertDialog dialog4 = builder4.create(); // ?????????
                        dialog4.setCancelable(false);
                        dialog4.show();
                        // WindowManager.LayoutParams params4 =
                        // dialog4.getWindow().getAttributes();
                        // dialog4.getWindow().getAttributes();
                        // params4.width = 850;
                        // params4.height = 720;
                        // dialog4.getWindow().setAttributes(params4);
                        TextView title4 = (TextView) layout4.findViewById(R.id.title_tanchaung_title);
                        title4.setText("设定喂食量");
                        RelativeLayout back = (RelativeLayout) layout4.findViewById(R.id.title_tanchaung_back);
                        back.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog4.dismiss();
                                dialog_flag = 0;
                            }
                        });

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String cookie = getCookie();
                                    String rsl = AppInterface.getActionInfo(cookie);
                                    JSONObject json = new JSONObject(rsl);
                                    ((DouYinApplication) getActivity().getApplication())
                                            .setLast_weishi(json.getString("feed_time"));
                                    ((DouYinApplication) getActivity().getApplication()).setWeishi(json.getString("feed"));
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                textview1 = (TextView) layout4.findViewById(R.id.last_weishi);
                                Log.d("data", "weishi" + ((DouYinApplication) getActivity().getApplication()).getWeishi());
                                if (((DouYinApplication) getActivity().getApplication()).getWeishi().equals("1"))
                                    weishi_button = (RadioButton) layout4.findViewById(R.id.weishi_xiao);
                                else if (((DouYinApplication) getActivity().getApplication()).getWeishi().equals("2"))
                                    weishi_button = (RadioButton) layout4.findViewById(R.id.weishi_da);
                                else
                                    weishi_button = (RadioButton) layout4.findViewById(R.id.weishi_zhong);
                                Message message = new Message();// ???????????????????????handleMessage??????????????????????
                                message.what = UPDATE_WEISHI;
                                handler.sendMessage(message);
                            }
                        }).start();

                        weishi_queding_button = (Button) layout4.findViewById(R.id.button_weishi_queding);
                        weishi_queding_button.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        GetStatus GetStatus = new GetStatus();

                                        GetStatus.execute();
                                        GetStatus.setOnAsyncResponse(new AsyncResponse() {
                                            public void onDataReceivedSuccess(String result2) {
                                                status = result2;
                                                if (status.equals("0")) {

                                                    Builder weishi_dialog = new Builder(getActivity());
                                                    weishi_dialog.setTitle("提示");
                                                    weishi_dialog.setMessage("当前设备离线！");
                                                    weishi_dialog.setCancelable(true);
                                                    weishi_dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    }).create().show();
                                                } else {

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            int feed;
                                                            weishi_button_xiao = (RadioButton) layout4.findViewById(R.id.weishi_xiao);
                                                            weishi_button_zhong = (RadioButton) layout4.findViewById(R.id.weishi_zhong);
                                                            weishi_button_da = (RadioButton) layout4.findViewById(R.id.weishi_da);

                                                            if (weishi_button_xiao.isChecked())
                                                                feed = 1;
                                                            else if (weishi_button_zhong.isChecked())
                                                                feed = 2;
                                                            else
                                                                feed = 3;

                                                            String cookie = getCookie();

                                                            try {
                                                                String result = AppInterface.feed(feed, cookie);
                                                                JSONObject result_json = new JSONObject(result);
                                                                String error = result_json.getString("error");
                                                                if (error.equals("0")) {
                                                                    Looper.prepare();
                                                                    dialog4.dismiss();
                                                                    dialog_flag = 0;
                                                                    Toast.makeText(getActivity(), "喂食成功！", Toast.LENGTH_LONG).show();
                                                                    Looper.loop();

                                                                } else {
                                                                    Looper.prepare();
                                                                    Builder weishi_dialog = new Builder(
                                                                            getActivity());
                                                                    weishi_dialog.setMessage("喂食失败！请检查设备");
                                                                    weishi_dialog.setCancelable(true);
                                                                    weishi_dialog
                                                                            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                }
                                                                            }).create().show();
                                                                    Looper.loop();

                                                                }
                                                            } catch (IOException e) {
                                                                // TODO Auto-generated
                                                                // catch
                                                                // block
                                                                e.printStackTrace();
                                                            } catch (JSONException e) {
                                                                // TODO Auto-generated
                                                                // catch
                                                                // block
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }).start();
                                                }
                                            }

                                            @Override
                                            public void onDataReceivedFailed() {

                                            }

                                        });
                                    }
                                }).start();

                            }
                        });
                    }
                }
                break;

        }
    }
}
