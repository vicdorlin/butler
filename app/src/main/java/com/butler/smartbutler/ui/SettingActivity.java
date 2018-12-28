package com.butler.smartbutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.butler.smartbutler.R;
import com.butler.smartbutler.service.SmsService;
import com.butler.smartbutler.utils.L;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch aSwitch;
    //短信提醒
    private Switch sw_sms;
    private int SYSTEM_ALERT_WINDOW_PERMISSION_CODE = 1;

    private TextView tv_version;
    private LinearLayout llUpdate;
    private String versionName;
    private int versionCode;
    private String url;
    //我的定位
    private LinearLayout llLocation;
    //关于软件
    private LinearLayout llAbout;

    //扫一扫
    private LinearLayout llScan;
    private TextView tvScan;
    //生成二维码
    private LinearLayout llQrCode;

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
        llUpdate = findViewById(R.id.ll_update);
        llUpdate.setOnClickListener(this);
        try {
            initVersion();
            tv_version.setText("当前版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }
        llScan = findViewById(R.id.ll_scan);
        llScan.setOnClickListener(this);
        tvScan = findViewById(R.id.tv_scan_result);
        llQrCode = findViewById(R.id.ll_qr_code);
        llQrCode.setOnClickListener(this);
        llLocation = findViewById(R.id.ll_my_location);
        llLocation.setOnClickListener(this);
        llAbout = findViewById(R.id.ll_about);
        llAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this, QrCodeActivity.class));
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_update:
                /**
                 * 步骤
                 * 1，请求服务器配置文件，拿到code
                 * 2,比较
                 * 3，dialog
                 * 4，跳转到更新界面，并且把url传递过去
                 */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("json:" + t);
                        JSONObject jsonObject = JSON.parseObject(t);
                        Integer code = jsonObject.getInteger("versionCode");
                        if (code == null || code <= versionCode) {
                            ToastUtil.showShortToastCenter("当前已是最新版本");
                        }
                        String name = jsonObject.getString("versionName");
                        String content = jsonObject.getString("content");
                        url = jsonObject.getString("url");
                        showUpdateDialog("版本号：" + name + "\n升级说明：" + content);
                    }
                });

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

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this).setTitle("有新版本啦！！")
                .setMessage(content)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    }
                }).setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //什么都不做也会执行dismiss()
            }
        }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showShortToastCenter("permission granted");
            } else {
                ToastUtil.showShortToastCenter("permission denied");
            }
        }
    }

    //获取版本号/code
    private void initVersion() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);//O表示本地

        versionCode = packageInfo.versionCode;
        versionName = packageInfo.versionName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tvScan.setText(scanResult);
        }
    }
}
