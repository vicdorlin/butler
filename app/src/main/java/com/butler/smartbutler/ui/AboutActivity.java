package com.butler.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.fragment.WebViewActivity;
import com.butler.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends BaseActivity {
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除阴影
        setContentView(R.layout.activity_about);
        getSupportActionBar().setElevation(0);
        initView();
    }

    //初始化view
    private void initView() {
        mListView = findViewById(R.id.mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AboutActivity.this, WebViewActivity.class);
                //intent两种方法传值
                /*Bundle bundle = new Bundle();
                bundle.putString("key","values");
                intent.putExtras(bundle);*/
                String data = mList.get(position);
                if (data.startsWith("http")) {
                    intent.putExtra("title", "就是试试");
                    intent.putExtra("url", data);
                    startActivity(intent);
                }
            }
        });
        mList.add(getString(R.string.text_app_name) + getString(R.string.app_name));
        mList.add(getString(R.string.text_version) + UtilTools.getVersion(this));
        mList.add(getString(R.string.text_website_address));

        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList);
        //设置适配器
        mListView.setAdapter(mAdapter);
    }
}
