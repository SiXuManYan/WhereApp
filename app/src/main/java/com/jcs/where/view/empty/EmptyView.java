package com.jcs.where.view.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.jcs.where.R;

/**
 * Created by Wangsw  2021/3/11 10:56.
 * 列表缺省页
 */
public class EmptyView extends LinearLayout {

    public ImageView empty_iv;
    public TextView empty_message_tv;
    public TextView empty_hint_tv;
    public TextView action_tv;
    public LinearLayout parent_ll;

    public EmptyView(Context context) {
        super(context);
        initView();
    }


    public EmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_empty, this, true);
        empty_iv = view.findViewById(R.id.empty_iv);
        empty_message_tv = view.findViewById(R.id.empty_message_tv);
        empty_hint_tv = view.findViewById(R.id.empty_hint_tv);
        action_tv = view.findViewById(R.id.action_tv);
        parent_ll = view.findViewById(R.id.parent_ll);
    }

    public void setEmptyImage(@DrawableRes int imageId) {
        empty_iv.setImageResource(imageId);
    }

    public void setEmptyMessage(@StringRes int emptyMessage) {
        empty_message_tv.setText(emptyMessage);
    }

    public void setEmptyHint(@StringRes int hint) {
        empty_hint_tv.setText(hint);
        empty_hint_tv.setVisibility(VISIBLE);
    }

    public void setEmptyActionText(@StringRes int text) {
        action_tv.setText(text);
        action_tv.setVisibility(VISIBLE);
    }

    /**
     * 网络错误
     */
    public void showRetryDefault() {
        empty_iv.setImageResource(R.mipmap.ic_empty_network_error);
        empty_message_tv.setText(R.string.empty_network_error);
        empty_hint_tv.setText(R.string.empty_network_error_hint);
        action_tv.setText(R.string.empty_network_error_action);
    }

    public void setEmptyActionOnClickListener(View.OnClickListener listener) {
        action_tv.setOnClickListener(listener);
    }


    /**
     * 设置空布局
     *
     * @param imageId      空数据图片
     * @param emptyMessage 提示文案
     */
    public void showEmpty(@DrawableRes int imageId, @StringRes int emptyMessage) {
        setEmptyImage(imageId);
        setEmptyMessage(emptyMessage);
    }

    /**
     * 设置空布局
     */
    public void showEmptyDefault() {
        setEmptyImage(R.mipmap.ic_empty_earth);
        setEmptyMessage(R.string.empty_data_default);
    }

    public void showEmptyNothing() {
        empty_iv.setImageAlpha(0);
        empty_message_tv.setVisibility(View.GONE);
        empty_hint_tv.setVisibility(View.GONE);
        action_tv.setVisibility(View.GONE);
    }


    public void initEmpty(@DrawableRes int imageId,
                          @StringRes int emptyMessage,
                          @StringRes int emptyHint,
                          @StringRes int actionString,
                          View.OnClickListener listener) {

        parent_ll.setVisibility(View.GONE);
        empty_iv.setImageResource(imageId);
        empty_message_tv.setText(emptyMessage);
        empty_hint_tv.setText(emptyHint);
        action_tv.setText(actionString);
        action_tv.setOnClickListener(listener);
    }

    public void showEmptyContainer() {
        parent_ll.setVisibility(View.VISIBLE);
    }

    public void hideEmptyContainer() {
        parent_ll.setVisibility(View.GONE);
    }

}
