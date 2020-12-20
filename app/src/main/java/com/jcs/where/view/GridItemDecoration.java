package com.jcs.where.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jcs.where.base.BaseItemDecoration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends BaseItemDecoration {

    private final int mHorizontalSpacing;
    private final int mVerticalSpacing;

    public GridItemDecoration(Context context) {
        this(context, 8, 10);
    }

    public GridItemDecoration(Context context, int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = getPxFromDp(context, horizontalSpacing);
        mVerticalSpacing = getPxFromDp(context, verticalSpacing);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        BaseQuickAdapter adapter = (BaseQuickAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);
        int itemViewType = adapter.getItemViewType(position);
        if (itemViewType == 0) {
            position -= adapter.getHeaderLayoutCount();

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
