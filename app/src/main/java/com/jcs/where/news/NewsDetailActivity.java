package com.jcs.where.news;

import android.annotation.SuppressLint;
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
import com.jcs.where.api.response.SuccessResponse;
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
    private ImageView mAuthorIcon, mToFollowIv;
    private TextView mNewsTitleTv, mAuthorNameTv, mNewsTimeTv, mToFollowTv;
    private View mToFollowView;
    private WebView mWebView;
    private Group mToFollowGroup;
    private NewsDetailResponse mNewsDetailResponse;
    private final int STATUS_UNFOLLOWED = 1;
    private final int STATUS_FOLLOWED = 2;
    private Integer mFollowStatus;

    @Override
    protected void initView() {
        mNewsTitleTv = findViewById(R.id.newsTitleTv);
        mAuthorIcon = findViewById(R.id.authorIcon);
        mAuthorNameTv = findViewById(R.id.authorNameTv);
        mNewsTimeTv = findViewById(R.id.newsTimeTv);
        mToFollowView = findViewById(R.id.toFollowView);
        mToFollowTv = findViewById(R.id.toFollowTv);
        mToFollowIv = findViewById(R.id.toFollowIcon);
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
                mNewsDetailResponse = response;
                mToFollowGroup.setVisibility(View.VISIBLE);
                NewsDetailResponse.PublisherDTO publisher = response.getPublisher();
                 mFollowStatus = response.getFollowStatus();
                if (mFollowStatus == STATUS_FOLLOWED) {
                    showFollowed();
                } else {
                    showToFollow();
                }
                GlideUtil.load(NewsDetailActivity.this, publisher.getAvatar(), mAuthorIcon);
                mNewsTitleTv.setText(response.getTitle());
                mNewsTimeTv.setText(response.getCreatedAt());
                mAuthorNameTv.setText(publisher.getNickname());
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
        mFollowStatus = mNewsDetailResponse.getFollowStatus();
        Integer publisherId = mNewsDetailResponse.getPublisher().getId();
        showLoading();
        if (mFollowStatus == STATUS_FOLLOWED) {
            toCancelFollow(publisherId);
        } else {
            toFollow(publisherId);
        }

    }

    private void toCancelFollow(int id) {
        mModel.delFollowPublish(id, new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                stopLoading();
                showToFollow();
                mFollowStatus = STATUS_UNFOLLOWED;
            }
        });
    }

    private void toFollow(int id) {
        mModel.postFollowPublish(id, new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                stopLoading();
                showFollowed();
                mFollowStatus = STATUS_FOLLOWED;
            }
        });
    }

    private void showToFollow() {
        mToFollowTv.setText(getString(R.string.news_follow));
        mToFollowTv.setTextColor(getColor(R.color.white));
        mToFollowIv.setImageResource(R.mipmap.ic_add_white);
        mToFollowView.setBackgroundResource(R.drawable.shape_489bf8_radius_4);
    }

    private void showFollowed() {
        mToFollowTv.setText(getString(R.string.news_followed));
        mToFollowTv.setTextColor(getColor(R.color.blue_489b58));
        mToFollowIv.setImageResource(R.mipmap.ic_right_checked_blue);
        mToFollowView.setBackgroundResource(R.drawable.shape_white_radius_4_stroke_5a9dfe_1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }
}
