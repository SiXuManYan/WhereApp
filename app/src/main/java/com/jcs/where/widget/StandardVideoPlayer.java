package com.jcs.where.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jcs.where.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

/**
 * Created by Wangsw  2021/5/31 17:06.
 */
public class StandardVideoPlayer extends StandardGSYVideoPlayer {


    public StandardVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public StandardVideoPlayer(Context context) {
        super(context);
    }

    public StandardVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_video_layout_standard;
    }
}
