package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcs.where.R;
import com.jcs.where.utils.DimenUtil;

import androidx.annotation.Nullable;

public class StarView extends LinearLayout {

    private int mStarNum = 0;

    public StarView(Context context) {
        this(context, null);
    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(HORIZONTAL);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StarView);
        mStarNum = array.getInteger(R.styleable.StarView_starNum, 0);

        array.recycle();
        addStar();
    }

    private void addStar() {
        removeAllViews();
        for (int i = 0; i < mStarNum; i++) {
            ImageView star = new ImageView(getContext());
            star.setImageResource(R.drawable.ic_star_yellow);
            if (i > 0) {
                LinearLayout.LayoutParams lp = new LayoutParams(DimenUtil.toDp(getContext(), 10), DimenUtil.toDp(getContext(), 10));
                lp.leftMargin = DimenUtil.toDp(getContext(), 3);
                star.setLayoutParams(lp);
            }
            addView(star);
        }
    }


    public void setStartNum(int startNum) {
        this.mStarNum = startNum;
        addStar();
        invalidate();
    }


}
