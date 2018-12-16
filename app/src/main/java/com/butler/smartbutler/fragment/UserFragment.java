package com.butler.smartbutler.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.User;
import com.butler.smartbutler.ui.ForgetPasswordActivity;
import com.butler.smartbutler.ui.LoginActivity;
import com.butler.smartbutler.ui.LogisticActivity;
import com.butler.smartbutler.utils.L;
import com.butler.smartbutler.utils.ShareUtils;
import com.butler.smartbutler.utils.ToastUtil;
import com.butler.smartbutler.utils.UtilTools;
import com.butler.smartbutler.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment implements View.OnClickListener {
    private Button btnExitLogin;
    private Button btnModifyPwd;

    private TextView editUser;
    private EditText etUsername;
    private EditText etSex;
    private EditText etAge;
    private EditText etDesc;
    private TextView tvLogistics;
    private TextView tvPhoneLocation;
    private Button btnUpdateOk;
    private LinearLayout layoutAll;
    //圆形头像
    private CircleImageView circleImageView;
    //编辑头像提示框
    private CustomDialog dialog;
    private Button btnCamera;
    private Button btnPicture;
    private Button btnCancel;

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
        editUser = view.findViewById(R.id.edit_user);
        editUser.setOnClickListener(this);
        circleImageView = view.findViewById(R.id.profile_image);
        circleImageView.setOnClickListener(this);
        UtilTools.getImageToShare(getContext(), circleImageView);

        etUsername = view.findViewById(R.id.et_username);
        etSex = view.findViewById(R.id.et_sex);
        etAge = view.findViewById(R.id.et_age);
        etDesc = view.findViewById(R.id.et_desc);
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            ToastUtil.showShortToastCenter("您还没有登录，请重新登录");
        }
        etUsername.setText(user.getUsername());
        etSex.setText(user.getSex() == 2 ? "女" : "男");
        etAge.setText(String.valueOf(user.getAge()));
        etDesc.setText(user.getDesc());

        btnUpdateOk = view.findViewById(R.id.btn_update_ok);
        btnUpdateOk.setOnClickListener(this);
        layoutAll = view.findViewById(R.id.layoutAll);

        dialog = new CustomDialog(getContext(), 500, 500, R.layout.dialog_photo, R.style.theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        btnCamera = dialog.findViewById(R.id.btn_camera);
        btnPicture = dialog.findViewById(R.id.btn_picture);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCamera.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        tvLogistics = view.findViewById(R.id.tv_logistics);
        tvLogistics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                if (BmobUser.isLogin()) {
                    new AlertDialog.Builder(getActivity()).setTitle("退出程序").setMessage("是否退出程序")
                            .setPositiveButton("取消", null).setNegativeButton("确定", new DialogInterface.OnClickListener() {
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
                break;
            case R.id.edit_user://编辑资料
                setModifyStatus(true);
                markUserData(calUserData());
                break;
            case R.id.layoutAll:
            case R.id.btn_update_ok:
                String oldData = ShareUtils.getString(getContext(), "userData", null);
                User user = calUserData();
                //表示数据有修改
                if (!user.toString().equals(oldData)) {
                    User oldUser = BmobUser.getCurrentUser(User.class);
                    oldUser.setUsername(user.getUsername());
                    oldUser.setSex(user.getSex());
                    oldUser.setDesc(user.getDesc());
                    oldUser.setAge(user.getAge());
                    oldUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtil.showShortToastCenter("更新用户信息成功");
                            } else {
                                ToastUtil.showShortToastCenter("更新用户信息失败");
                            }
                        }
                    });
                    ShareUtils.delShare(getContext(), "userData");
                }
                setModifyStatus(false);
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_logistics:
                startActivity(new Intent(getActivity(), LogisticActivity.class));
                break;
        }
    }

    private void markUserData(User user) {
        if (user == null) return;
        ShareUtils.putString(getContext(), "userData", user.toString());
    }

    private User calUserData() {
        User user = new User();
        String age = etAge.getText().toString();
        if (!TextUtils.isEmpty(age)) {
            user.setAge(Integer.valueOf(age));
        }
        String name = etUsername.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            user.setUsername(name);
        }
        user.setDesc(etDesc.getText().toString());
        user.setSex("女".equals(etSex.getText().toString()) ? 2 : 1);
        return user;
    }

    private void setModifyStatus(boolean canEdit) {
        etUsername.setEnabled(canEdit);
        etSex.setEnabled(canEdit);
        etAge.setEnabled(canEdit);
        etDesc.setEnabled(canEdit);
        if (canEdit) {
            etSex.clearFocus();
            etSex.setSelected(false);
            etAge.clearFocus();
            etAge.setSelected(false);
            etDesc.clearFocus();
            etDesc.setSelected(false);
            btnUpdateOk.setVisibility(View.VISIBLE);
            layoutAll.setOnClickListener(this);
        } else {
            btnUpdateOk.setVisibility(View.GONE);
            layoutAll.setOnClickListener(null);
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            circleImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(getActivity(), circleImageView);
    }
}
