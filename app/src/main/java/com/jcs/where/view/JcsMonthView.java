package com.jcs.where.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

public class JcsMonthView extends MonthView {
    private Paint mDayPaint;

    public JcsMonthView(Context context) {
        super(context);
        mDayPaint = new Paint();
        mDayPaint.setColor(Color.parseColor("#333333"));
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        canvas.drawText("1", x, y, mDayPaint);
    }
}
