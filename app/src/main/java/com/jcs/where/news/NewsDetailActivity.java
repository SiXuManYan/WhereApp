package com.jcs.where.news;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jcs.where.R;

/**
 * 新闻详情页面（不是展示视频的页面）
 * create by zyf on 2021/1/24 11:58 下午
 */
public class NewsDetailActivity extends BaseNewsDetailActivity {

    private WebView mWebView;

    @Override
    protected void initView() {
        super.initView();
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
    protected void dealDiff() {
        //使用WebView加载显示url
        mWebView.loadUrl(mNewsDetailResponse.getArticleLink());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_detail;
    }
}
