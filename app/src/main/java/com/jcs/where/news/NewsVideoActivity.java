package com.jcs.where.news;

import com.jcs.where.R;
import com.jcs.where.widget.JcsVideoPlayer;

import cn.jzvd.Jzvd;

/**
 * create by zyf on 2021/1/27 11:02 下午
 */
public class NewsVideoActivity extends BaseNewsDetailActivity {
    private JcsVideoPlayer mVideoPlayer;


    @Override
    protected void initView() {
        super.initView();
        mVideoPlayer = findViewById(R.id.newsVideoPlayer);
        setMarginTopForStatusBar(mVideoPlayer);
        setStatusBar();
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
