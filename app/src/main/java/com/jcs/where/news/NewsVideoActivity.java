package com.jcs.where.news;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.R;
import com.jcs.where.api.response.NewsResponse;
import com.jcs.where.base.IntentEntry;
import com.jcs.where.news.adapter.MoreRecommendVideoAdapter;
import com.jcs.where.widget.JcsVideoPlayer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.Jzvd;

/**
 * 新闻视频详情页
 * create by zyf on 2021/1/27 11:02 下午
 */
public class NewsVideoActivity extends BaseNewsDetailActivity {
    private JcsVideoPlayer mVideoPlayer;
    private RecyclerView mMoreRecycler;
    private MoreRecommendVideoAdapter mMoreVideoAdapter;

    @Override
    protected void initView() {
        super.initView();
        mVideoPlayer = findViewById(R.id.newsVideoPlayer);
        mMoreRecycler = findViewById(R.id.moreRecommendRecycler);
        // 导航栏高度
        setMarginTopForStatusBar(mVideoPlayer);
        // 导航栏颜色
        setStatusBar();

        mMoreVideoAdapter = new MoreRecommendVideoAdapter();
        mMoreRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMoreRecycler.setAdapter(mMoreVideoAdapter);
    }

    @Override
    protected void dealDiff() {
        String videoLink = mNewsDetailResponse.getVideoLink();
        if (mVideoPlayer != null && videoLink != null && !videoLink.equals("")) {
            mVideoPlayer.setUp(videoLink, mNewsDetailResponse.getTitle());
            // 进入页面加载成功后自动播放
            mVideoPlayer.startVideo();
        }


    }

    @Override
    protected void bindListener() {
        super.bindListener();
        mMoreVideoAdapter.setOnItemClickListener(this::onMoreVideoClicked);
    }

    private void onMoreVideoClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        NewsResponse item = mMoreVideoAdapter.getItem(position);
        // 跳转到播放视频的详情页面
        toActivity(NewsVideoActivity.class, new IntentEntry("newsId", String.valueOf(item.getId())));
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.black_333333;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_video;
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
}
