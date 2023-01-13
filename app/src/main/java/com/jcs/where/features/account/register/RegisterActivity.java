package com.jcs.where.features.account.register;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
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
    private TextView register_tv;

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
        BarUtils.addMarginTopEqualStatusBarHeight(findViewById(R.id.back_iv));
        password_aet = findViewById(R.id.password_aet);
        password_confirm_aet = findViewById(R.id.password_confirm_aet);
        password_rule_iv = findViewById(R.id.password_rule_iv);
        password_confirm_rule_iv = findViewById(R.id.password_confirm_rule_iv);
        register_tv = findViewById(R.id.register_tv);
    }

    @Override
    protected void initData() {
        presenter = new RegisterPresenter(this);
        presenter.context = this;
        mAccount = getIntent().getStringExtra(Constant.PARAM_ACCOUNT);
        mVerifyCode = getIntent().getStringExtra(Constant.PARAM_VERIFY_CODE);
        mCountryCode = getIntent().getStringExtra(Constant.PARAM_COUNTRY_CODE);
    }

    @Override
    protected void bindListener() {
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        password_confirm_rule_iv.setOnClickListener(this::onConfirmPasswordRuleClick);

        register_tv.setOnClickListener(this::onRegisterClick);

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
                    register_tv.setAlpha(0.7f);
                } else {
                    String confirm = password_confirm_aet.getText().toString().trim();
                    if (!TextUtils.isEmpty(confirm)) {
                        register_tv.setAlpha(1.0f);
                    }


                }
            }
        });

        password_confirm_aet.addTextChangedListener(new TextWatcher() {
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
                    register_tv.setAlpha(0.7f);
                } else {
                    String password = password_aet.getText().toString().trim();
                    if (!TextUtils.isEmpty(password)) {
                        register_tv.setAlpha(1.0f);
                    }


                }
            }
        });

    }


    private void onPasswordRuleClick(View view) {
        if (mIsCipherText) {
            FeaturesUtil.editOpen(password_aet, password_rule_iv, R.mipmap.ic_login_eye_open_gray);
        } else {
            FeaturesUtil.editDismiss(password_aet, password_rule_iv, R.mipmap.ic_login_eye_close_gray);
        }
        mIsCipherText = !mIsCipherText;
    }

    private void onConfirmPasswordRuleClick(View view) {
        if (mIsCipherTextConfirm) {
            FeaturesUtil.editOpen(password_confirm_aet, password_confirm_rule_iv, R.mipmap.ic_login_eye_open_gray);
        } else {
            FeaturesUtil.editDismiss(password_confirm_aet, password_confirm_rule_iv, R.mipmap.ic_login_eye_close_gray);
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
