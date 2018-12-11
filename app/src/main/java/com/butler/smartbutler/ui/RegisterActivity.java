package com.butler.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.User;
import com.butler.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText etUser;
    private EditText etAge;
    private EditText etDesc;
    private RadioGroup etRadioGroup;
    private EditText etPwd;
    private EditText etPwdConfirm;
    private EditText etEmail;
    private Button etBtnRegister;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        etUser = findViewById(R.id.et_user);
        etAge = findViewById(R.id.et_age);
        etDesc = findViewById(R.id.et_desc);
        etRadioGroup = findViewById(R.id.et_mRadioGroup);
        etPwd = findViewById(R.id.et_pass);
        etPwdConfirm = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etBtnRegister = findViewById(R.id.et_btnRegister);
        etBtnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_btnRegister:
                //获取到输入框的值
                String name = etUser.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();
                Integer sex = R.id.rb_girl == etRadioGroup.getCheckedRadioButtonId() ? 2 : 1;
                String pwd = etPwd.getText().toString().trim();
                String pwdConfirm = etPwdConfirm.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                L.i("======sex:" + sex);
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdConfirm) || TextUtils.isEmpty(email)) {
                    Toast.makeText(this, "输入框数据不全", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pwd.equals(pwdConfirm)) {
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(desc)) {
                    desc = "暂无简介";
                }

                User user = new User();
                user.setAge(Integer.valueOf(age));
                user.setUsername(name);
                user.setDesc(desc);
                user.setSex(sex);
                user.setPassword(pwd);
                user.setEmail(email);

                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                break;
        }
    }
}
