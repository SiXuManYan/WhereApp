package com.jcs.where.mine.activity.merchant_settled;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * 页面-商家入驻审核过程
 * create by zyf on 2021/2/22 7:30 下午
 */
public class MerchantVerifyActivity extends BaseActivity {
    public static final String EXT_VERIFY = "verify";

    private ImageView mVerifyIcon;
    private TextView mVerifyTitle, mVerifyDesc;
    private TextView mStep2DescTv, mStep3DescTv, mStep3Prompt;
    private Button mCommitBtn;
    private View mStep2To3Line;

    public static void go(Context context, Integer isVerify) {
        Intent to = new Intent(context, MerchantVerifyActivity.class);
        to.putExtra(EXT_VERIFY, isVerify);
        context.startActivity(to);
    }

    @Override
    protected void initView() {
        mStep2DescTv = findViewById(R.id.step2DescTv);
        mStep3DescTv = findViewById(R.id.step3DescTv);
        mStep3Prompt = findViewById(R.id.step3Prompt);
        mStep2To3Line = findViewById(R.id.step2To3Line);

        mVerifyIcon = findViewById(R.id.verifyIcon);
        mVerifyTitle = findViewById(R.id.verifyTitle);
        mVerifyDesc = findViewById(R.id.verifyDesc);

        mCommitBtn = findViewById(R.id.commitTv);
    }

    @Override
    protected void initData() {
        int verifyStatus = getIntent().getIntExtra(EXT_VERIFY, 0);
        switch (verifyStatus) {
            case 1:
                // 待审核

                // 这里显示了审核中的UI
                mVerifyIcon.setImageResource(R.mipmap.ic_merchant_verify_ing);
                mVerifyDesc.setText(R.string.merchant_verify_ing_desc);
                mVerifyTitle.setText(R.string.merchant_verify_ing_title);
                mCommitBtn.setVisibility(View.GONE);
                mStep2DescTv.setText(R.string.merchant_verify_ing_title);
                break;
            case 2:
                // 审核通过
                mVerifyIcon.setImageResource(R.mipmap.ic_merchant_verify_success);
                mVerifyDesc.setText(R.string.merchant_verify_success_desc);
                mVerifyTitle.setText(R.string.merchant_verify_success_title);
                mCommitBtn.setVisibility(View.GONE);
                mStep2DescTv.setText(R.string.merchant_verify_success_step2);
                mStep2To3Line.setBackgroundResource(R.color.blue_4C9EF2);
                mStep3Prompt.setTextColor(getColor(R.color.blue_4C9EF2));
                mStep3Prompt.setBackgroundResource(R.drawable.shape_circle_cce0f9_stroke_1_4c9ef2);
                mStep3DescTv.setTextColor(getColor(R.color.blue_4C9EF2));

                break;
            case 3:
                // 审核未通过
                mVerifyIcon.setImageResource(R.mipmap.ic_merchant_verify_failed);
                mVerifyDesc.setText(R.string.merchant_verify_faild_desc);
                mVerifyTitle.setText(R.string.merchant_verify_faild_title);
                mStep2DescTv.setText(R.string.merchant_verify_faild_title);
                break;
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant_verify;
    }
}
