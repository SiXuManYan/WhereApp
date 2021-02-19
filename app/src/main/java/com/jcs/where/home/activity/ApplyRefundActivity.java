package com.jcs.where.home.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * 页面-申请退款
 * create by zyf on 2021/2/6 12:17 下午
 */
public class ApplyRefundActivity extends BaseActivity {
    public static final String K_ID = "id";
    private TextView mOrderNumberTv, mPriceTv, mRefundMethodTv, mExpectedTimeTv;
    private Button mEnsureBtn;
    private int mOrderId;

    @Override
    protected void initView() {
        mOrderNumberTv = findViewById(R.id.orderNumberTv);
        mPriceTv = findViewById(R.id.priceTv);
        mRefundMethodTv = findViewById(R.id.refundMethodTv);
        mExpectedTimeTv = findViewById(R.id.expectedTimeTv);
        mEnsureBtn = findViewById(R.id.ensureBtn);
    }

    @Override
    protected void initData() {
        mOrderId = getIntent().getIntExtra(K_ID, -1);
    }

    @Override
    protected void bindListener() {
        mEnsureBtn.setOnClickListener(this::onEnsureBtnClicked);
    }

    /**
     * 确定按钮访问要退款api
     */
    private void onEnsureBtnClicked(View view) {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_refund;
    }
}
