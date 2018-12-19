package com.butler.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.WechatSelectionData;
import com.butler.smartbutler.utils.PicassoUtil;
import com.butler.smartbutler.utils.ScreenUtil;

import java.util.List;

public class WechatAdapter extends BaseAdapter {
    private Context context;
    private List<WechatSelectionData> dataList;
    private LayoutInflater layoutInflater;

    public WechatAdapter(Context context, List<WechatSelectionData> dataList) {
        this.context = context;
        this.dataList = dataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        //判断是否第一次运行
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.wechat_item, null);
            viewHolder.ivImg = convertView.findViewById(R.id.iv_img);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            viewHolder.tvSource = convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //拿到当前的这个实体类
        WechatSelectionData data = dataList.get(position);
        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvSource.setText(data.getSource());
        String imgUrl = data.getFirstimg();
        if (TextUtils.isEmpty(imgUrl))
            imgUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3572520673,1947183971&fm=173&app=49&f=JPEG?w=640&h=427&s=56B6876426D051DE8280D4D0030010F2";
        PicassoUtil.loadImageViewSize(imgUrl, viewHolder.ivImg, ScreenUtil.getScreenWidthPixels(context) / 3, ScreenUtil.getScreenWidthPixels(context) / 4);
        return convertView;
    }

    class ViewHolder {
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvSource;
    }
}
