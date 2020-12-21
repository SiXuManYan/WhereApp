package com.jcs.where.home.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jcs.where.R;
import com.jcs.where.adapter.HotelCalendarAdapter;
import com.jcs.where.base.BaseItemDecoration;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HotelCalendarItemDecoration extends BaseItemDecoration {
    private Paint mHeaderBgPaint;
    private int headerNum = 0;

    private final int dayWidth = 37;
    private final int spanCount = 37;

    public void initPaint(Context context) {
        this.mHeaderBgPaint = new Paint();
        mHeaderBgPaint.setColor(ContextCompat.getColor(context, R.color.grey_EAEAEA));
    }

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

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Context context = parent.getContext();
        if (mHeaderBgPaint == null && context != null) {
            initPaint(context);
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager != null && context != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            HotelCalendarAdapter adapter = (HotelCalendarAdapter) parent.getAdapter();
            HotelCalendarAdapter.HotelCalendarBean item = adapter.getItem(firstVisibleItemPosition);
            if (item.isHeader()) {
            }
        }

        c.drawRect(0, 0, parent.getMeasuredWidth(), getPxFromDp(context, 30), mHeaderBgPaint);

    }
}
