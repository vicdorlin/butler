package com.butler.smartbutler.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.butler.smartbutler.R;
import com.butler.smartbutler.ui.ForgetPasswordActivity;
import com.butler.smartbutler.ui.LoginActivity;
import com.butler.smartbutler.utils.ToastUtil;

import cn.bmob.v3.BmobUser;

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button btnExitLogin;
    private Button btnModifyPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    /**
     * 初始化view
     *
     * @param view
     */
    private void findView(View view) {
        btnExitLogin = view.findViewById(R.id.btn_exit_user);
        btnModifyPwd = view.findViewById(R.id.btn_modfy_pwd);
        btnExitLogin.setOnClickListener(this);
        btnModifyPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                if (BmobUser.isLogin()) {
                    new AlertDialog.Builder(getActivity()).setTitle("退出程序").setMessage("是否退出程序")
                            .setPositiveButton("取消",null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BmobUser.logOut();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            ToastUtil.showShortToastCenter("退出登录成功");
                            getActivity().finish();
                        }
                    }).show();
                } else {
                    ToastUtil.showShortToastCenter("当前已退出登录");
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }

                break;
            case R.id.btn_modfy_pwd:
                startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
        }
    }
}
