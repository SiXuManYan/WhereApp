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
    private String mMiddleTitle = "";
    private int mMiddleTitleColor = -1;
    private boolean mShowBackIv = true;
    private int mBackIconRes = -1;
    private int mFirstRightIconRes = -1;
    private int mSecondRightIconRes = -1;
    private boolean mShowBottomLine = false;
    private ImageView mBackIv;
    private View mBottomLine;
    private TextView mMiddleTitleTv;
    private ImageView mFirstRightIv, mSecondRightIv;

    public JcsTitle(Context context) {
        this(context, null, 0);
    }

    public JcsTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JcsTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.JcsTitle);
        mMiddleTitle = array.getString(R.styleable.JcsTitle_middleTitle);
        mMiddleTitleColor = array.getColor(R.styleable.JcsTitle_middleTitleColor, Color.parseColor("#333333"));
        mShowBackIv = array.getBoolean(R.styleable.JcsTitle_showBackIv, true);
        mBackIconRes = array.getResourceId(R.styleable.JcsTitle_backIcon, R.drawable.ic_back_black);
        mFirstRightIconRes = array.getResourceId(R.styleable.JcsTitle_rightFirstIcon, 0);
        mSecondRightIconRes = array.getResourceId(R.styleable.JcsTitle_rightSecondIcon, 0);
        mShowBottomLine = array.getBoolean(R.styleable.JcsTitle_showBottomLine, false);

        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_title, this);
        mBottomLine = view.findViewById(R.id.bottomLine);
        mBackIv = view.findViewById(R.id.backIv);
        mBackIv.setImageResource(mBackIconRes);
        mMiddleTitleTv = view.findViewById(R.id.middleTitle);
        mMiddleTitleTv.setText(mMiddleTitle);
        mMiddleTitleTv.setTextColor(mMiddleTitleColor);
        mFirstRightIv = view.findViewById(R.id.firstRightIv);
        mSecondRightIv = view.findViewById(R.id.secondRightIv);

        if (!mShowBackIv) {
            mBackIv.setVisibility(GONE);
        }

        if (mShowBottomLine) {
            mBottomLine.setVisibility(VISIBLE);
        }

        if (mFirstRightIconRes == 0) {
            mFirstRightIv.setVisibility(GONE);
        }else {
            mFirstRightIv.setImageResource(mFirstRightIconRes);
        }

        if (mSecondRightIconRes == 0) {
            mSecondRightIv.setVisibility(GONE);
            mFirstRightIv.setImageResource(mSecondRightIconRes);
        }
    }

    public void setBackIvClickListener(View.OnClickListener listener) {
        mBackIv.setOnClickListener(listener);
    }

    public void setFirstRightIvClickListener(View.OnClickListener listener) {
        mFirstRightIv.setOnClickListener(listener);
    }

    public void setSecondRightIvClickListener(View.OnClickListener listener) {
        mSecondRightIv.setOnClickListener(listener);
    }

    public void setMiddleTitle(String middleTitle) {
        this.mMiddleTitle = middleTitle;
        this.mMiddleTitleTv.setText(middleTitle);
    }

    public void setMiddleTitleColor(int color) {
        this.mMiddleTitleColor = color;
        this.mMiddleTitleTv.setTextColor(mMiddleTitleColor);
    }

    public void setBackIcon(int backIconRes) {
        this.mBackIconRes = backIconRes;
        this.mBackIv.setImageResource(backIconRes);
    }

    public void setFirstRightIcon(int firstRightIcon) {
        this.mFirstRightIconRes = firstRightIcon;
        this.mFirstRightIv.setImageResource(firstRightIcon);
    }
    public void setSecondRightIcon(int secondRightIcon) {
        this.mSecondRightIconRes = secondRightIcon;
        this.mSecondRightIv.setImageResource(secondRightIcon);
    }

}
