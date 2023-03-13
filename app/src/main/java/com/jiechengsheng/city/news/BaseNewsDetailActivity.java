package com.jiechengsheng.city.news;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.BaseObserver;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.api.response.NewsDetailResponse;
import com.jiechengsheng.city.api.response.SuccessResponse;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.frames.common.Html5Url;
import com.jiechengsheng.city.news.model.NewsDetailModel;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.utils.GlideUtil;
import com.jiechengsheng.city.utils.MobUtil;
import com.jiechengsheng.city.widget.JcsTitle;

/**
 * create by zyf on 2021/1/27 11:39 下午
 */
public abstract class BaseNewsDetailActivity extends BaseActivity {
    protected NewsDetailModel mModel;
    protected ImageView mAuthorIcon;
    protected TextView mNewsTitleTv, mAuthorNameTv, mNewsTimeTv, mToFollowTv;
    protected WebView mWebView;
    protected JcsTitle jcsTitle;
    protected NewsDetailResponse mNewsDetailResponse;
    protected final int STATUS_UNFOLLOWED_UNCOLLECTED = 1;
    protected final int STATUS_FOLLOWED_COLLECTED = 2;
    protected Integer mFollowStatus;
    protected String mNewsId;

    @Override
    protected void initView() {
        mNewsTitleTv = findViewById(R.id.newsTitleTv);
        mAuthorIcon = findViewById(R.id.authorIcon);
        mAuthorNameTv = findViewById(R.id.authorNameTv);
        mNewsTimeTv = findViewById(R.id.newsTimeTv);
        mToFollowTv = findViewById(R.id.toFollowTv);
        mWebView = findViewById(R.id.webView);
        jcsTitle = findViewById(R.id.jcsTitle);
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
        mNewsId = getIntent().getStringExtra(Constant.PARAM_NEWS_ID);
        mModel.getNewsDetail(mNewsId, new BaseObserver<NewsDetailResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
            }

            @Override
            protected void onSuccess(NewsDetailResponse response) {
                mNewsDetailResponse = response;
                NewsDetailResponse.PublisherDTO publisher = response.getPublisher();
                mFollowStatus = response.getFollowStatus();
                // 根据收藏状态设置 JcsTitle 右侧第二个 icon
                if (mNewsDetailResponse.getCollectStatus() == STATUS_FOLLOWED_COLLECTED) {
                    showCollected();
                } else {
                    showUncollected();
                }
                if (mFollowStatus == STATUS_FOLLOWED_COLLECTED) {
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
        mToFollowTv.setOnClickListener(this::onFollowedClicked);


        mJcsTitle.setSecondRightIvClickListener(this::onCollectClicked);
        mJcsTitle.setFirstRightIvClickListener(v -> {
            String url = String.format(Html5Url.SHARE_FACEBOOK, Html5Url.MODEL_NEWS, mNewsId) + "&extra=news_video";
            MobUtil.shareFacebookWebPage(url, this);
        });
    }

    private void onCollectClicked(View view) {
        // 如果已收藏
        if (mNewsDetailResponse.getCollectStatus() == STATUS_FOLLOWED_COLLECTED) {
            mModel.delCollectNews(String.valueOf(mNewsDetailResponse.getId()), new BaseObserver<SuccessResponse>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                }

                @Override
                protected void onSuccess(SuccessResponse response) {
                    mNewsDetailResponse.setCollectStatus(STATUS_UNFOLLOWED_UNCOLLECTED);
                    showUncollected();
                }
            });
        }
        // 如果未收藏
        if (mNewsDetailResponse.getCollectStatus() == STATUS_UNFOLLOWED_UNCOLLECTED) {
            mModel.postCollectNews(String.valueOf(mNewsDetailResponse.getId()), new BaseObserver<SuccessResponse>() {
                @Override
                protected void onError(ErrorResponse errorResponse) {
                }

                @Override
                protected void onSuccess(SuccessResponse response) {
                    mNewsDetailResponse.setCollectStatus(STATUS_FOLLOWED_COLLECTED);
                    showCollected();
                }
            });
        }
    }

    private void onFollowedClicked(View view) {
        Integer publisherId = mNewsDetailResponse.getPublisher().getId();
        if (mFollowStatus == STATUS_FOLLOWED_COLLECTED) {
            toCancelFollow(publisherId);
        } else {
            toFollow(publisherId);
        }

    }

    protected void toCancelFollow(int id) {
        mModel.delFollowPublish(id, new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                showToFollow();
                mFollowStatus = STATUS_UNFOLLOWED_UNCOLLECTED;
            }
        });
    }

    protected void toFollow(int id) {
        mModel.postFollowPublish(id, new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                showFollowed();
                mFollowStatus = STATUS_FOLLOWED_COLLECTED;
            }
        });
    }

    protected void showCollected() {
        mJcsTitle.setSecondRightIcon(R.mipmap.ic_like_red);
    }

    protected void showUncollected() {
        mJcsTitle.setSecondRightIcon(getUncollectedIcon());
    }

    protected abstract int getUncollectedIcon();

    protected void showToFollow() {
        mToFollowTv.setText(getString(R.string.news_follow));
        mToFollowTv.setTextColor(getColor(R.color.white));
        mToFollowTv.setBackgroundResource(R.drawable.shape_blue_radius_16);
    }

    protected void showFollowed() {
        mToFollowTv.setText(getString(R.string.news_followed));
        mToFollowTv.setTextColor(getColor(R.color.blue_377BFF));
        mToFollowTv.setBackgroundResource(R.drawable.stock_blue_radius_16);
    }
}
