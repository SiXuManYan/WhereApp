package com.jcs.where.mine.activity;

import android.widget.TextView;

import com.jcs.where.BuildConfig;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;

/**
 * Created by Wangsw  2021/2/3 16:48.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        TextView version_tv = (TextView) findViewById(R.id.version_tv);
        version_tv.setText(getString(R.string.version_format, BuildConfig.VERSION_NAME));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindListener() {

    }


}
