package com.butler.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.butler.smartbutler.MainActivity;
import com.butler.smartbutler.R;
import com.butler.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;
    //小圆点
    ImageView point1, point2, point3;
    //跳过
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);//返回按钮
        iv_back.setOnClickListener(this);

        viewPager = findViewById(R.id.mViewPager);
        view1 = View.inflate(this, R.layout.guide_page_one, null);
        view2 = View.inflate(this, R.layout.guide_page_two, null);
        view3 = View.inflate(this, R.layout.guide_page_three, null);
        view3.findViewById(R.id.btn_start).setOnClickListener(this);
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ((ViewPager) container).addView(mList.get(position));
                return mList.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                ((ViewPager) container).removeView(mList.get(position));
            }
        });

        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        setPointImg(true, false, false);
        iv_back.setVisibility(View.VISIBLE);
        //滑动效果
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                L.i("position:" + i);
                switch (i) {
                    case 0:
                        setPointImg(true, false, false);
                        break;
                    case 1:
                        setPointImg(false, true, false);
                        break;
                    case 2:
                        setPointImg(false, false, true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3) {
        if (isCheck1) {
            point1.setBackgroundResource(R.drawable.point_on);
        } else {
            point1.setBackgroundResource(R.drawable.point_off);
        }
        if (isCheck2) {
            point2.setBackgroundResource(R.drawable.point_on);
        } else {
            point2.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck3) {
            point3.setBackgroundResource(R.drawable.point_on);
        } else {
            point3.setBackgroundResource(R.drawable.point_off);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
            case R.id.iv_back:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
