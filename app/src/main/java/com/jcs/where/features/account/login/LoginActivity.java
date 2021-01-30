package com.jcs.where.features.account.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.currency.WebViewActivity;
import com.jcs.where.features.account.password.PasswordResetActivity;
import com.jcs.where.features.account.register.RegisterActivity;
import com.jcs.where.frams.common.Html5Url;
import com.jcs.where.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;

import static com.jcs.where.utils.Constant.PARAM_ACCOUNT;
import static com.jcs.where.utils.Constant.PARAM_COUNTRY_CODE;
import static com.jcs.where.utils.Constant.PARAM_VERIFY_CODE;

/**
 * Created by Wangsw  2021/1/28 16:43.
 * 注册登录 + 密码登录
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {


    private TextView
            login_rule_tv,
            country_tv,
            login_type_tv,
            get_verify_tv,
            error_hint_tv,
            forgot_password_tv;

    private AppCompatEditText
            phone_aet,
            verify_code_aet,
            password_aet;

    private ImageView password_rule_iv;

    private ViewSwitcher login_mode_vs;

    /**
     * 是否为验证码模式
     */
    private boolean mIsVerifyMode = true;

    /**
     * 是否为密文
     */
    private boolean mIsCipherText = true;

    /**
     * 国家码
     * 默认 菲律宾+63前缀
     */
    private String mCountryPrefix = StringUtils.getStringArray(R.array.country_prefix)[0];

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.iv_back));
        BarUtils.setNavBarColor(this, ColorUtils.getColor(R.color.blue_395668));
        login_rule_tv = findViewById(R.id.login_rule_tv);
        country_tv = findViewById(R.id.country_tv);
        phone_aet = findViewById(R.id.phone_aet);
        verify_code_aet = findViewById(R.id.verify_code_aet);
        password_aet = findViewById(R.id.password_aet);
        login_mode_vs = findViewById(R.id.login_mode_vs);
        login_type_tv = findViewById(R.id.login_type_tv);
        get_verify_tv = findViewById(R.id.get_verify_tv);
        error_hint_tv = findViewById(R.id.error_hint_tv);
        forgot_password_tv = findViewById(R.id.forgot_password_tv);
        password_rule_iv = findViewById(R.id.password_rule_iv);

    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter(this);

        // 默认菲律宾前缀
        country_tv.setText(getString(R.string.country_code_format, "63"));
        // 用户协议，隐私政策 (url 替换)
        login_rule_tv.setMovementMethod(LinkMovementMethod.getInstance());
        String defaultStr = getString(R.string.login_rule_default);
        String keyWord0 = getString(R.string.login_rule_keyword_0);
        String and = getString(R.string.login_rule_default_and);
        String keyWord1 = getString(R.string.login_rule_keyword_1);
        SpanUtils.with(login_rule_tv)
                .append(defaultStr)
                .append(keyWord0)
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(LoginActivity.this, Html5Url.USER_AGREEMENT_URL);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.WHITE);
                        ds.setUnderlineText(false);
                    }
                })
                .append(and)
                .append(keyWord1)
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.goTo(LoginActivity.this, Html5Url.PRIVACY_POLICY);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(Color.WHITE);
                        ds.setUnderlineText(false);
                    }
                }).create();


    }

    @Override
    protected void bindListener() {
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        login_type_tv.setOnClickListener(this::onLoginTypeClick);
        get_verify_tv.setOnClickListener(this::onVerifyGetClick);
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        forgot_password_tv.setOnClickListener(this::onForgotPasswordClick);
        findViewById(R.id.login_tv).setOnClickListener(this::onLoginClick);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

    }


    /**
     * 选择国家开头
     */
    private void onCountryPrefixClick(View v) {
      FeaturesUtil.getCountryPrefix(this, countryCode -> {
          mCountryPrefix = countryCode;
          country_tv.setText(getString(R.string.country_code_format, countryCode));
      });

    }

    /**
     * 切换登录模式
     */
    private void onLoginTypeClick(View view) {
        if (mIsVerifyMode) {

            // 切换为密码登录
            login_mode_vs.setDisplayedChild(1);
            login_type_tv.setText(R.string.verify_login);

            forgot_password_tv.setVisibility(View.VISIBLE);
        } else {
            // 切换为验证码登录
            login_mode_vs.setDisplayedChild(0);
            login_type_tv.setText(R.string.password_login);
            forgot_password_tv.setVisibility(View.GONE);
        }
        mIsVerifyMode = !mIsVerifyMode;
    }

    /**
     * 登录
     */
    private void onLoginClick(View view) {

        String account = phone_aet.getText().toString().trim();
        String verifyCode = verify_code_aet.getText().toString().trim();
        String password = password_aet.getText().toString().trim();
        presenter.handleLogin(mIsVerifyMode, mCountryPrefix, account, verifyCode, password);
    }


    /**
     * 获取验证码
     */
    private void onVerifyGetClick(View view) {
        String phone = phone_aet.getText().toString().trim();
        presenter.getVerifyCode(mCountryPrefix, phone, get_verify_tv);
    }

    /**
     *  切换密码显示类型
     */
    private void onPasswordRuleClick(View view) {
        if (mIsCipherText) {
            // 切换至明文
            FeaturesUtil.editOpen(password_aet, password_rule_iv);
        } else {
            // 切换至密文
            FeaturesUtil.editDismiss(password_aet, password_rule_iv);
        }
        mIsCipherText = !mIsCipherText;
    }

    /**
     * 忘记密码
     */
    private void onForgotPasswordClick(View view) {
        String account = phone_aet.getText().toString().trim();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACCOUNT, account);
        startActivity(PasswordResetActivity.class, bundle);
    }


    @Override
    public void LoginSuccess() {
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_LOGIN_SUCCESS));
        ToastUtils.showShort(getString(R.string.login_success));
        finish();
    }


    @Override
    public void guideRegister(String account, String verifyCode) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ACCOUNT, account);
        bundle.putString(PARAM_VERIFY_CODE, verifyCode);
        bundle.putString(PARAM_COUNTRY_CODE, mCountryPrefix);
        startActivity(RegisterActivity.class, bundle);
    }

    @Override
    public void onError(ErrorResponse errorResponse) {
        super.onError(errorResponse);
        error_hint_tv.setText(errorResponse.getErrMsg());
    }

    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        super.onEventReceived(baseEvent);
        int code = baseEvent.code;
        switch (code) {
            case EventCode.EVENT_LOGIN_SUCCESS:
                // 注册成功关闭页面
                finish();
                break;
            case EventCode.EVENT_PASSWORD_RESET_SUCCESS:
                password_aet.setText("");
                break;
            default:
                break;
        }
    }
}
