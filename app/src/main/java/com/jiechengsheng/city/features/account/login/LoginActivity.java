package com.jiechengsheng.city.features.account.login;

import static com.jiechengsheng.city.utils.Constant.PARAM_ACCOUNT;
import static com.jiechengsheng.city.utils.Constant.PARAM_COUNTRY_CODE;
import static com.jiechengsheng.city.utils.Constant.PARAM_VERIFY_CODE;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.api.ErrorResponse;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.base.mvp.BaseMvpActivity;
import com.jiechengsheng.city.features.account.bind.BindPhoneActivity;
import com.jiechengsheng.city.features.account.password.PasswordResetActivity;
import com.jiechengsheng.city.features.account.register.RegisterActivity;
import com.jiechengsheng.city.features.web.WebViewActivity;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.google.GooglePlus;

/**
 * Created by Wangsw  2021/1/28 16:43.
 * 注册登录 + 密码登录
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {


    private TextView

            country_tv,
            login_type_tv,
            get_verify_tv,
            forgot_password_tv;

    private Button login_tv;


    private CheckedTextView login_rule_tv;

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
    private AppCompatCheckBox rule_check_cb;
    private ImageView clear_phone_iv;
    private ImageView clear_verify_iv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean dismissBack = bundle.getBoolean(Constant.PARAM_DISMISS_BACK_ICON);
            if (dismissBack) {
                findViewById(R.id.back_iv).setVisibility(View.INVISIBLE);
            }
        }

        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.back_iv));
        BarUtils.setNavBarColor(this, ColorUtils.getColor(R.color.color_002364));
        login_rule_tv = findViewById(R.id.login_rule_tv);
        country_tv = findViewById(R.id.country_tv);
        phone_aet = findViewById(R.id.phone_aet);
        verify_code_aet = findViewById(R.id.verify_code_aet);
        password_aet = findViewById(R.id.password_aet);
        login_mode_vs = findViewById(R.id.login_mode_vs);
        login_type_tv = findViewById(R.id.login_type_tv);
        get_verify_tv = findViewById(R.id.get_verify_tv);
        forgot_password_tv = findViewById(R.id.forgot_password_tv);
        password_rule_iv = findViewById(R.id.password_rule_iv);
        rule_check_cb = findViewById(R.id.rule_check_cb);
        clear_phone_iv = findViewById(R.id.clear_phone_iv);
        clear_verify_iv = findViewById(R.id.clear_verify_iv);
        login_tv = findViewById(R.id.login_tv);

    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter(this);
        presenter.context = this;


        // 默认菲律宾前缀
        country_tv.setText(getString(R.string.country_code_format, "63"));
        // 用户协议，隐私政策
        handleRule(login_rule_tv, getString(R.string.login_rule_default),Color.WHITE);
    }

    private void handleRule(TextView textView, String startString,@ColorInt int color) {

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        String userAgreement = getString(R.string.login_rule_keyword_0);
        String and = getString(R.string.login_rule_default_and);
        String privacyPolicy = getString(R.string.login_rule_keyword_1);
        String disclaimer = getString(R.string.disclaimer_title);


        SpanUtils.with(textView)
                .append(startString)
                .append(userAgreement)
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.Companion.navigation(LoginActivity.this, FeaturesUtil.getUserAgreement(), false);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(color);
                        ds.setUnderlineText(true);
                    }
                })
                .append(and)
                .append(privacyPolicy)
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.Companion.navigation(LoginActivity.this, FeaturesUtil.getPrivacyPolicy(), false);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(color);
                        ds.setUnderlineText(true);
                    }
                })
                .append(and)
                .append(disclaimer)
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        WebViewActivity.Companion.navigation(LoginActivity.this, FeaturesUtil.getDisclaimer(), false);
                    }

                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(color);
                        ds.setUnderlineText(true);
                    }
                })
                .create();
    }

    @Override
    protected void bindListener() {
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        login_type_tv.setOnClickListener(this::onLoginTypeClick);
        get_verify_tv.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                onVerifyGetClick();
            }
        });

        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        forgot_password_tv.setOnClickListener(this::onForgotPasswordClick);
        login_tv.setOnClickListener(this::onLoginClick);
        findViewById(R.id.facebook_login_iv).setOnClickListener(this::onFacebookLoginClick);
        findViewById(R.id.google_login_iv).setOnClickListener(this::onGoogleLoginClick);
        findViewById(R.id.back_iv).setOnClickListener(v -> finish());

        clear_phone_iv.setOnClickListener(v -> phone_aet.setText(""));
        clear_verify_iv.setOnClickListener(v -> verify_code_aet.setText(""));

        phone_aet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    clear_phone_iv.setVisibility(View.GONE);
                    login_tv.setAlpha(0.7f);
                } else {
                    clear_phone_iv.setVisibility(View.VISIBLE);
                    String password = password_aet.getText().toString().trim();
                    String verifyCode = verify_code_aet.getText().toString().trim();
                    if (mIsVerifyMode && !TextUtils.isEmpty(verifyCode)) {
                        login_tv.setAlpha(0.9f);
                    }
                    if (!mIsVerifyMode && !TextUtils.isEmpty(password)) {
                        login_tv.setAlpha(0.9f);
                    }

                }
            }
        });


        password_aet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    if (!mIsVerifyMode) {
                        login_tv.setAlpha(0.7f);
                    }
                } else {
                    String phone = phone_aet.getText().toString().trim();
                    if (!mIsVerifyMode && !TextUtils.isEmpty(phone)) {
                        login_tv.setAlpha(0.9f);
                    }
                }

            }

        });

        verify_code_aet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    if (mIsVerifyMode) {
                        login_tv.setAlpha(0.7f);
                    }
                    clear_verify_iv.setVisibility(View.GONE);
                } else {
                    clear_verify_iv.setVisibility(View.VISIBLE);
                    String phone = phone_aet.getText().toString().trim();
                    if (mIsVerifyMode && !TextUtils.isEmpty(phone)) {
                        login_tv.setAlpha(0.9f);
                    }
                }
            }
        });


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

        login_tv.setClickable(false);

        new Handler(getMainLooper()).postDelayed(() -> login_tv.setClickable(true), 1000);

        if (!rule_check_cb.isChecked()) {
            // 未同意协议
            showRuleDialog();
            return;
        }

        realLogin();
    }

    private void showRuleDialog() {

        String defaultStr = getString(R.string.have_read_and_agree);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_agreement_privacy, null);
        TextView content_tv = view.findViewById(R.id.content_tv);
        TextView agree_tv = view.findViewById(R.id.agree_tv);
        TextView disagree_tv = view.findViewById(R.id.disagree_tv);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();

        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // 更改默认宽度
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(window.getAttributes());
            window.setAttributes(lp);
        }

        handleRule(content_tv, defaultStr,ColorUtils.getColor(R.color.blue_377BFF));
        agree_tv.setOnClickListener(view1 -> {
            rule_check_cb.setChecked(true);
            alertDialog.dismiss();
            realLogin();
        });
        disagree_tv.setOnClickListener(view12 -> {
            alertDialog.dismiss();
        });

    }

    private void realLogin() {
        String account = Objects.requireNonNull(phone_aet.getText()).toString().trim();
        String verifyCode = Objects.requireNonNull(verify_code_aet.getText()).toString().trim();
        String password = Objects.requireNonNull(password_aet.getText()).toString().trim();
        presenter.handleLogin(mIsVerifyMode, mCountryPrefix, account, verifyCode, password);
    }

    /**
     * 获取验证码
     */
    private void onVerifyGetClick() {
        String phone = phone_aet.getText().toString().trim();
        presenter.getVerifyCode(mCountryPrefix, phone, get_verify_tv);
    }

    /**
     * 切换密码显示类型
     */
    private void onPasswordRuleClick(View view) {
        if (mIsCipherText) {
            // 切换至明文
            FeaturesUtil.editOpen(password_aet, password_rule_iv, R.mipmap.ic_login_eye_open_white);
        } else {
            // 切换至密文
            FeaturesUtil.editDismiss(password_aet, password_rule_iv, R.mipmap.ic_login_eye_close_white);
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
    }

    @Override
    public void onEventReceived(BaseEvent<?> baseEvent) {
        super.onEventReceived(baseEvent);
        int code = baseEvent.code;
        if (code == EventCode.EVENT_PASSWORD_RESET_SUCCESS) {
            password_aet.setText("");
        }
        if (code == EventCode.EVENT_LOGIN_SUCCESS) {
            finish();
        }
    }

    private void onFacebookLoginClick(View view) {
        showLoading();
        presenter.threePartyAuthorize(Facebook.NAME);
    }

    private void onGoogleLoginClick(View view) {
        showLoading();
        presenter.threePartyAuthorize(GooglePlus.NAME);
    }


    @Override
    public void guideToAccountBind(PlatformDb platformData, int loginType) {

        String userName = platformData.getUserName();
        String userId = platformData.getUserId();
        String userIcon = platformData.getUserIcon();
        String platformName = platformData.getPlatformNname();

        Bundle bundle = new Bundle();
        bundle.putString(Constant.PARAM_USER_NAME, userName);
        bundle.putString(Constant.PARAM_USER_ID, userId);
        bundle.putString(Constant.PARAM_USER_ICON, userIcon);
        bundle.putString(Constant.PARAM_PLATFORM_NAME, platformName);
        bundle.putInt(Constant.PARAM_THREE_PARTY_LOGIN_TYPE, loginType);
        startActivity(BindPhoneActivity.class, bundle);
    }

    @Override
    public void authorizeCancel() {
        hideLoading();
    }

    @Override
    public void authorizeError() {
        hideLoading();
    }
}
