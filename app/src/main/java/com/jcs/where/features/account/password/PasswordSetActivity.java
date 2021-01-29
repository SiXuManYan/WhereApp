package com.jcs.where.features.account.password;

import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/1/29 16:53.
 * 设置密码
 * 规则:数字+字母 最少6位
 */
public class PasswordSetActivity extends BaseMvpActivity<PasswordSetPresenter> implements PasswordSetView {


    private String mAccount;
    private String mVerifyCode;
    private String mCountryCode;
    private AppCompatEditText password_aet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_password_set;
    }

    @Override
    protected void initView() {
        password_aet = findViewById(R.id.password_aet);
    }

    @Override
    protected void initData() {
        mAccount = getIntent().getStringExtra(Constant.PARAM_ACCOUNT);
        mVerifyCode = getIntent().getStringExtra(Constant.PARAM_VERIFY_CODE);
        mCountryCode = getIntent().getStringExtra(Constant.PARAM_COUNTRY_CODE);
    }

    @Override
    protected void bindListener() {
        findViewById(R.id.register_tv).setOnClickListener(this::onRegisterClick);
    }

    private void onRegisterClick(View view) {

        String password = password_aet.getText().toString().trim();
        presenter.register(mAccount, mVerifyCode, password, mCountryCode);
    }

    @Override
    public void registerSuccess() {
        ToastUtils.showShort(R.string.register_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_LOGIN_SUCCESS));
        finish();
    }
}
