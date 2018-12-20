package com.butler.smartbutler.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.ShareUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    //语音播报
    private Switch aSwitch;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_speak:
                //切换相反
                aSwitch.setSelected(!aSwitch.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", aSwitch.isChecked());
                break;
        }
    }
}
