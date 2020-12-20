package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class JcsTitle extends ConstraintLayout {
    private String middleTitle = "";
    private int middleTitleColor = -1;
    private boolean showBackIv = true;
    private int backIconRes = -1;
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
        middleTitleColor = array.getColor(R.styleable.JcsTitle_middleTitleColor, Color.parseColor("#333333"));
        showBackIv = array.getBoolean(R.styleable.JcsTitle_showBackIv, true);
        backIconRes = array.getResourceId(R.styleable.JcsTitle_backIcon, R.drawable.ic_back_black);
        showBottomLine = array.getBoolean(R.styleable.JcsTitle_showBottomLine, false);

        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_title, this);
        mBottomLine = view.findViewById(R.id.bottomLine);
        mBackIv = view.findViewById(R.id.backIv);
        mBackIv.setImageResource(backIconRes);
        mMiddleTitleTv = view.findViewById(R.id.middleTitle);
        mMiddleTitleTv.setText(middleTitle);
        mMiddleTitleTv.setTextColor(middleTitleColor);

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

    public void setMiddleTitle(String middleTitle) {
        this.middleTitle = middleTitle;
        this.mMiddleTitleTv.setText(middleTitle);
    }

    public void setMiddleTitleColor(int color) {
        this.middleTitleColor = color;
        this.mMiddleTitleTv.setTextColor(middleTitleColor);
    }

    public void setBackIcon(int backIconRes) {
        this.backIconRes = backIconRes;
        this.mBackIv.setImageResource(backIconRes);
    }

}
