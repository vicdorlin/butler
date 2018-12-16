package com.butler.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.butler.smartbutler.R;
import com.butler.smartbutler.adapter.LogisticCompanyAdapter;
import com.butler.smartbutler.adapter.LogisticViewListAdapter;
import com.butler.smartbutler.entity.JuheLogisticResultData;
import com.butler.smartbutler.entity.LogisticCompany;
import com.butler.smartbutler.entity.LogisticData;
import com.butler.smartbutler.utils.StaticClass;
import com.butler.smartbutler.utils.ToastUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogisticActivity extends BaseActivity implements View.OnClickListener {
    private Spinner spinner;
    private List<LogisticCompany> companies;
    private EditText etOrderId;
    private Button btnQuery;
    private LogisticCompanyAdapter logisticCompanyAdapter;
    private List<LogisticData> mList = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic);
        initView();
    }

    private void initView() {
        spinner = findViewById(R.id.company_list);
        companies = new ArrayList<>();
//        companies.add(new LogisticCompany("顺丰", "sf"));
//        companies.add(new LogisticCompany("申通", "sto"));
        RxVolley.get(StaticClass.POLYMERIZE_EXPRESS_COM_QUERY_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                try {
                    JSON json = JSON.parseObject(t);
                    JuheLogisticResultData<List<LogisticCompany>> resultData = JSON.toJavaObject(json, JuheLogisticResultData.class);
                    companies = JSONArray.parseArray(JSON.toJSONString(resultData.getResult()), LogisticCompany.class);
                    logisticCompanyAdapter = new LogisticCompanyAdapter(getApplicationContext(), companies);
                    spinner.setAdapter(logisticCompanyAdapter);
                } catch (Exception e) {
                    ToastUtil.showShortToastCenter("出了点问题");
                }
            }
        });

        etOrderId = findViewById(R.id.order_id);
        btnQuery = findViewById(R.id.btn_getLogistic);
        btnQuery.setOnClickListener(this);
        mListView = findViewById(R.id.lv_logistic);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getLogistic:
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求数据（Json）
                 * 4.解析Json
                 * 5.listview适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */
                String orderId = etOrderId.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    ToastUtil.showShortToastCenter("请输入订单号");
                }
                LogisticCompany selectedItem = (LogisticCompany) spinner.getSelectedItem();
                String url = StaticClass.POLYMERIZE_URL.replace("{com}", selectedItem.getNo()).replace("{no}", orderId);
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        JSONObject jsonObject = JSON.parseObject(t);
                        JSONObject jsonResult = jsonObject.getJSONObject("result");
                        JSONArray jsonArray = jsonResult.getJSONArray("list");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject json = (JSONObject) jsonArray.get(i);
                            LogisticData data = new LogisticData();
                            data.setRemark(json.getString("remark"));
                            data.setZone(json.getString("zone"));
                            data.setDatetime(json.getString("datetime"));
                            mList.add(data);
                        }
                        //倒序
                        Collections.reverse(mList);
                        LogisticViewListAdapter adapter = new LogisticViewListAdapter(getApplicationContext(), mList);
                        mListView.setAdapter(adapter);
                    }
                });
                break;
        }
    }
}
