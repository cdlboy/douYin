package com.douyin.example;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.douyin.R;

public class DataFragment extends Fragment implements OnClickListener {


    private RelativeLayout lay_tongji1;
    private RelativeLayout lay_tongji2;
    private RelativeLayout lay_tongji3;
    private RelativeLayout lay_tongji4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.data_fragment, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lay_tongji1 = (RelativeLayout) getActivity().findViewById(R.id.layout_tongji1);
        lay_tongji1.setOnClickListener(this);

        lay_tongji2 = (RelativeLayout) getActivity().findViewById(R.id.layout_tongji2);
        lay_tongji2.setOnClickListener(this);

        lay_tongji3 = (RelativeLayout) getActivity().findViewById(R.id.layout_tongji3);
        lay_tongji3.setOnClickListener(this);

        lay_tongji4 = (RelativeLayout) getActivity().findViewById(R.id.layout_tongji4);
        lay_tongji4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_tongji1:
                Intent intent1 = new Intent(getActivity(), TongjiActivity.class);
                intent1.putExtra("webview","1");
                startActivity(intent1);
                break;
            case R.id.layout_tongji2:
                Intent intent2 = new Intent(getActivity(), TongjiActivity.class);
                intent2.putExtra("webview","2");
                startActivity(intent2);
                break;
            case R.id.layout_tongji3:
                Intent intent3 = new Intent(getActivity(), TongjiActivity.class);
                intent3.putExtra("webview","3");
                startActivity(intent3);
                break;
            case R.id.layout_tongji4:
                Intent intent4 = new Intent(getActivity(), TongjiActivity.class);
                intent4.putExtra("webview","4");
                startActivity(intent4);
                break;
            default:
                break;

        }
    }
}
