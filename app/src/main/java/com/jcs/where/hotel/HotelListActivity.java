package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.bean.HotelTypeBean;
import com.jcs.where.hotel.fragment.HotelListFragment;
import com.jcs.where.hotel.tablayout.ColorClipTabLayout;
import com.jcs.where.manager.TokenManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.view.ToastUtils;

public class HotelListActivity extends BaseActivity {

    private static final String EXT_STARTDATE = "startDate";
    private static final String EXT_ENDDATE = "endDate";
    private static final String EXT_CITY = "city";
    private static final String EXT_CITYID = "cityId";
    private ColorClipTabLayout mTab;
    private ViewPager mViewPager;
    private TextView startDayTv, endDayTv, cityTv;

    public static void goTo(Context context, String startDate, String endDate, String city,String cityId) {
        Intent intent = new Intent(context, HotelListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXT_STARTDATE, startDate);
        intent.putExtra(EXT_ENDDATE, endDate);
        intent.putExtra(EXT_CITY, city);
        intent.putExtra(EXT_CITYID, cityId);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        initView();
        initData();
    }

    private void initView() {
        mTab = V.f(this, R.id.tab);
        mViewPager = V.f(this, R.id.viewPager);
        startDayTv = V.f(this, R.id.tv_startday);
        startDayTv.setText(getIntent().getStringExtra(EXT_STARTDATE).replace("月", "-").replace("日", ""));
        endDayTv = V.f(this, R.id.tv_endday);
        endDayTv.setText(getIntent().getStringExtra(EXT_ENDDATE).replace("月", "-").replace("日", ""));
        cityTv = V.f(this, R.id.tv_city);
        cityTv.setText(getIntent().getStringExtra(EXT_CITY));
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/types", null, "", TokenManager.get().getToken(HotelListActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<HotelTypeBean>>() {
                    }.getType();
                    List<HotelTypeBean> list = gson.fromJson(result, type);
                    initTab(list);
                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelListActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelListActivity.this, e.getMessage());
            }
        });
    }

    private void initTab(List<HotelTypeBean> list) {

        final List<String> titles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titles.add(list.get(i).getName());
        }

        mTab.setSelectedTabIndicatorHeight(0);
        final List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            fragments.add(HotelListFragment.newInstance(String.valueOf(list.get(i).getId()),getIntent().getStringExtra(EXT_CITYID)));
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        mTab.setupWithViewPager(mViewPager);

        mTab.setLastSelectedTabPosition(0);
        mTab.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(titles.size());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotellist;
    }

    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
    }
}
