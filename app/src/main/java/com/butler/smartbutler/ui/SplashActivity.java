package com.butler.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.utils.UtilTools;

/**
 * 闪屏页
 */
public class SplashActivity extends AppCompatActivity {
    //1，延时2000ms
    //2，判断程序是否第一次运行
    //3，自定义字体
    //4，Activity全屏主题

    private TextView tv_splash;
    /**
     * handler可以用作延时  也可以用于子线程更新ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    //判断程序是否首次运行
    private boolean isFirst() {
        if (ShareUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true)) {
            ShareUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    //初始化view
    private void initView() {
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        tv_splash = (TextView) findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this, tv_splash);
    }

    /**
     * 禁止返回键
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
