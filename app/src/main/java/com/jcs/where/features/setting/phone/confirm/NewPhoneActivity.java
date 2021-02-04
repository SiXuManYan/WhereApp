package com.jcs.where.features.setting.phone.confirm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.StringUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.features.setting.phone.verify.code.CodeVerifyActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;

/**
 * Created by Wangsw  2021/2/4 15:24.
 * 确认新手机号
 */
public class NewPhoneActivity extends BaseActivity {

    private TextView country_tv;
    private AppCompatEditText phone_aet;
    /**
     * 国家码
     * 默认 菲律宾+63前缀
     */
    private String mCountryPrefix = StringUtils.getStringArray(R.array.country_prefix)[0];

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_phone_activity;
    }

    @Override
    protected void initView() {
        country_tv = findViewById(R.id.country_tv);
        phone_aet = findViewById(R.id.phone_aet);
    }

    @Override
    protected void initData() {
        country_tv.setText(getString(R.string.country_code_format, "63"));
    }

    @Override
    protected void bindListener() {
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        findViewById(R.id.send_verify_tv).setOnClickListener(v -> {
            String account = phone_aet.getText().toString().trim();
            if (FeaturesUtil.isWrongPhoneNumber(account, mCountryPrefix)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PARAM_VERIFY_USE_MODE, 1);
            bundle.putString(Constant.PARAM_ACCOUNT, account);
            bundle.putString(Constant.PARAM_COUNTRY_CODE, mCountryPrefix);
            startActivity(CodeVerifyActivity.class, bundle);
        });
    }

    private void onCountryPrefixClick(View view) {
        FeaturesUtil.getCountryPrefix(this, countryCode -> {
            mCountryPrefix = countryCode;
            country_tv.setText(getString(R.string.country_code_format, countryCode));
        });
    }


}
