package com.jcs.where.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
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
    private ImageView mBackIv;
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
        showBackIv = array.getBoolean(R.styleable.JcsTitle_showBackIv,true);

        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_jcs_title,this);
        mBackIv = view.findViewById(R.id.backIv);
        mMiddleTitleTv = view.findViewById(R.id.middleTitle);
        mMiddleTitleTv.setText(middleTitle);

        if(!showBackIv){
            mBackIv.setVisibility(GONE);
        }
    }

    public void setBackIvClickListener(View.OnClickListener listener){
        mBackIv.setOnClickListener(listener);
    }

}
