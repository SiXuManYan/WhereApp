package com.jiechengsheng.city.features.setting.phone.confirm;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseActivity;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.features.setting.phone.verify.code.CodeVerifyActivity;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        EventBus.getDefault().register(this);
        country_tv = findViewById(R.id.country_tv);
        phone_aet = findViewById(R.id.phone_aet);
    }

    @Override
    protected void initData() {
        country_tv.setText(getString(R.string.country_code_format, "63"));
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void bindListener() {
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        findViewById(R.id.send_verify_tv).setOnClickListener(v -> {
            String account = phone_aet.getText().toString().trim();
            if (FeaturesUtil.isWrongPhoneNumber(mCountryPrefix, account)) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

        int code = baseEvent.code;
        switch (code) {
            case EventCode.EVENT_REFRESH_USER_INFO:
                // 手机号更改成功
                finish();
                break;
            default:
                break;
        }
    }


}
