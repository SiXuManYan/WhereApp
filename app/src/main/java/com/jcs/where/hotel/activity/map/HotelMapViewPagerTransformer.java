package com.jcs.where.hotel.activity.map;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Wangsw  2021/3/10 11:47.
 */
public class HotelMapViewPagerTransformer implements ViewPager.PageTransformer {

    public final float SCALE_MAX = 0.9f;
    public final float ALPHA_MAX = 0.9f;


    @Override
    public void transformPage(@NonNull View page, float position) {
        float scale = (position < 0)
                ? ((1 - SCALE_MAX) * position + 1)
                : ((SCALE_MAX - 1) * position + 1);
        float alpha = (position < 0)
                ? ((1 - ALPHA_MAX) * position + 1)
                : ((ALPHA_MAX - 1) * position + 1);
        // 滑动过程中，page间距不变
        if (position < 0) {
            page.setPivotX( page.getWidth());
            page.setPivotY( page.getHeight() / 2);
        } else {
            ViewCompat.setPivotX(page, 0);
            ViewCompat.setPivotY(page, page.getHeight() / 2);
        }
        page.setScaleX( scale);
        page.setScaleY( scale);
        page.setAlpha( Math.abs(alpha));
    }
}
