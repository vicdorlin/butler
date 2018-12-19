package com.butler.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.GridData;
import com.butler.smartbutler.utils.PicassoUtil;
import com.butler.smartbutler.utils.ScreenUtil;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<GridData> dataList;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<GridData> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.girl_item, null);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GridData data = dataList.get(position);
        //解析图片
        PicassoUtil.loadImageViewSize(data.getImgUrl(),viewHolder.imageView,ScreenUtil.getScreenWidthPixels(context)/2,ScreenUtil.getScreenHeightPixels(context)/3);

        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
