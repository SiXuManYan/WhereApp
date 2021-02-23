package com.jcs.where.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jcs.where.R;

/**
 * Created by Wangsw  2021/2/23 10:40.
 */
public class MessageView extends LinearLayout {

    private TextView message_count_tv;
    private int currentMessageCount = 0;

    public MessageView(Context context) {
        super(context);
        initView();
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MessageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_message_count, this, true);
        message_count_tv = view.findViewById(R.id.message_count_tv);
    }

    /**
     * 设置消息数量
     *
     * @param count
     */
    public void setMessageCount(int count) {
        if (count > 0) {
            message_count_tv.setVisibility(View.VISIBLE);
        } else {
            message_count_tv.setVisibility(View.GONE);
        }
        message_count_tv.setText(String.valueOf(count));
        this.currentMessageCount = count;
    }



}
