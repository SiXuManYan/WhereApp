package com.jcs.where.features.setting.phone.verify.code;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.features.setting.phone.confirm.NewPhoneActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.SPKey;
import com.jcs.where.widget.JcsTitle;
import com.jcs.where.widget.verify.VerificationCodeView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/2/4 15:12.
 * 验证码验证
 */
public class CodeVerifyActivity extends BaseMvpActivity<CodeVerifyPresenter> implements CodeVerifyView {

    /**
     * 0 验证旧版手机号 1 验证新手机号
     */
    private int mUseMode = 0;
    private String mNewAccount = "";
    private String mNewAccountCountryCode = "";


    private VerificationCodeView captcha_view;
    private TextView resend_tv;
    private JcsTitle jcsTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_by_code;
    }

    @Override
    protected void initView() {
        captcha_view = findViewById(R.id.captcha_view);
        resend_tv = findViewById(R.id.resend_tv);
        jcsTitle = findViewById(R.id.jcsTitle);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected void initData() {
        presenter = new CodeVerifyPresenter(this);
        mUseMode = getIntent().getIntExtra(Constant.PARAM_VERIFY_USE_MODE, 0);
        mNewAccount = getIntent().getStringExtra(Constant.PARAM_ACCOUNT);
        mNewAccountCountryCode = getIntent().getStringExtra(Constant.PARAM_COUNTRY_CODE);

        if (mUseMode == 1) {
            jcsTitle.setMiddleTitle(getString(R.string.modify_phone));
        }
        getVerifyCode();
    }

    private void getVerifyCode() {
        presenter.getVerifyCode(resend_tv);
    }

    @Override
    protected void bindListener() {
        captcha_view.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {

            @Override
            public void onTextChange(View view, String content) { }

            @Override
            public void onComplete(View view, String content) {
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                switch (mUseMode) {
                    case 0:
                        startActivity(NewPhoneActivity.class);
                        finish();
                        break;
                    case 1:
                        // 进行手机号更换
                        presenter.modifyPhone(mNewAccount, mNewAccountCountryCode);
                        break;
                    default:
                        break;
                }


            }
        });
        resend_tv.setOnClickListener(v -> {
            getVerifyCode();
        });
    }


    @Override
    public void modifyPhoneSuccess() {
        ToastUtils.showShort(R.string.phone_reset_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_USER_INFO));
        finish();
    }
}
