package com.jcs.where.home.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import com.jcs.where.R;
import com.jcs.where.widget.calendar.JcsCalendarAdapter;
import com.jcs.where.base.BaseItemDecoration;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HotelCalendarItemDecoration extends BaseItemDecoration {
    /**
     * 绘制悬停头的背景
     */
    private Paint mHeaderBgPaint;

    /**
     * 绘制悬停头的日期
     */
    private TextPaint mHeaderTextPaint;

    /**
     * 绘制开始和结束日期之间的item的选择状态
     */
    private Paint mBetweenStartAndEndPaint;

    /**
     * 清空开始和结束日期之间的item的选择状态
     */
    private Paint mClearPaint;

    private int mHeaderNum = 0;
    private final int mDayWidth = 37;
    private final int mSpanCount = 37;
    private String mCurrentDate = "";
    private Rect mCurrentDateBg;
    private int mSpace;
    private int mStartPosition;
    private int mEndPosition;

    public HotelCalendarItemDecoration(Context context) {
        initPaint(context);
    }

    public void initPaint(Context context) {
        mHeaderBgPaint = new Paint();
        mHeaderBgPaint.setColor(ContextCompat.getColor(context, R.color.grey_EAEAEA));
        mHeaderTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mHeaderTextPaint.setTextSize(getPxFromDp(context, 14));
        mHeaderTextPaint.setColor(ContextCompat.getColor(context, R.color.black_333333));
        mBetweenStartAndEndPaint = new Paint();
        mBetweenStartAndEndPaint.setColor(ContextCompat.getColor(context, R.color.blue_D5EAFF));
        mClearPaint = new Paint();
        mClearPaint.setColor(ContextCompat.getColor(context, R.color.white));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        JcsCalendarAdapter adapter = (JcsCalendarAdapter) parent.getAdapter();
        if (adapter != null) {
            JcsCalendarAdapter.CalendarBean bean = adapter.getItem(childAdapterPosition);
            Context context = view.getContext();
            if (!bean.isHeader()) {
                int recyclerWidth = parent.getMeasuredWidth();
                int viewWidths = getPxFromDp(context, mDayWidth) * 7;
                int spaceWidths = recyclerWidth - viewWidths;
                if (mSpace == 0) {
                    mSpace = spaceWidths / 14;
                }
                outRect.top = getPxFromDp(context, 5);
                outRect.bottom = getPxFromDp(context, 5);
            } else {
                mHeaderNum++;
            }
        }
    }

    /**
     * 暂时无用
     */
    private void drawMarginBg(@NonNull Canvas c, @NonNull RecyclerView parent) {
        JcsCalendarAdapter adapter = (JcsCalendarAdapter) parent.getAdapter();
        if (adapter != null) {
            for (int i = mStartPosition; i <= mEndPosition; i++) {
                View itemView = parent.getChildAt(i);
                if (itemView != null) {
                    int left = itemView.getLeft();
                    int top = itemView.getTop();
                    int right = itemView.getRight();
                    int bottom = itemView.getBottom();
                    Object startObj = itemView.getTag(R.id.tag_is_start_date);
                    Object endObj = itemView.getTag(R.id.tag_is_end_date);
                    Object selectedObj = itemView.getTag(R.id.tag_is_selected_date);
                    boolean isStart = startObj != null && (boolean) startObj;
                    boolean isEnd = endObj != null && (boolean) endObj;
                    boolean isSelect = selectedObj != null && (boolean) selectedObj;
                    if (isStart && adapter.isEndSelected()) {
                        c.drawRect(right, top, right + mSpace + 1, bottom, mBetweenStartAndEndPaint);
                    } else if (isEnd) {
                        c.drawRect(left - mSpace - 1, top, left, bottom, mBetweenStartAndEndPaint);
                    } else if (!isStart && !isEnd && isSelect) {
                        c.drawRect(left - mSpace - 1, top, left, bottom, mBetweenStartAndEndPaint);
                        c.drawRect(right, top, right + mSpace + 1, bottom, mBetweenStartAndEndPaint);
                    } else {
                        c.drawRect(left - mSpace - 1, top, left, bottom, mClearPaint);
                        c.drawRect(right, top, right + mSpace + 1, bottom, mClearPaint);
                    }
                }
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
            c.drawRect(mCurrentDateBg, mHeaderBgPaint);
            LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
            if (layoutManager != null) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                JcsCalendarAdapter adapter = (JcsCalendarAdapter) parent.getAdapter();
                if (adapter != null) {
                    JcsCalendarAdapter.CalendarBean item = adapter.getItem(firstVisibleItemPosition);
                    String showDate = item.getShowYearMonthDate();
                    if (showDate != null) {
                        mCurrentDate = showDate;
                    }
                    c.drawText(mCurrentDate, getPxFromDp(context, 16), getTextBaseLine(mHeaderTextPaint, mCurrentDateBg), mHeaderTextPaint);
                }
            }
        }
    }

    public int getStartPosition() {
        return mStartPosition;
    }

    public void setStartPosition(int startPosition) {
        this.mStartPosition = startPosition;
    }

    public int getEndPosition() {
        return mEndPosition;
    }

    public void setEndPosition(int endPosition) {
        this.mEndPosition = endPosition;
    }
}
