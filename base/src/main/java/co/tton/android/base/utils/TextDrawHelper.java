package co.tton.android.base.utils;

import android.graphics.Paint;

public class TextDrawHelper {

    private float mTextWidth;
    private float mTextHeight;
    private float mTextLeading;

    public void setPaint(Paint paint) {
        mTextHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
        mTextLeading = paint.getFontMetrics().leading - paint.getFontMetrics().ascent;
    }

    public float[] getStartPoint(int width, int height) {
        float x = (width - mTextWidth) / 2.0f;
        float y = (height - mTextHeight) / 2.0f + mTextLeading;
        return new float[]{x, y};
    }

    public float getTextWidth(Paint paint, String text) {
        mTextWidth = paint.measureText(text);
        return mTextWidth;
    }
}
