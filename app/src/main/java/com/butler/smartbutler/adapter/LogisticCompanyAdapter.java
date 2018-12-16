package com.butler.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.butler.smartbutler.R;
import com.butler.smartbutler.entity.LogisticCompany;

import java.util.List;

public class LogisticCompanyAdapter extends BaseAdapter {
    private List<LogisticCompany> mList;
    private Context mContext;

    public LogisticCompanyAdapter(Context pContext, List<LogisticCompany> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public LogisticCompany getItem(int position) {
        return mList.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.k_v_item, null);
        if (convertView != null) {
            TextView _TextView1 = convertView.findViewById(R.id.textView1);
            TextView _TextView2 = convertView.findViewById(R.id.textView2);

            _TextView1.setText(mList.get(position).getCom());
            _TextView2.setText(mList.get(position).getNo());
        }
        return convertView;

    }
}
