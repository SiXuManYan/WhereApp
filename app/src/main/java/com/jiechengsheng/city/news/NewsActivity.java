package com.jiechengsheng.city.news;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.google.android.material.tabs.TabLayout;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.response.NewsChannelResponse;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.news.adapter.NewsViewPagerAdapter;
import com.jiechengsheng.city.news.dto.FollowAndUnfollowDTO;
import com.jiechengsheng.city.news.fragment.NewsFragment;
import com.jiechengsheng.city.news.model.NewsAtyModel;
import com.jiechengsheng.city.search.SearchActivity;
import com.jiechengsheng.city.search.tag.SearchTag;
import com.jiechengsheng.city.utils.RequestResultCode;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 新闻页
 * author : zyf
 * date   : 2021/1/6-22:54
 */

public class NewsActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsViewPagerAdapter mPagerAdapter;
    private NewsAtyModel mModel;
    private ImageView mAddTabView;
    private List<NewsChannelResponse> mFollowChannels;
    private List<NewsChannelResponse> mMoreChannels;
    private List<NewsChannelResponse> mAllChannels;
    private List<NewsChannelResponse> mTabs;

    /**
     * 新闻Fragment集合
     */
    private List<NewsFragment> mNewsFragments;

    /**
     * 首先要显示的Tab对应新闻列表的索引
     * 默认情况是第一个
     * 在新闻频道关注页面点击了已关注的频道后，这个索引会更改为点击的已关注频道对应的索引
     * <p>
     * 1 为默认展示推荐的新闻列表
     */
    private int mFirstVisibleTabPosition = 1;

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mAddTabView = findViewById(R.id.addTabView);
    }

    @Override
    protected void initData() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white_FEFEFE));
        mModel = new NewsAtyModel();
        mFollowChannels = new ArrayList<>();
        mMoreChannels = new ArrayList<>();
        mTabs = new ArrayList<>();
        mNewsFragments = new ArrayList<>();

        mModel.getNewsTabs(new BaseObserver<List<NewsChannelResponse>>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {

            }

            @Override
            public void onSuccess(@NotNull List<NewsChannelResponse> newsChannelResponses) {
                mAllChannels = newsChannelResponses;

                // 拆分出已经关注的和未关注的新闻频道
                getFollowedAndMoreChannels();

                // 本地添加关注频道
                NewsChannelResponse follow = new NewsChannelResponse();
                follow.setName(getString(R.string.news_follow));
                follow.setId(-2);
                follow.setFollowStatus(1);
                follow.setEditable(false);

                // 本地添加推荐频道
                NewsChannelResponse recommend = new NewsChannelResponse();
                recommend.setName(getString(R.string.news_recommend));
                recommend.setId(-1);
                recommend.setFollowStatus(1);
                recommend.setEditable(false);

                mTabs.add(follow);
                mTabs.add(recommend);

                mTabs.addAll(mFollowChannels);

                // 根据 mTabs 的数据配制 TabLayout 和 ViewPager
                deployTabAndViewPager();
            }
        });
    }

    /**
     * 根据 mTabs 的数据配制 TabLayout 和 ViewPager
     */
    private void deployTabAndViewPager() {

        for (int i = 0; i < mTabs.size(); i++) {
            NewsChannelResponse response = mTabs.get(i);
            mTabLayout.addTab(mTabLayout.newTab().setText(response.getName()));
            mNewsFragments.add(NewsFragment.newInstance(response, i == mFirstVisibleTabPosition));
        }

        mPagerAdapter = new NewsViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mPagerAdapter.setNewsFragments(mNewsFragments);
        mPagerAdapter.setTabCategories(mTabs);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        if (mFirstVisibleTabPosition != 0) {
            mViewPager.setCurrentItem(mFirstVisibleTabPosition);
        }
    }

    private void updateFollow() {
        String followIds = getChannelIds(mFollowChannels);
        String moreIds = getChannelIds(mMoreChannels);
        mModel.updateFollow(followIds, moreIds, new BaseObserver<NewsAtyModel.UpdateFollowZipResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
            }

            @Override
            public void onSuccess(@NotNull NewsAtyModel.UpdateFollowZipResponse updateFollowZipResponse) {

                // 清空缓存数据
                mNewsFragments.clear();
                mTabLayout.removeAllTabs();

                deployTabAndViewPager();
            }
        });
    }

    private String getChannelIds(List<NewsChannelResponse> channels) {
        int size = channels.size();
        List<Integer> channelIds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NewsChannelResponse channel = channels.get(i);
            channelIds.add(channel.getId());
        }
        return channelIds.toString();
    }

    /**
     * 从全部频道中拆分出已关注频道和未关注频道
     */
    private void getFollowedAndMoreChannels() {
        mFollowChannels = new ArrayList<>();
        int size = mAllChannels.size();
        for (int i = 0; i < size; i++) {
            NewsChannelResponse newsChannelResponse = mAllChannels.get(i);
            if (newsChannelResponse.getFollowStatus() == 1) {
                mFollowChannels.add(newsChannelResponse);
            }

            if (newsChannelResponse.getFollowStatus() == 2) {
                mMoreChannels.add(newsChannelResponse);
            }
        }
    }

    @Override
    protected void bindListener() {
        mAddTabView.setOnClickListener(this::onAddTabClicked);
        mJcsTitle.setFirstRightIvClickListener(this::onSearchIconClicked);
    }

    /**
     * 搜索
     */
    private void onSearchIconClicked(View view) {
        SearchActivity.goTo(this, "", SearchTag.NEWS, RequestResultCode.REQUEST_NEWS_TO_SEARCH);
    }

    /**
     * 点击了添加icon，跳转到选择新闻频道的页面
     */
    private void onAddTabClicked(View view) {
        Intent to = new Intent(this, SelectNewsChannelActivity.class);

        to.putExtra(SelectNewsChannelActivity.K_NEWS_FOLLOW_CHANNEL, (Serializable) mTabs);
        to.putExtra(SelectNewsChannelActivity.K_NEWS_MORE_CHANNEL, (Serializable) mMoreChannels);

        startActivityForResult(to, RequestResultCode.RESULT_FOLLOW_TO_NEWS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RequestResultCode.RESULT_FOLLOW_TO_NEWS) {

            if (data != null) {
                Serializable serializable = data.getSerializableExtra("dto");
                if (serializable instanceof FollowAndUnfollowDTO) {
                    FollowAndUnfollowDTO dto = (FollowAndUnfollowDTO) serializable;
                    List<NewsChannelResponse> followed = dto.followed;
                    mTabs = followed;
                    mFollowChannels = new ArrayList<>(followed.subList(2, followed.size()));
                    mMoreChannels = dto.more;
                    // 添加一个占位的 tab，用于滑动效果
//                    mTabs.add(new NewsChannelResponse(""));
                    mFirstVisibleTabPosition = dto.showPosition;

                    // 更新关注和非关注频道
                    updateFollow();
                }
            }

        }
    }

    /**
     * 停止播放视频
     */
    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * 停止播放视频
     */
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news;
    }
}
