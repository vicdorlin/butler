package com.butler.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.L;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;

import java.io.File;

public class UpdateActivity extends BaseActivity {
    public static final int HANDLER_LOADING = 10001;//正在下载
    public static final int HANDLER_FINISHED = 10002;//下载完成
    public static final int HANDLER_FAIL = 10003;//下载失败

    private TextView tvSize;
    private String url;
    private String path;
    //进度圈
    private CircleProgressBar circleProgressBar;

    //为什么用handler：因为是异步请求，如果在子线程中操作了主线程UI会报错，安卓只能在主线程中更新UI
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LOADING:
                    //实时更新进度
                    Bundle data = msg.getData();
                    long transferredBytes = data.getLong("transferredBytes");
                    long totalSize = data.getLong("totalSize");
                    tvSize.setText(transferredBytes + "/" + totalSize);
                    //设置进度
                    circleProgressBar.setProgress((int) (100 * transferredBytes / totalSize));
                    break;
                case HANDLER_FINISHED:
                    tvSize.setText("下载成功");
                    //启动应用安装
                    startInstallApk();
                    break;
                case HANDLER_FAIL:
//                    L.e("失败");
                    tvSize.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装（隐式意图）
    private void startInstallApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private static final String DEFAULT_PATTERN = "%d%%";

    private void initView() {
        tvSize = findViewById(R.id.tv_size);
        circleProgressBar = findViewById(R.id.line_progress);
        circleProgressBar.setMax(100);
        circleProgressBar.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
            }
        });

        url = getIntent().getStringExtra("url");
        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        if (!TextUtils.isEmpty(url)) {
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
//                    L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message message = new Message();
                    message.what = HANDLER_LOADING;
                    //这里通过Bundle传值  发送一个Bundle
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    handler.sendEmptyMessage(HANDLER_FINISHED);
                }

                @Override
                public void onFailure(VolleyError error) {
                    handler.sendEmptyMessage(HANDLER_FAIL);
                    L.e("失败");
                }
            });
        }
    }
}
