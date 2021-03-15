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
        StringBuffer buffer = new StringBuffer(BuildConfig.VERSION_NAME);
        if (BuildConfig.FLAVOR.equals("dev")) {
            buffer.append("test");
        }
        version_tv.setText(getString(R.string.version_format, buffer.toString()));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void bindListener() {

    }


}
