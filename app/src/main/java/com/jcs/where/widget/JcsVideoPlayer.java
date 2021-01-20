package com.jcs.where.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jcs.where.R;

import cn.jzvd.JzvdStd;

/**
 * create by zyf on 2021/1/19 11:02 下午
 */
public class JcsVideoPlayer extends JzvdStd {
    public JcsVideoPlayer(Context context) {
        super(context);
        jcsInit();
    }

    public JcsVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        jcsInit();
    }

    private void jcsInit() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.wight_jzvd_no_title;
    }
}
