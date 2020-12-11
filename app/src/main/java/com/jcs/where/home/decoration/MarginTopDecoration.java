package com.jcs.where.home.decoration;

import android.graphics.Rect;
import android.view.View;

import com.jcs.where.base.BaseItemDecoration;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * create by zyf on 2020/12/11 10:03 PM
 */
public abstract class MarginTopDecoration extends BaseItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = getPxFromDp(view.getContext(), getMarginTop());
    }

    public abstract int getMarginTop();
}
