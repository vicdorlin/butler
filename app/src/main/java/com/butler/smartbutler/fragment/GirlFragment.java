package com.butler.smartbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.butler.smartbutler.R;
import com.butler.smartbutler.adapter.GridAdapter;
import com.butler.smartbutler.entity.GridData;
import com.butler.smartbutler.utils.PicassoUtil;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.view.CustomDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.List;

public class GirlFragment extends Fragment {
    //列表
    private GridView gridView;
    //数据
    private List<GridData> list = new ArrayList<>();
    //提示框
    private CustomDialog dialog;
    //预览图片
    private PhotoView photoView;
    //PhotoView
    private PhotoViewAttacher photoViewAttacher;
    private boolean isLoad;
    private int startNum = 0;
    private int size = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        gridView = view.findViewById(R.id.mGridView);
        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_girl,
                R.style.theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        photoView = dialog.findViewById(R.id.photo_view);

        String url = StaticClass.GIRL_URL.replace("{start}", startNum + "").replace("{size}", size + "");
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtil.loadImageView(list.get(position).getImgUrl(), photoView);
                //缩放
                photoViewAttacher = new PhotoViewAttacher(photoView);
                //刷新
                photoViewAttacher.update();
                dialog.show();
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isLoad && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    startNum++;
                    RxVolley.get(StaticClass.GIRL_URL.replace("{start}", startNum * size + "").replace("{size}", size + ""), new HttpCallback() {
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
        int index = gridView.getFirstVisiblePosition();
//        View v = gridView.getChildAt(0);
//        int top = v == null ? 0 : v.getTop();
        JSONObject jsonObject = JSON.parseObject(t);
        List<GridData> dataList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("imgs");
        for (int i = 0; i < jsonArray.size(); i++) {
            Object o = jsonArray.get(i);
            if (o == null) continue;
            JSONObject jo = (JSONObject) o;
            if (TextUtils.isEmpty(jo.getString("imageUrl"))) continue;
            String url = jo.getString("imageUrl");
            GridData data = new GridData();
            data.setImgUrl(url);
            dataList.add(data);
        }
        list.addAll(dataList);
        gridView.setAdapter(new GridAdapter(getActivity(), list));
        gridView.setSelection(index + 2);
    }
}
