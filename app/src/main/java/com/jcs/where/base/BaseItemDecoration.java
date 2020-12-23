package com.jcs.where.base;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;

import androidx.recyclerview.widget.RecyclerView;

public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    protected int getPxFromDp(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    protected float getTextBaseLine(TextPaint textPaint, Rect rect) {
        //计算baseline
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        return rect.centerY() + distance;
    }
}
