package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jcs.where.R;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

public class JcsTitle extends ConstraintLayout {
    private String mMiddleTitle = "";
    private int mMiddleTitleColor = -1;
    private boolean mShowBackIv = true;
    private int mBackIconRes = -1;
    private int mFirstRightIconRes = -1;
    private int mSecondRightIconRes = -1;
    private int mBackgroundRes = -1;
    private boolean mShowBottomLine = false;
    private float mBackIconWidth, mBackIconHeight;
    private ImageView mBackIv;
    private View mBottomLine;
    private TextView mMiddleTitleTv;
    private ImageView mFirstRightIv, mSecondRightIv;
    private ConstraintLayout mTitleLayout;

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
        mBackIconWidth = array.getDimension(R.styleable.JcsTitle_backIconWidth, -1);
        mBackIconHeight = array.getDimension(R.styleable.JcsTitle_backIconHeight, -1);
        mBackIconRes = array.getResourceId(R.styleable.JcsTitle_backIcon, R.drawable.ic_back_black);
        mFirstRightIconRes = array.getResourceId(R.styleable.JcsTitle_rightFirstIcon, 0);
        mSecondRightIconRes = array.getResourceId(R.styleable.JcsTitle_rightSecondIcon, 0);
        mBackgroundRes = array.getResourceId(R.styleable.JcsTitle_backgroundRes, 0);
        mShowBottomLine = array.getBoolean(R.styleable.JcsTitle_showBottomLine, false);


        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_title, this);
        mBottomLine = view.findViewById(R.id.bottomLine);
        mBackIv = view.findViewById(R.id.backIv);
        mBackIv.setImageResource(mBackIconRes);
        mBackIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mMiddleTitleTv = view.findViewById(R.id.middleTitle);
        mMiddleTitleTv.setText(mMiddleTitle);
        mMiddleTitleTv.setTextColor(mMiddleTitleColor);
        mFirstRightIv = view.findViewById(R.id.firstRightIv);
        mSecondRightIv = view.findViewById(R.id.secondRightIv);
        mTitleLayout = view.findViewById(R.id.titleLayout);

        if (!mShowBackIv) {
            mBackIv.setVisibility(GONE);
        } else {
            if (mBackIconHeight != -1 && mBackIconWidth != -1) {
                ConstraintLayout.LayoutParams layoutParams = (LayoutParams) mBackIv.getLayoutParams();
                // 默认左右有个padding为5，所以这里是加上左右总的padding10为总宽度
                layoutParams.width = (int) mBackIconWidth + getDp(10);
                layoutParams.height = (int) mBackIconHeight;
                layoutParams.leftMargin = getDp(10);
                mBackIv.setLayoutParams(layoutParams);
            }
        }

        if (mShowBottomLine) {
            mBottomLine.setVisibility(VISIBLE);
        }

        if (mFirstRightIconRes == 0) {
            mFirstRightIv.setVisibility(GONE);
        } else {
            mFirstRightIv.setImageResource(mFirstRightIconRes);
        }

        if (mSecondRightIconRes == 0) {
            mSecondRightIv.setVisibility(GONE);
        } else {
            mSecondRightIv.setImageResource(mSecondRightIconRes);
        }

        if (mBackgroundRes != 0) {
            mTitleLayout.setBackgroundResource(mBackgroundRes);
        }
    }

    public void setBackGround(@ColorRes int color){
        mTitleLayout.setBackgroundColor(ColorUtils.getColor(color));
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


    public void setMiddleTitle(@StringRes int strResId) {
        String string = StringUtils.getString(strResId);
        this.mMiddleTitle = string;
        this.mMiddleTitleTv.setText(string);
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

    protected int getDp(int height) {
        Context context = getContext();
        if (context == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
    }
}
