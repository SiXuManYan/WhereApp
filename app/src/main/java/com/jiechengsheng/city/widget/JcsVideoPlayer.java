package com.jiechengsheng.city.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jiechengsheng.city.R;

import cn.jzvd.JzvdStd;

/**
 * create by zyf on 2021/1/19 11:02 下午
 */
public class JcsVideoPlayer extends JzvdStd {
    private boolean mHasSetUp = false;

    public JcsVideoPlayer(Context context) {
        super(context);
        jcsInit();
    }

    public JcsVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        jcsInit();
    }

    @Override
    public void setUp(String url, String title) {
        super.setUp(url, title);
        mHasSetUp = true;
    }

    private void jcsInit() {

    }

    @Override
    public void startVideo() {
        if (mHasSetUp) {
            super.startVideo();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.wight_jzvd_no_title;
    }
}
