package com.butler.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.User;
import com.butler.smartbutler.utils.ScreenUtil;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import cn.bmob.v3.BmobUser;

public class QrCodeActivity extends BaseActivity {
    private ImageView qrImgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    private void initView() {
        qrImgImageView = findViewById(R.id.iv_qr_code);
        String contentString = BmobUser.getCurrentUser(User.class).getUsername();
        if (!contentString.equals("")) {
            //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
            Bitmap qrCodeBitmap = EncodingUtils.createQRCode(contentString, ScreenUtil.getScreenWidthPixels(this) / 2,
                    ScreenUtil.getScreenWidthPixels(this) / 2,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
            qrImgImageView.setImageBitmap(qrCodeBitmap);
        } else {
            Toast.makeText(this, "Text can not be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
