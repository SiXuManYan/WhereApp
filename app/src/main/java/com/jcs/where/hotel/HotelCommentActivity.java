package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.jcs.where.R;
import com.jcs.where.api.HttpUtils;
import com.jcs.where.bean.ErrorBean;
import com.jcs.where.hotel.fragment.CommentListFragment;
import com.jcs.where.hotel.tablayout.TabLayout;
import com.jcs.where.manager.TokenManager;
import com.jcs.where.utils.StatusBarUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.tton.android.base.app.activity.BaseActivity;
import co.tton.android.base.utils.V;
import co.tton.android.base.utils.ValueUtils;
import co.tton.android.base.view.ToastUtils;

public class HotelCommentActivity extends BaseActivity {

    private static final String EXT_ID = "id";
    private TabLayout mTablayout;
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public static void goTo(Context context, int id) {
        Intent intent = new Intent(context, HotelCommentActivity.class);
        intent.putExtra(EXT_ID, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ValueUtils.getColor(this, R.color.white), 0);
        StatusBarUtils.setStatusBarLightMode(getWindow());
        initView();
    }

    private void initView() {
        mTablayout = V.f(this, R.id.main_tab);
        viewPager = V.f(this, R.id.viewpager);
        titles = new ArrayList<>();
        initData();
    }

    private void initData() {
        showLoading();
        HttpUtils.doHttpReqeust("GET", "hotelapi/v1/hotel/" + getIntent().getIntExtra(EXT_ID, 0) + "/comment/nums", null, "", TokenManager.get().getToken(HotelCommentActivity.this), new HttpUtils.StringCallback() {
            @Override
            public void onSuccess(int code, String result) {
                stopLoading();
                if (code == 200) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> list = gson.fromJson(result, type);
                    // Center camera on Adelaide
                    titles.add("全部(" + list.get(0) + ")");
                    titles.add("晒图(" + list.get(1) + ")");
                    titles.add("低分(" + list.get(2) + ")");
                    titles.add("最新(" + list.get(3) + ")");
                    fragments = new ArrayList<>();
                    fragments.add(CommentListFragment.newInstance(String.valueOf(getIntent().getIntExtra(EXT_ID, 0)), "0"));
                    fragments.add(CommentListFragment.newInstance(String.valueOf(getIntent().getIntExtra(EXT_ID, 0)), "1"));
                    fragments.add(CommentListFragment.newInstance(String.valueOf(getIntent().getIntExtra(EXT_ID, 0)), "2"));
                    fragments.add(CommentListFragment.newInstance(String.valueOf(getIntent().getIntExtra(EXT_ID, 0)), "3"));
                    viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return fragments.get(position);
                        }

                        @Override
                        public int getCount() {
                            return fragments.size();
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return titles.get(position);
                        }
                    });

                    viewPager.setOffscreenPageLimit(fragments.size() - 1);
                    mTablayout.setupWithViewPager(viewPager);

                } else {
                    ErrorBean errorBean = new Gson().fromJson(result, ErrorBean.class);
                    ToastUtils.showLong(HotelCommentActivity.this, errorBean.message);
                }
            }

            @Override
            public void onFaileure(int code, Exception e) {
                stopLoading();
                ToastUtils.showLong(HotelCommentActivity.this, e.getMessage());
            }
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotelcomment;
    }
}
