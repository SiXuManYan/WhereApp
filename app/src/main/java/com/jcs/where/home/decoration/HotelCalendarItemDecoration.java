package com.jcs.where.home.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
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
    private TextPaint mHeaderTextPaint;
    private int mHeaderNum = 0;

    private final int mDayWidth = 37;
    private final int mSpanCount = 37;
    private String mCurrentDate = "";
    private Rect mCurrentDateBg;

    public void initPaint(Context context) {
        mHeaderBgPaint = new Paint();
        mHeaderBgPaint.setColor(ContextCompat.getColor(context, R.color.grey_EAEAEA));
        mHeaderTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mHeaderTextPaint.setTextSize(getPxFromDp(context, 14));
        mHeaderTextPaint.setColor(ContextCompat.getColor(context, R.color.black_333333));
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
                int viewWidths = getPxFromDp(context, mDayWidth) * 7;
                int spaceWidths = recyclerWidth - viewWidths;
                int space = spaceWidths / 14;

                outRect.left = space;
                outRect.right = space;
                outRect.top = getPxFromDp(context, 5);
                outRect.bottom = getPxFromDp(context, 5);
            } else {
                mHeaderNum++;
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        Context context = parent.getContext();
        if (context != null) {
            if (mCurrentDateBg == null) {
                mCurrentDateBg = new Rect(0, 0, parent.getMeasuredWidth(), getPxFromDp(context, 30));
            }
            if (mHeaderBgPaint == null) {
                initPaint(context);
            }
            c.drawRect(mCurrentDateBg, mHeaderBgPaint);
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            if (layoutManager != null) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                HotelCalendarAdapter adapter = (HotelCalendarAdapter) parent.getAdapter();
                if (adapter != null) {
                    HotelCalendarAdapter.HotelCalendarBean item = adapter.getItem(firstVisibleItemPosition);
                    String showDate = item.getShowDate();
                    if (showDate != null) {
                        mCurrentDate = showDate;
                    }
                    c.drawText(mCurrentDate, getPxFromDp(context, 16), getTextBaseLine(mHeaderTextPaint, mCurrentDateBg), mHeaderTextPaint);
                }
            }
        }
    }
}
