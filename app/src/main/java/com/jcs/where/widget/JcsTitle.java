package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.jcs.where.R;

public class JcsTitle extends ConstraintLayout {
    private String middleTitle = "";
    private boolean showBackIv = true;
    private boolean showBottomLine = false;
    private ImageView mBackIv;
    private View mBottomLine;
    private TextView mMiddleTitleTv;

    public JcsTitle(Context context) {
        this(context, null, 0);
    }

    public JcsTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JcsTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.JcsTitle);
        middleTitle = array.getString(R.styleable.JcsTitle_middleTitle);
        showBackIv = array.getBoolean(R.styleable.JcsTitle_showBackIv, true);
        showBottomLine = array.getBoolean(R.styleable.JcsTitle_showBottomLine, false);

        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_title, this);
        mBottomLine = view.findViewById(R.id.bottomLine);
        mBackIv = view.findViewById(R.id.backIv);
        mMiddleTitleTv = view.findViewById(R.id.middleTitle);
        mMiddleTitleTv.setText(middleTitle);

        if (!showBackIv) {
            mBackIv.setVisibility(GONE);
        }

        if (showBottomLine) {
            mBottomLine.setVisibility(VISIBLE);
        }
    }

    public void setBackIvClickListener(View.OnClickListener listener) {
        mBackIv.setOnClickListener(listener);
    }

}
