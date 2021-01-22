package com.jcs.where.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.jzvd.Jzvd;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.NewsChannelResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.NewsViewPagerAdapter;
import com.jcs.where.news.fragment.NewsFragment;
import com.jcs.where.news.model.NewsAtyModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻页
 * author : zyf
 * date   : 2021/1/6-22:54
 */

public class NewsActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsViewPagerAdapter mNewsViewPagerAdapter;
    private NewsAtyModel mModel;
    private View mAddTabView;
    private List<NewsChannelResponse> mFollowChannels;
    private List<NewsChannelResponse> mAllChannels;
    private List<NewsChannelResponse> mTabs;

    /**
     * 新闻Fragment集合
     */
    private List<NewsFragment> mNewsFragments;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mAddTabView = findViewById(R.id.addTabView);
    }

    @Override
    protected void initData() {
        mModel = new NewsAtyModel();
        mFollowChannels = new ArrayList<>();
        mTabs = new ArrayList<>();
        mNewsFragments = new ArrayList<>();

        mModel.getNewsTabs(new BaseObserver<List<NewsChannelResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onNext(@NotNull List<NewsChannelResponse> newsChannelResponses) {
                mAllChannels = newsChannelResponses;
                // 拆分出已经关注的新闻频道
                getFollowedChannels();
                NewsChannelResponse follow = new NewsChannelResponse();
                follow.setName(getString(R.string.news_follow));
                follow.setId(-1);

                NewsChannelResponse recommend = new NewsChannelResponse();
                recommend.setName(getString(R.string.news_recommend));
                recommend.setId(-2);

                mTabs.add(follow);
                mTabs.add(recommend);

                mTabs.addAll(mFollowChannels);
                // 添加一个占位的 tab，用于滑动效果
                mTabs.add(new NewsChannelResponse(""));

                for (int i = 0; i < mTabs.size(); i++) {
                    NewsChannelResponse newsChannelResponse = mTabs.get(i);
                    mTabLayout.addTab(mTabLayout.newTab().setText(newsChannelResponse.getName()));
                    mNewsFragments.add(NewsFragment.newInstance(newsChannelResponse, i == 0));
                }

                mNewsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                mNewsViewPagerAdapter.setNewsFragments(mNewsFragments);
                mNewsViewPagerAdapter.setTabCategories(mTabs);
                mViewPager.setAdapter(mNewsViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(mNewsViewPagerAdapter.getCount());

                // 设置最后一个占位的 tab 不可点击
                notClickLastTab();
            }
        });
    }

    private void getFollowedChannels() {
        mFollowChannels = new ArrayList<>();
        int size = mAllChannels.size();
        for (int i = 0; i < size; i++) {
            NewsChannelResponse newsChannelResponse = mAllChannels.get(i);
            if (newsChannelResponse.getFollowStatus() == 1) {
                mFollowChannels.add(newsChannelResponse);
            }
        }
    }

    private void notClickLastTab() {
        LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
        int childCount = tabStrip.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null && i == childCount - 1) {
                tabView.setClickable(false);
            }
        }
    }

    @Override
    protected void bindListener() {
        mAddTabView.setOnClickListener(this::onAddTabClicked);
    }

    private void onAddTabClicked(View view) {
        // 弹出选择新闻分类的页面
        Intent to = new Intent(this, SelectNewsChannelActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(to,bundle);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
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
