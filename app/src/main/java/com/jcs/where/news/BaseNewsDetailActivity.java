package com.jcs.where.news;

import android.annotation.SuppressLint;
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
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.news.model.NewsDetailModel;
import com.jcs.where.utils.GlideUtil;

import androidx.constraintlayout.widget.Group;

/**
 * create by zyf on 2021/1/27 11:39 下午
 */
public abstract class BaseNewsDetailActivity extends BaseActivity {
    protected NewsDetailModel mModel;
    protected ImageView mAuthorIcon, mToFollowIv;
    protected TextView mNewsTitleTv, mAuthorNameTv, mNewsTimeTv, mToFollowTv;
    protected WebView mWebView;
    protected Group mToFollowGroup;
    protected View mToFollowView;
    protected NewsDetailResponse mNewsDetailResponse;
    protected final int STATUS_UNFOLLOWED = 1;
    protected final int STATUS_FOLLOWED = 2;
    protected Integer mFollowStatus;

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
                GlideUtil.load(BaseNewsDetailActivity.this, publisher.getAvatar(), mAuthorIcon);
                mNewsTitleTv.setText(response.getTitle());
                mNewsTimeTv.setText(response.getCreatedAt());
                mAuthorNameTv.setText(publisher.getNickname());
                //使用WebView加载显示url
                Log.e("BaseNewsDetailActivity", "onSuccess: " + response.getTitle());
                mWebView.loadUrl(mNewsDetailResponse.getArticleLink());
                dealDiff();
            }
        });
    }

    protected abstract void dealDiff();

    @Override
    protected void bindListener() {
        mToFollowView.setOnClickListener(this::onFollowedClicked);
    }

    private void onFollowedClicked(View view) {
        Integer publisherId = mNewsDetailResponse.getPublisher().getId();
        showLoading();
        if (mFollowStatus == STATUS_FOLLOWED) {
            toCancelFollow(publisherId);
        } else {
            toFollow(publisherId);
        }

    }

    protected void toCancelFollow(int id) {
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

    protected void toFollow(int id) {
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

    protected void showToFollow() {
        mToFollowTv.setText(getString(R.string.news_follow));
        mToFollowTv.setTextColor(getColor(R.color.white));
        mToFollowIv.setImageResource(R.mipmap.ic_add_white);
        mToFollowView.setBackgroundResource(R.drawable.shape_489bf8_radius_4);
    }

    protected void showFollowed() {
        mToFollowTv.setText(getString(R.string.news_followed));
        mToFollowTv.setTextColor(getColor(R.color.blue_489b58));
        mToFollowIv.setImageResource(R.mipmap.ic_right_checked_blue);
        mToFollowView.setBackgroundResource(R.drawable.shape_white_radius_4_stroke_5a9dfe_1);
    }
}
