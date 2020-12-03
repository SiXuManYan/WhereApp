package com.jcs.where.home.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcs.where.base.BaseItemDecoration;
import com.jcs.where.home.adapter.HotelCalendarAdapter;

public class HotelCalendarItemDecoration extends BaseItemDecoration {
    private int headerNum = 0;

    private final int dayWidth = 37;
    private final int spanCount = 37;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        HotelCalendarAdapter adapter = (HotelCalendarAdapter) parent.getAdapter();
        if (adapter != null) {
            HotelCalendarAdapter.HotelCalendarBean bean = adapter.getItem(childAdapterPosition);
            Context context = view.getContext();
            if (!bean.isHeader()) {
                int recyclerWidth = parent.getMeasuredWidth();
                int viewWidths = getPxFromDp(context, dayWidth) * 7;
                int spaceWidths = recyclerWidth - viewWidths;
                int space = spaceWidths / 14;

                outRect.left = space;
                outRect.right = space;
                outRect.top = getPxFromDp(context, 5);
                outRect.bottom = getPxFromDp(context, 5);
            } else {
                headerNum++;
            }
        }
    }
}
