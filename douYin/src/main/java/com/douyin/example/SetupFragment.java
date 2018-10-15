package com.douyin.example;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.douyin.R;
import com.douyin.model.DouYinApplication;

public class SetupFragment extends Fragment implements OnClickListener {

    private RelativeLayout lay_people;
    private RelativeLayout lay_shezhi;
    private RelativeLayout lay_wifi;
    private TextView user_name;
    private TextView device_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.setup_fragment, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user_name = (TextView) getActivity().findViewById(R.id.name_people);
        device_id = (TextView) getActivity().findViewById(R.id.name_shebei);
        user_name.setText(((DouYinApplication) getActivity().getApplication()).getUser_name());
        device_id.setText(((DouYinApplication) getActivity().getApplication()).getDevice_id());
        lay_people = (RelativeLayout) getActivity().findViewById(R.id.layout_people);
        lay_shezhi = (RelativeLayout) getActivity().findViewById(R.id.layout_shezhi);
        lay_wifi = (RelativeLayout) getActivity().findViewById(R.id.layout_wifi);
        lay_people.setOnClickListener(this);
        lay_shezhi.setOnClickListener(this);
        lay_wifi.setOnClickListener(this);
    }

    @Override



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_wifi:
                WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                String wifi_name = wifiInfo.getSSID();
                String wifi_name2 = wifi_name.replaceAll("\"", "");

                if (!wifi_name2.equals("myssid")) {

                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(getActivity());
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("当前没有连上相应Wifi,是否立即前往连接");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                                }
                            });
                    normalDialog.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    // 显示
                    normalDialog.show();

                } else {
                    Intent intent=new Intent(getActivity(),lastActivity.class);
                    startActivity(intent);



//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent= new Intent();
//                            intent.setAction("android.intent.action.VIEW");
//                            Uri content_url = Uri.parse("192.168.4.1");
//                            intent.setData(content_url);
//                            startActivity(intent);
//                        }
//                    });

                }

                break;
            case R.id.layout_people:
                Intent intent = new Intent(getActivity(), PeopleActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_shezhi:
                Intent intent2 = new Intent(getActivity(), ShezhiActivity.class);
                startActivity(intent2);
            default:
                break;


        }
    }
}
