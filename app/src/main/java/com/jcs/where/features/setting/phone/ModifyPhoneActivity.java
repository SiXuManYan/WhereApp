package com.jcs.where.features.setting.phone;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.setting.phone.verify.code.CodeVerifyActivity;
import com.jcs.where.features.setting.phone.verify.password.PasswordVerifyActivity;
import com.jcs.where.storage.entity.User;
import com.jcs.where.utils.Constant;

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
        current_phone_tv = findViewById(R.id.current_phone_tv);
    }

    @Override
    protected void initData() {
        presenter = new ModifyPhonePresenter(this);
        String phone = User.getInstance().phone;
        if (!TextUtils.isEmpty(phone)) {
            current_phone_tv.setText(phone);
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
            startActivity(CodeVerifyActivity.class,bundle);
        });

    }


    @Override
    protected boolean isStatusDark() {
        return true;
    }


}