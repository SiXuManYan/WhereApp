package com.jiechengsheng.city.features.mall.sku.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.blankj.utilcode.util.SizeUtils;


/**
 * Sku Item ScrollView
 * <p>
 * 解决Sku过多时，选择界面铺满全屏的问题
 * <p>
 * Created by wuhenzhizao on 15/12/11.
 */
public class SkuMaxHeightScrollView extends ScrollView {

    public SkuMaxHeightScrollView(Context context) {
        super(context);
    }

    public SkuMaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height)
                height = h;
        }
        float heightDp = SizeUtils.px2dp(height);
        if (heightDp < 100) {
            int minHeight = SizeUtils.dp2px(100f);
            setMeasuredDimension(width, minHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
