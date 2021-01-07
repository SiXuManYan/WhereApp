package com.jcs.where.news;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.NewsViewPagerAdapter;
import com.jcs.where.news.fragment.NewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * author : hwd
 * date   : 2021/1/6-22:54
 */

public class NewsActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsViewPagerAdapter mNewsViewPagerAdapter;
    private ArrayList<String> mTitleList;
    /**
     * 新闻Fragment集合
     */
    private List<NewFragment> newFragments;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        mTitleList = new ArrayList<>();
        newFragments = new ArrayList<>();
        mTitleList.add("关注");
        mTitleList.add("推荐");
        mTitleList.add("巴丹");
        mTitleList.add("热点");
        mTitleList.add("抗击肺炎");
        mTitleList.add("视频");
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
            newFragments.add(NewFragment.newInstance());
        }
        mNewsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mNewsViewPagerAdapter.setNewsFragments(newFragments);
        mNewsViewPagerAdapter.setTabCategories(mTitleList);
        mViewPager.setAdapter(mNewsViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mNewsViewPagerAdapter.getCount());
    }

    @Override
    protected void bindListener() {
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                View customView = tab.getCustomView();
//                if (customView == null) {
//                    tab.setCustomView(R.layout.tab_normal_only_text);
//                }
//                TextView textView = tab.getCustomView().findViewById(R.id.tabTitle);
//                textView.setTextAppearance( R.style.TabLayoutTextSelected);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                View customView = tab.getCustomView();
//                if (customView == null) {
//                    tab.setCustomView(R.layout.tab_normal_only_text);
//                }
//                TextView textView = tab.getCustomView().findViewById(R.id.tabTitle);
//                textView.setTextAppearance( R.style.TabLayoutTextUnSelected);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }
}
