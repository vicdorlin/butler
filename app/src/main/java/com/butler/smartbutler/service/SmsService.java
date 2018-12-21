package com.butler.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.L;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.view.DispatchLineatLayout;


/*
 *项目名： SmartButler
 *包名：   com.imooc.smartbutler.service
 *文件名:  SmsService
 *创建者:  LGL
 *创建时间:2016/11/202:42
 *描述:    短信监听服务
 */
public class SmsService extends Service implements View.OnClickListener {
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    //窗口管理器
    private WindowManager windowManager;
    //布局参数
    private WindowManager.LayoutParams layoutParams;

    private DispatchLineatLayout mView;

    private TextView tvPhone;
    private TextView tvSmsContent;
    private Button btnReply;

    private LinearLayout layout;

    private HomeWatchReceiver homeWatchReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    //初始化
    private void init() {
        L.i("init service");

        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加action
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intentFilter);

        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentF = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver, intentF);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        //注销
        unregisterReceiver(smsReceiver);
        unregisterReceiver(homeWatchReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reply:
                sendSms();
            case R.id.ll_sms_item:
                if (mView.getParent() != null) {
                    windowManager.removeViewImmediate(mView);
                }
                break;
        }
    }

    //回复短信
    private void sendSms() {
        Uri parse = Uri.parse("smsto:" + smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, parse);
        //设置自动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", "");
        startActivity(intent);
    }

    /**
     * 短信广播
     */
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (StaticClass.SMS_ACTION.equals(intent.getAction())) {
                L.i("来短信了");
                //获取短信内容返回的是一个object数组
                Object[] pduses = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for (Object pdus : pduses) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus);
                    smsPhone = smsMessage.getOriginatingAddress();//发件人
                    smsContent = smsMessage.getMessageBody();
//                    ToastUtil.showShortToastCenter("发件人：" + smsPhone + "内容：" + smsContent);
                    showWindow();
                }
            }
        }
    }

    //窗口提示
    private void showWindow() {

        //有了权限，具体的动作
        //获取系统服务
        windowManager = (WindowManager) getApplicationContext().getSystemService(getApplication().WINDOW_SERVICE);
        //获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSPARENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //加载布局
        mView = (DispatchLineatLayout) View.inflate(getApplicationContext(), R.layout.sms_item, null);

        tvPhone = mView.findViewById(R.id.tv_phone);
        tvSmsContent = mView.findViewById(R.id.tv_smsContent);
        btnReply = mView.findViewById(R.id.btn_reply);
        btnReply.setOnClickListener(this);
        tvPhone.setText("发件人：" + smsPhone);
        tvSmsContent.setText(smsContent);
        layout = mView.findViewById(R.id.ll_sms_item);
        layout.setOnClickListener(this);
        //添加view到窗口
        windowManager.addView(mView, layoutParams);

        mView.setDispatchKeyEventListener(new DispatchLineatLayout.DispatchKeyEventListener() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                //判断是否按返回键
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (mView.getParent() != null) {
                        windowManager.removeView(mView);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra("reason");
                if ("homekey".equals(reason)) {
                    if (mView.getParent() != null) {
                        windowManager.removeView(mView);
                    }
                }
            }
        }
    }

}
