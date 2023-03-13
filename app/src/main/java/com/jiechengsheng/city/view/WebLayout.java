package com.jiechengsheng.city.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jiechengsheng.city.R;
import com.just.agentweb.IWebLayout;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by cenxiaozhong on 2017/7/1.
 * source code  https://github.com/Justson/AgentWeb
 */

public class WebLayout implements IWebLayout {

    private final TwinklingRefreshLayout mTwinklingRefreshLayout;
    private final Activity mActivity;
    private WebView mWebView = null;

    public WebLayout(Activity activity) {
        this.mActivity = activity;
        mTwinklingRefreshLayout = (TwinklingRefreshLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_twk_web, null);
        mTwinklingRefreshLayout.setPureScrollModeOn();
        mWebView = (WebView) mTwinklingRefreshLayout.findViewById(R.id.webView);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mTwinklingRefreshLayout;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }


}
