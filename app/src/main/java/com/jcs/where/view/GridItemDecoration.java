package com.jcs.where.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.tton.android.base.utils.ValueUtils;
import co.tton.android.base.view.BaseQuickAdapter;

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public GridItemDecoration(Context context) {
        this(context, 8, 10);
    }

    public GridItemDecoration(Context context, int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = ValueUtils.dpToPx(context, horizontalSpacing);
        mVerticalSpacing = ValueUtils.dpToPx(context, verticalSpacing);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        BaseQuickAdapter adapter = (BaseQuickAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);
        int itemViewType = adapter.getItemViewType(position);
        if (itemViewType == BaseQuickAdapter.NORMAL_VIEW) {
            position -= adapter.getHeaderViewCount();

            int span = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int itemOffset = mHorizontalSpacing / 2;

            outRect.right = itemOffset;
            outRect.left = itemOffset;
            if (position < span) {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom = mVerticalSpacing;
        }
    }
}
