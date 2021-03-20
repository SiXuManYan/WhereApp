package com.jcs.where.widget.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.jcs.where.R;

/**
 * Created by Wangsw  2021/3/20 16:06.
 */
public class IndicatorView extends LinearLayout {

    private LinearLayout container_ll;

    public IndicatorView(Context context) {
        super(context);
        initView();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_indicator, this, true);
        container_ll = view.findViewById(R.id.container_ll);
    }

    public void onPageSelected(int position) {

        int childCount = container_ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView child = (ImageView) container_ll.getChildAt(i);
            if (position == i) {
                child.setImageResource(R.drawable.shape_point_selected);
            } else {
                child.setImageResource(R.drawable.shape_point_normal);
            }

        }
    }

}
