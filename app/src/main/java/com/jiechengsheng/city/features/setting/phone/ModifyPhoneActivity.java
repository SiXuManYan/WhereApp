package com.jiechengsheng.city.features.setting.phone;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.base.mvp.BaseMvpActivity;
import com.jiechengsheng.city.features.setting.phone.verify.code.CodeVerifyActivity;
import com.jiechengsheng.city.features.setting.phone.verify.password.PasswordVerifyActivity;
import com.jiechengsheng.city.storage.entity.User;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.utils.FeaturesUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/2/4 14:49.
 * 修改手机号
 */
public class ModifyPhoneActivity extends BaseMvpActivity<ModifyPhonePresenter> implements ModifyPhoneView {

    private TextView current_phone_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_phone;
    }

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        current_phone_tv = findViewById(R.id.current_phone_tv);
    }

    @Override
    protected void initData() {
        presenter = new ModifyPhonePresenter(this);
        User user = User.getInstance();
        String phone = user.phone;
        String countryCode = user.countryCode;
        if (!TextUtils.isEmpty(phone)) {
            current_phone_tv.setText(getString(R.string.account_with_country_format, countryCode, FeaturesUtil.getFormatPhoneNumber(phone)));
        }


    }

    @Override
    protected void bindListener() {
        findViewById(R.id.verify_by_password_tv).setOnClickListener(v -> {
            startActivity(PasswordVerifyActivity.class);
        });

        findViewById(R.id.verify_by_code_tv).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.PARAM_VERIFY_USE_MODE, 0);
            startActivity(CodeVerifyActivity.class, bundle);
        });

    }


    @Override
    protected boolean isStatusDark() {
        return true;
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
