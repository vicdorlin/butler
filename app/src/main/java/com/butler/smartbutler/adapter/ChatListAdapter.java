package com.butler.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.ChatListData;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    //左边的type
    public static final int VALUE_TEXT_LEFT = 1;
    //右边的type
    public static final int VALUE_TEXT_RIGHT = 2;

    private Context mContext;
    private LayoutInflater inflater;
    private ChatListData data;
    private List<ChatListData> mList;

    public ChatListAdapter(Context mContext, List<ChatListData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeftText viewHolderLeftText = null;
        ViewHolderRightText viewHolderRightText = null;
        //获取当前要显示的type 根据这个type来区分数据的加载
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case VALUE_TEXT_LEFT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = inflater.inflate(R.layout.left_item, null);
                    viewHolderLeftText.tv_text_left = convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case VALUE_TEXT_RIGHT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = inflater.inflate(R.layout.right_item, null);
                    viewHolderRightText.tv_text_right = convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }
        } else {
            switch (type) {
                case VALUE_TEXT_LEFT:
                    viewHolderLeftText = (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_TEXT_RIGHT:
                    viewHolderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }
        //赋值
        ChatListData data = mList.get(position);
        switch (type) {
            case VALUE_TEXT_LEFT:
                viewHolderLeftText.tv_text_left.setText(data.getText());
                break;
            case VALUE_TEXT_RIGHT:
                viewHolderRightText.tv_text_right.setText(data.getText());
                break;
        }

        return convertView;
    }

    /**
     * 根据数据源的position来返回要显示的item
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        ChatListData data = mList.get(position);
        int type = data.getType();
        return type;
    }

    /**
     * 返回所有的layout数量
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 3;//mList.size() + 1;
    }

    //左边的文本
    class ViewHolderLeftText {
        private TextView tv_text_left;
    }



    class ViewHolderRightText {
        private TextView tv_text_right;
    }
}
