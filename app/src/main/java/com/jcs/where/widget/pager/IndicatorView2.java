package com.jcs.where.widget.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.jcs.where.R;

/**
 * Created by Wangsw  2021/3/20 16:06.
 */
public class IndicatorView2 extends LinearLayout {

    private LinearLayout container_ll;

    public int selectedDrawableResId = R.drawable.shape_point_selected_9999;
    public int commonDrawableResId = R.drawable.shape_point_normal_ffffff;


    public IndicatorView2(Context context) {
        super(context);
        initView();
    }

    public IndicatorView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IndicatorView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public IndicatorView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    public void setPointCount(int totalCount) {
        container_ll.removeAllViews();
        for (int i = 0; i < totalCount; i++) {

            LayoutParams params = new LayoutParams(SizeUtils.dp2px(5), SizeUtils.dp2px(5));
            params.setMarginEnd(SizeUtils.dp2px(5));
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);

            if (i == 0) {
                imageView.setImageResource(selectedDrawableResId);
            } else {
                imageView.setImageResource(commonDrawableResId);
            }
            container_ll.addView(imageView);
        }

        if (totalCount <= 1) {
            setVisibility(View.GONE);
        }

    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_indicator_2, this, true);
        container_ll = view.findViewById(R.id.container_ll);
    }

    public void onPageSelected(int position) {

        int childCount = container_ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView child = (ImageView) container_ll.getChildAt(i);
            if (position == i) {
                child.setImageResource(selectedDrawableResId);
            } else {
                child.setImageResource(commonDrawableResId);
            }

        }
    }

}
