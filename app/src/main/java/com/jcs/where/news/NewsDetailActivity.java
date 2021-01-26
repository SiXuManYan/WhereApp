package com.jcs.where.news;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.NewsDetailResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.model.NewsDetailModel;
import com.jcs.where.utils.GlideUtil;

import androidx.constraintlayout.widget.Group;

/**
 * 新闻详情页面（不是展示视频的页面）
 * create by zyf on 2021/1/24 11:58 下午
 */
public class NewsDetailActivity extends BaseActivity {
    private NewsDetailModel mModel;
    private ImageView mAuthorIcon;
    private TextView mNewsTitleTv, mAuthorNameTv, mNewsTimeTv;
    private View mToFollowView;
    private WebView mWebView;
    private Group mToFollowGroup;

    @Override
    protected void initView() {
        mNewsTitleTv = findViewById(R.id.newsTitleTv);
        mAuthorIcon = findViewById(R.id.authorIcon);
        mAuthorNameTv = findViewById(R.id.authorNameTv);
        mNewsTimeTv = findViewById(R.id.newsTimeTv);
        mToFollowView = findViewById(R.id.toFollowView);
        mToFollowGroup = findViewById(R.id.toFollowGroup);
        mWebView = findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());
        deployWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void deployWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    @Override
    protected void initData() {
        mModel = new NewsDetailModel();
        String newsId = getIntent().getStringExtra("newsId");
        showLoading();
        mModel.getNewsDetail(newsId, new BaseObserver<NewsDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(NewsDetailResponse response) {
                stopLoading();
                mToFollowGroup.setVisibility(View.VISIBLE);
                NewsDetailResponse.PublisherDTO publisher = response.getPublisher();
                GlideUtil.load(NewsDetailActivity.this, publisher.getAvatar(), mAuthorIcon);
                mNewsTitleTv.setText(response.getTitle());
                mNewsTimeTv.setText(response.getCreatedAt());
                mAuthorNameTv.setText(publisher.getNickname());
                Log.e("NewsDetailActivity", "onSuccess: " + response.getArticleLink());

                //使用WebView加载显示url
                mWebView.loadUrl(response.getArticleLink());
            }
        });

    }

    @Override
    protected void bindListener() {
        mToFollowView.setOnClickListener(this::onFollowedClicked);
    }

    private void onFollowedClicked(View view) {
        showLoading("去关注");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }
}
