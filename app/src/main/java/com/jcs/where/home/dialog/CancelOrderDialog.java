package com.jcs.where.home.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseDialog;

/**
 * create by zyf on 2021/2/6 1:07 下午
 */
public class CancelOrderDialog extends BaseDialog {

    private TextView mContentPrompt;
    private Button mCancelBtn, mEnsureBtn;

    @Override
    protected int getLayout() {
        return R.layout.dialog_to_install_app;
    }

    @Override
    protected int getHeight() {
        return 180;
    }

    @Override
    protected int getWidth() {
        return 300;
    }

    @Override
    protected void initView(View view) {
        mCancelBtn = view.findViewById(R.id.cancelBtn);
        mEnsureBtn = view.findViewById(R.id.ensureBtn);

        mContentPrompt = view.findViewById(R.id.installPrompt);
        mContentPrompt.setText("取消订单");

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

        mCancelBtn.setOnClickListener(v -> dismiss());
        mEnsureBtn.setOnClickListener(this::onEnsureClicked);
    }

    public void onEnsureClicked(View view) {

    }

    @Override
    protected boolean isBottom() {
        return false;
    }
}
