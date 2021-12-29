package com.jcs.where.features.account.register;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.main.MainActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/1/29 16:53.
 * 注册页，【设置密码】
 */
public class RegisterActivity extends BaseMvpActivity<RegisterPresenter> implements RegisterView {

    private AppCompatEditText
            password_aet,
            password_confirm_aet;

    private ImageView
            password_rule_iv,
            password_confirm_rule_iv;

    private String
            mAccount,
            mVerifyCode,
            mCountryCode;

    /**
     * 密码是否为密文
     */
    private boolean mIsCipherText = true;


    /**
     * 确认密码是否为密文
     */
    private boolean mIsCipherTextConfirm = true;

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_new;
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.iv_back));
        password_aet = findViewById(R.id.password_aet);
        password_confirm_aet = findViewById(R.id.password_confirm_aet);
        password_rule_iv = findViewById(R.id.password_rule_iv);
        password_confirm_rule_iv = findViewById(R.id.password_confirm_rule_iv);
    }

    @Override
    protected void initData() {
        presenter = new RegisterPresenter(this);
        mAccount = getIntent().getStringExtra(Constant.PARAM_ACCOUNT);
        mVerifyCode = getIntent().getStringExtra(Constant.PARAM_VERIFY_CODE);
        mCountryCode = getIntent().getStringExtra(Constant.PARAM_COUNTRY_CODE);
    }

    @Override
    protected void bindListener() {
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        password_confirm_rule_iv.setOnClickListener(this::onConfirmPasswordRuleClick);
        findViewById(R.id.register_tv).setOnClickListener(this::onRegisterClick);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
    }


    private void onPasswordRuleClick(View view) {
        if (mIsCipherText) {
            FeaturesUtil.editOpen(password_aet, password_rule_iv,R.mipmap.ic_login_eye_open_gray);
        } else {
            FeaturesUtil.editDismiss(password_aet, password_rule_iv,R.mipmap.ic_login_eye_close_gray);
        }
        mIsCipherText = !mIsCipherText;
    }

    private void onConfirmPasswordRuleClick(View view) {
        if (mIsCipherTextConfirm) {
            FeaturesUtil.editOpen(password_confirm_aet, password_confirm_rule_iv);
        } else {
            FeaturesUtil.editDismiss(password_confirm_aet, password_confirm_rule_iv);
        }
        mIsCipherTextConfirm = !mIsCipherTextConfirm;
    }

    private void onRegisterClick(View view) {
        String password = password_aet.getText().toString().trim();
        String passwordConfirm = password_confirm_aet.getText().toString().trim();
        presenter.register(mAccount, mVerifyCode, mCountryCode, password, passwordConfirm);
    }


    @Override
    public void registerSuccess() {
        ToastUtils.showShort(R.string.register_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_LOGIN_SUCCESS));
//        startActivityClearTop(MainActivity.class, null);
        finish();
    }
}
