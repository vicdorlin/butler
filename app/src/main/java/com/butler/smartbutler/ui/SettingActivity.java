package com.butler.smartbutler.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.service.SmsService;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.utils.ToastUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch aSwitch;
    //短信提醒
    private Switch sw_sms;
    private int SYSTEM_ALERT_WINDOW_PERMISSION_CODE = 1;

    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        aSwitch = findViewById(R.id.sw_speak);
        aSwitch.setOnClickListener(this);
        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        aSwitch.setChecked(isSpeak);
        sw_sms = findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_sms.setChecked(isSms);
        tv_version = findViewById(R.id.tv_version);
        tv_version.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(SettingActivity.this,Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED){
                        ToastUtil.showShortToastCenter("you have already granted this permission!");
                    }else {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SYSTEM_ALERT_WINDOW)){
                            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("this permission is needed because of this and that")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(SettingActivity.this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},1);
                                        }
                                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                        }else {
                            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},SYSTEM_ALERT_WINDOW_PERMISSION_CODE);
                        }
                    }
                }
                break;
            case R.id.sw_speak:
                //切换相反
                aSwitch.setSelected(!aSwitch.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", aSwitch.isChecked());
                break;
            case R.id.sw_sms:
                sw_sms.setSelected(!sw_sms.isSelected());
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                if (sw_sms.isChecked()) {
                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                ToastUtil.showShortToastCenter("permission granted");
            }else {
                ToastUtil.showShortToastCenter("permission denied");
            }
        }
    }
}
