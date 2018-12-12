package com.butler.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.utils.ToastUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText etNow;
    private EditText etNew;
    private EditText etConfirm;
    private EditText etEmail;
    private Button btnUpdatePwd;
    private Button btnForgetPwd;
    private TextView tvModPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        initView();
    }

    private void initView() {
        tvModPwd = findViewById(R.id.tv_modPwd);
        etNow = findViewById(R.id.et_now);
        etNew = findViewById(R.id.et_new);
        etConfirm = findViewById(R.id.et_new_confirm);
        etEmail = findViewById(R.id.et_email);
        btnUpdatePwd = findViewById(R.id.btn_update_password);
        btnUpdatePwd.setOnClickListener(this);
        btnForgetPwd = findViewById(R.id.btn_forget_password);
        btnForgetPwd.setOnClickListener(this);
        //用户未登录 隐藏修改密码框
        if (!BmobUser.isLogin()) {
            tvModPwd.setVisibility(View.GONE);
            btnUpdatePwd.setVisibility(View.GONE);
            etNow.setVisibility(View.GONE);
            etNew.setVisibility(View.GONE);
            etConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_password:
                String oldPwd = etNow.getText().toString();
                String newPwd = etNew.getText().toString();
                String newPwdConfirm = etConfirm.getText().toString();
                if (!newPwd.equals(newPwdConfirm)) {
                    ToastUtil.showShortToast("确认密码不一致");
                    break;
                }
                if (newPwd.equals(oldPwd)) {
                    ToastUtil.showShortToast("新旧密码相同!");
                    break;
                }
                //已经登录的用户才可以修改密码
                BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            BmobUser.logOut();//退出登录
                            ShareUtils.delShare(ForgetPasswordActivity.this, "password");
                            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                            ToastUtil.showShortToast("密码更新成功，请重新登录");
                            finish();
                            return;
                        } else {
                            ToastUtil.showShortToast("更新失败");
                            return;
                        }
                    }
                });
                break;
            case R.id.btn_forget_password:
                String email = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    ToastUtil.showShortToast("请输入");
                    return;
                }
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ShareUtils.delShare(ForgetPasswordActivity.this, "password");
                            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                            ToastUtil.showShortToast("邮件发送成功，请前往邮箱验证");
                            finish();
                        } else {
                            ToastUtil.showShortToast("邮件发送失败 code:" + e.getErrorCode());
                        }
                    }
                });
                break;
        }
    }
}
