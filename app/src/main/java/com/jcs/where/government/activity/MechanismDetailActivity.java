package com.jcs.where.government.activity;

import android.content.Intent;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * 机构详情页
 * create by zyf on 2021/1/2 11:16 AM
 */
public class MechanismDetailActivity extends BaseActivity {
    private int mMechanismId;
    public static final String K_MECHANISM_ID = "mechanismId";

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String temp = intent.getStringExtra(K_MECHANISM_ID);
        if (temp != null) {
            try {
                mMechanismId = Integer.parseInt(temp);
            }catch (NumberFormatException e){
                mMechanismId = 0;
            }
        }
    }

    @Override
    protected void bindListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mechanism_detail;
    }
}
