package com.jcs.where.features.account.password;

import static com.jcs.where.utils.Constant.PARAM_ACCOUNT;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/1/30 10:33.
 * 忘记密码（密码重置）
 */
public class PasswordResetActivity extends BaseMvpActivity<PasswordResetPresenter> implements PasswordResetView {

    private AppCompatEditText
            phone_aet,
            verify_code_aet,
            password_aet;


    private TextView
            get_verify_tv,
            reset_tv,
            country_tv;

    private ImageView password_rule_iv;
    private ImageView clear_verify_iv;


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
        return R.layout.activity_password_reset;
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.iv_back));
        BarUtils.setNavBarColor(this, ColorUtils.getColor(R.color.grey_F5F5F5));
        phone_aet = findViewById(R.id.phone_aet);
        verify_code_aet = findViewById(R.id.verify_code_aet);
        password_aet = findViewById(R.id.password_aet);
        get_verify_tv = findViewById(R.id.get_verify_tv);
        password_rule_iv = findViewById(R.id.password_rule_iv);
        clear_verify_iv = findViewById(R.id.clear_verify_iv);
        country_tv = findViewById(R.id.country_tv);
        reset_tv = findViewById(R.id.reset_tv);
    }

    @Override
    protected void initData() {
        presenter = new PasswordResetPresenter(this);
        String account = getIntent().getStringExtra(PARAM_ACCOUNT);
        if (!TextUtils.isEmpty(account)) {
            phone_aet.setText(account);
            phone_aet.setSelection(account.length());
        }
        // 默认菲律宾前缀
        country_tv.setText(getString(R.string.country_code_format, "63"));
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void bindListener() {
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        get_verify_tv.setOnClickListener(this::onVerifyGetClick);
        reset_tv.setOnClickListener(this::onResetClick);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());


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
                    reset_tv.setAlpha(0.7f);
                } else {
                    String verify = verify_code_aet.getText().toString().trim();
                    String password = password_aet.getText().toString().trim();

                    if (TextUtils.isEmpty(verify) || TextUtils.isEmpty(password)) {
                        reset_tv.setAlpha(0.7f);
                    } else {
                        reset_tv.setAlpha(0.9f);
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
                    clear_verify_iv.setVisibility(View.GONE);
                    reset_tv.setAlpha(0.7f);
                } else {
                    clear_verify_iv.setVisibility(View.VISIBLE);


                    String phone = phone_aet.getText().toString().trim();
                    String password = password_aet.getText().toString().trim();

                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                        reset_tv.setAlpha(0.7f);
                    } else {
                        reset_tv.setAlpha(0.9f);
                    }

                }
            }
        });
        clear_verify_iv.setOnClickListener(v -> verify_code_aet.setText(""));



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
                    clear_verify_iv.setVisibility(View.GONE);
                    reset_tv.setAlpha(0.7f);
                } else {
                    clear_verify_iv.setVisibility(View.VISIBLE);

                    String phone = phone_aet.getText().toString().trim();
                    String verify_code = verify_code_aet.getText().toString().trim();

                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verify_code)) {
                        reset_tv.setAlpha(0.7f);
                    } else {
                        reset_tv.setAlpha(0.9f);
                    }

                }
            }
        });


    }


    private void onPasswordRuleClick(View view) {
        if (mIsCipherText) {
            // 切换至明文
            FeaturesUtil.editOpen(password_aet, password_rule_iv, R.mipmap.ic_login_eye_open_gray);
        } else {
            // 切换至密文
            FeaturesUtil.editDismiss(password_aet, password_rule_iv, R.mipmap.ic_login_eye_close_gray);
        }
        mIsCipherText = !mIsCipherText;
    }

    /**
     * 重置密码
     *
     * @param view
     */
    private void onResetClick(View view) {
        String account = phone_aet.getText().toString().trim();
        String verifyCode = verify_code_aet.getText().toString().trim();
        String newPassword = password_aet.getText().toString().trim();
        presenter.resetPassword(mCountryPrefix, account, verifyCode, newPassword);
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
     * 获取验证码
     *
     * @param view
     */
    private void onVerifyGetClick(View view) {
        String phone = phone_aet.getText().toString().trim();
        presenter.getVerifyCode(mCountryPrefix, phone, get_verify_tv);
    }


    @Override
    public void passwordResetSuccess() {
        ToastUtils.showShort(R.string.password_reset_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_PASSWORD_RESET_SUCCESS));
        finish();
    }
}
