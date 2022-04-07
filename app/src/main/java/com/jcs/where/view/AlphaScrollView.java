package com.jcs.where.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.widget.Toolbar;

public class AlphaScrollView extends ScrollView {
    public static final String TAG = "AlphaTitleScrollView";
    private int mSlop;
    private Toolbar toolbar;
    private ImageView headView;

    public AlphaScrollView(Context context) {
        super(context);
        init();
    }

    public AlphaScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphaScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSlop = 5;
    }

    public void findToorbarAndHeadPic(Toolbar toolbar, ImageView headView) {
        this.toolbar = toolbar;
        this.headView = headView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        float headHeight = headView.getMeasuredHeight() - toolbar.getMeasuredHeight();
        int alpha = (int) (((float) t / headHeight) * 255);
        if (alpha >= 255)
            alpha = 255;
        if (alpha <= mSlop)
            alpha = 0;
        toolbar.getBackground().setAlpha(alpha);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
