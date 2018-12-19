package com.butler.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.butler.smartbutler.R;
import com.butler.smartbutler.ui.BaseActivity;

public class WebViewActivity extends BaseActivity {
    //进度
    private ProgressBar progressBar;
    //网页
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.mProgressBar);
        webView = findViewById(R.id.mWebView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");
        //设置标题
        getSupportActionBar().setTitle(title);

        //进行加载网页的逻辑
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        settings.setSupportZoom(true);//支持缩放
        settings.setBuiltInZoomControls(true);
        //接口回调
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 进度变化的监听
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //加载网页
        webView.loadUrl(url);

        //本地显示（防止跳转浏览器）
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //我接受这个事件
                return true;
            }
        });

    }
}
