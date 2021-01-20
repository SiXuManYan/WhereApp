package com.jcs.where.news;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.jzvd.Jzvd;

import com.google.android.material.tabs.TabLayout;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.NewsTabResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.adapter.NewsViewPagerAdapter;
import com.jcs.where.news.fragment.NewsFragment;
import com.jcs.where.news.model.NewsAtyModel;

import org.jetbrains.annotations.NotNull;

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
    private List<NewsTabResponse> mTabs;
    private NewsAtyModel mModel;
    /**
     * 新闻Fragment集合
     */
    private List<NewsFragment> mNewsFragments;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void initData() {
        mModel = new NewsAtyModel();
        mTabs = new ArrayList<>();
        mNewsFragments = new ArrayList<>();

        mModel.getNewsTabs(new BaseObserver<List<NewsTabResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onNext(@NotNull List<NewsTabResponse> newsTabResponses) {
                NewsTabResponse follow = new NewsTabResponse();
                follow.setName(getString(R.string.news_follow));
                follow.setId(-1);

                NewsTabResponse recommend = new NewsTabResponse();
                recommend.setName(getString(R.string.news_recommend));
                recommend.setId(-2);

                mTabs.add(follow);
                mTabs.add(recommend);
                mTabs.addAll(newsTabResponses);

                for (int i = 0; i < mTabs.size(); i++) {
                    NewsTabResponse newsTabResponse = mTabs.get(i);
                    mTabLayout.addTab(mTabLayout.newTab().setText(newsTabResponse.getName()));
                    mNewsFragments.add(NewsFragment.newInstance(newsTabResponse, i == 0));
                }

                mNewsViewPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(),
                        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                mNewsViewPagerAdapter.setNewsFragments(mNewsFragments);
                mNewsViewPagerAdapter.setTabCategories(mTabs);
                mViewPager.setAdapter(mNewsViewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setOffscreenPageLimit(mNewsViewPagerAdapter.getCount());
            }
        });
    }

    @Override
    protected void bindListener() {
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
