package com.butler.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.butler.smartbutler.R;
import com.butler.smartbutler.adapter.WechatAdapter;
import com.butler.smartbutler.entity.WechatSelectionData;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.List;

public class WechatFragment extends Fragment {
    private ListView mListView;
    List<WechatSelectionData> list = new ArrayList<>();
    private boolean isLoad;
    private int startNum = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView = view.findViewById(R.id.mListView);
        final String url = StaticClass.WECHAT_SELECTION_URL;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
//                ToastUtil.showShortToastCenter(t);
                parsingJson(t);
            }
        });
        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                //intent两种方法传值
                /*Bundle bundle = new Bundle();
                bundle.putString("key","values");
                intent.putExtras(bundle);*/
                WechatSelectionData data = list.get(position);
                intent.putExtra("title", data.getTitle());
                intent.putExtra("url", data.getUrl());
                startActivity(intent);
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLoad && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    startNum++;
                    RxVolley.get(url + ("&pno=" + startNum), new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            parsingJson(t);
                        }
                    });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //记录当屏幕显示的条目数与总条目数相等就说明到底部了就为true
                isLoad = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });
    }

    private void parsingJson(String t) {
        JSONObject jsonObject = JSON.parseObject(t);
        JSONObject jsonResult = jsonObject.getJSONObject("result");
        if (jsonObject == null) {
            ToastUtil.showShortToastCenter("没有数据了");
            return;
        }
        int index = mListView.getFirstVisiblePosition();
        View v = mListView.getChildAt(0);
        int top = v == null ? 0 : v.getTop();
        List<WechatSelectionData> selectionDataList = jsonResult.getJSONArray("list").toJavaList(WechatSelectionData.class);
        if (!list.containsAll(selectionDataList)) {
            list.addAll(selectionDataList);
        }

        mListView.setAdapter(new WechatAdapter(getContext(), list));
        mListView.setSelectionFromTop(index, top);
    }
}
