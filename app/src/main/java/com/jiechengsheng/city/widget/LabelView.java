package com.jiechengsheng.city.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiechengsheng.city.R;
import com.jiechengsheng.city.utils.DimenUtil;

import java.util.List;

import androidx.annotation.Nullable;

public class LabelView extends LinearLayout {

    private final int mLabelNum = 0;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(HORIZONTAL);
    }

    private void addLabel(List<LabelNameInterface> labels) {
        removeAllViews();
        if (labels != null) {
            int size = labels.size();
            for (int i = 0; i < size; i++) {

                TextView labelTv = new TextView(getContext());
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DimenUtil.toDp(getContext(), 14));
                int leftAndRight = DimenUtil.toDp(getContext(), 10);
                labelTv.setPadding(leftAndRight, 0, leftAndRight, 0);
                labelTv.setGravity(Gravity.CENTER_VERTICAL);
                labelTv.setBackgroundResource(R.drawable.bg_hotel_label);
                labelTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                labelTv.setTextColor(Color.parseColor("#5CA5F3"));
                labelTv.setText(labels.get(i).getLabelName());
                if (i > 0) {
                    lp.leftMargin = DimenUtil.toDp(getContext(), 5);
                }
                labelTv.setLayoutParams(lp);

                addView(labelTv);
            }
        }
    }


    public void setLabels(List<LabelNameInterface> labels) {
        addLabel(labels);
        invalidate();
    }

    public interface LabelNameInterface {
        String getLabelName();
    }

}
