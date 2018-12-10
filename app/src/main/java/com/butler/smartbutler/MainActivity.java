package com.butler.smartbutler;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.butler.smartbutler.fragment.ButlerFragment;
import com.butler.smartbutler.fragment.GirlFragment;
import com.butler.smartbutler.fragment.UserFragment;
import com.butler.smartbutler.fragment.WechatFragment;
import com.butler.smartbutler.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitle;
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Added in API level 21
        getSupportActionBar().setElevation(0);

        initData();
        initView();
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        Resources resources = getResources();
        mTitle.add(resources.getString(R.string.tab_1));
        mTitle.add(resources.getString(R.string.tab_2));
        mTitle.add(resources.getString(R.string.tab_3));
        mTitle.add(resources.getString(R.string.tab_4));

        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());

    }

    //初始化View
    private void initView() {
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        fab_setting.hide();
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //mViewPager的滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                Log.i("TAG","position" + i);
                if (i == 0) {
                    fab_setting.hide();
                } else {
                    fab_setting.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int i) {
                return mFragment.get(i);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
