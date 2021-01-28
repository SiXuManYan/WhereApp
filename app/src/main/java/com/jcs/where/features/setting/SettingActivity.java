package com.jcs.where.features.setting;

import android.view.View;

import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * Created by Wangsw  2021/1/28 15:13.
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {
        findViewById(R.id.sign_out_tv).setOnClickListener(this::onSignOutClick);
    }

    private void onSignOutClick(View view) {

    }


}
