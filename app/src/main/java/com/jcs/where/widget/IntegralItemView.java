package com.jcs.where.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.jcs.where.R;
import com.jcs.where.api.response.SignListResponse;

/**
 * Created by Wangsw  2021/1/23 10:36.
 * 签到列表 item
 */
public class IntegralItemView extends LinearLayout {

    private ImageView mBgIv;
    private TextView mContentTv;

    public IntegralItemView(Context context) {
        super(context);
        initView();
    }


    public IntegralItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IntegralItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public IntegralItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_integral, this, true);
        mBgIv = view.findViewById(R.id.bg_iv);
        mContentTv = view.findViewById(R.id.content_tv);

    }


    public void setImageBg(boolean isSigned) {
        if (isSigned) {
            mBgIv.setImageResource(R.drawable.shape_oval_integral);
        } else {
            mBgIv.setImageResource(R.drawable.shape_oval_integral_gray);
        }
    }


    public void setContent(int index, SignListResponse.DataBean data) {
        boolean isSigned = data.getSignStatus() == 1;
        setImageBg(isSigned);
        if (isSigned) {
            // 已签到显示第几天
            String[] stringArray = StringUtils.getStringArray(R.array.sign_in_index);
            mContentTv.setText(stringArray[index]);
        } else {
            // 未签到显示可获得的积分
            mContentTv.setText(StringUtils.getString(R.string.add_format, data.getIntegral()));
        }
    }
}
