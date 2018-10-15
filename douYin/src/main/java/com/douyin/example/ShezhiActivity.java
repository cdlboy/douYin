package com.douyin.example;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.douyin.R;

public class ShezhiActivity extends Activity implements View.OnClickListener {

    private TextView title;

    private Dialog mCameraDialog;

    private RelativeLayout relativeLayout_tuichu;
    private RelativeLayout relativeLayout_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_shezhi);
        title = (TextView) findViewById(R.id.title_title);
        title.setText("设置");
        relativeLayout_tuichu = (RelativeLayout) findViewById(R.id.tuichu);
        relativeLayout_tuichu.setOnClickListener(ShezhiActivity.this);
        relativeLayout_about = (RelativeLayout) findViewById(R.id.about);
        relativeLayout_about.setOnClickListener(ShezhiActivity.this);
        ActivityCollector.addActivity(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about:
                Intent intent1 = new Intent(ShezhiActivity.this, AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.tuichu:
                //弹出对话框
                setDialog();
                break;
            case R.id.dialog_genghuan:
                //更换账号按钮

                Intent intent = new Intent(ShezhiActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.dialog_tuichu:
                //关闭按钮

                mCameraDialog.cancel();

                final ProgressDialog dialog = ProgressDialog.show(ShezhiActivity.this, "",

                        "正在退出，请稍等", true, true);

                Thread t = new Thread(new Runnable() {

                    @Override

                    public void run() {

                        try {

                            Thread.sleep(2000);//让他显示10秒后，取消ProgressDialog

                        } catch (InterruptedException e) {

// TODO Auto-generated catch block

                            e.printStackTrace();

                        }

                        dialog.dismiss();
                        ActivityCollector.finishAll();
                    }

                });

                t.start();




                break;
            case R.id.dialog_quxiao:
                //取消按钮
                mCameraDialog.cancel();
                break;


        }
    }

    private void setDialog() {
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_dialog, null);
        //初始化视图
        root.findViewById(R.id.dialog_genghuan).setOnClickListener(this);
        root.findViewById(R.id.dialog_tuichu).setOnClickListener(this);
        root.findViewById(R.id.dialog_quxiao).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shezhi, menu);
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
}
