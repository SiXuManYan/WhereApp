package com.jiechengsheng.city.government.fragment;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.viewpager.widget.ViewPager;

/**
 * create by zyf on 2020/12/31 11:34 PM
 */
class PageAnimation implements ViewPager.PageTransformer {
    final float SCALE_MAX = 0.9f;
    final float ALPHA_MAX = 0.5f;
    final float MAX_Z = 12;

    @Override
    public void transformPage(@NotNull View page, float position) {
        float scale = (position < 0)
                ? ((1 - SCALE_MAX) * position + 1)
                : ((SCALE_MAX - 1) * position + 1);
        float alpha = (position < 0)
                ? ((1 - ALPHA_MAX) * position + 1)
                : ((ALPHA_MAX - 1) * position + 1);
        //为了滑动过程中，page间距不变，这里做了处理
        if (position < 0) {
            page.setPivotX(page.getWidth());
        } else {
            page.setPivotX(0);
        }

        int pivotY = page.getHeight() / 2;
        page.setPivotY(pivotY);
        page.setScaleX(scale);
        page.setScaleY(scale);
//        page.setAlpha(Math.abs(alpha));

        // 阴影
        page.setElevation(MAX_Z * Math.abs(alpha));
    }
}
