package com.jcs.where.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jcs.where.R;

/**
 * Created by Wangsw  2021/1/23 10:36.
 * 连续签到积分 view
 */
public class IntegralView extends LinearLayout {

    private ImageView mBgIv;
    private TextView mContentTv;

    public IntegralView(Context context) {
        super(context);
        initView();
    }


    public IntegralView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IntegralView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public IntegralView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_integral, this, true);
        mBgIv = view.findViewById(R.id.bg_iv);
        mContentTv = view.findViewById(R.id.content_tv);
        mContentTv.setOnClickListener(view1 -> {
            if (contentClickCallBack != null) {
                contentClickCallBack.onContentClick();
            }
        });


    }


    public void setImageBg(boolean isBright) {
        if (isBright) {
            mBgIv.setImageResource(R.drawable.shape_oval_integral);
        } else {
            mBgIv.setImageResource(R.drawable.shape_oval_integral_gray);
        }
    }

    public void setContentText(String text) {
        mContentTv.setText(text);
    }

    public ContentClickCallBack contentClickCallBack;

    public interface ContentClickCallBack {

        /**
         * 内容点击回调
         */
        void onContentClick();
    }


}
