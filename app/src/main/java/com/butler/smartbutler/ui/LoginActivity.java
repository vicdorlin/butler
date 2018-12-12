package com.butler.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.butler.smartbutler.MainActivity;
import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.User;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //注册按钮
    private Button btnRegister;
    private Button btnLogin;
    private EditText textUsername;
    private EditText textPassword;
    private CheckBox keepPassword;
    private TextView forgetPwdView;

    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        textUsername = findViewById(R.id.text_username);
        textPassword = findViewById(R.id.text_pwd);
        keepPassword = findViewById(R.id.box_remember_pwd);
        forgetPwdView = findViewById(R.id.text_forget_pwd);
        forgetPwdView.setOnClickListener(this);
        boolean isChecked = ShareUtils.getBoolean(this, "keepPassowrd", false);
        if (isChecked) {
            keepPassword.setChecked(isChecked);
            textUsername.setText(ShareUtils.getString(this, "username", ""));
            textPassword.setText(ShareUtils.getString(this, "password", ""));
        }

        dialog = new CustomDialog(this, 100, 199, R.layout.dialog_loading, R.style.theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                String username = textUsername.getText().toString().trim();
                String pwd = textPassword.getText().toString().trim();
                User user = new User();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT);
                    return;
                }
                dialog.show();
                user.setUsername(username);
                user.setPassword(pwd);
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        dialog.dismiss();
                        if (e == null) {
                            if (!user.getEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "请先验证邮箱后再登录", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else if (e.getErrorCode() == 101) {
                            Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                break;
            case R.id.text_forget_pwd:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this, "keepPassowrd", keepPassword.isChecked());
        if (keepPassword.isChecked()) {
            ShareUtils.putString(this, "username", textUsername.getText().toString().trim());
            ShareUtils.putString(this, "password", textPassword.getText().toString().trim());
        } else {
            ShareUtils.delShare(this, "username");
            ShareUtils.delShare(this, "password");
        }
    }
}
