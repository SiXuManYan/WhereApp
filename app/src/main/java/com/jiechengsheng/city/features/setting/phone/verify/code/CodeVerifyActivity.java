package com.jiechengsheng.city.features.setting.phone.verify.code;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.base.mvp.BaseMvpActivity;
import com.jiechengsheng.city.features.setting.phone.confirm.NewPhoneActivity;
import com.jiechengsheng.city.utils.Constant;
import com.jiechengsheng.city.widget.verify.VerificationCodeView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/2/4 15:12.
 * 验证码验证
 */
public class CodeVerifyActivity extends BaseMvpActivity<CodeVerifyPresenter> implements CodeVerifyView {

    /**
     * 0 验证旧版手机号
     * 1 验证新手机号
     */
    private int mUseMode = 0;
    private String mNewAccount = "";
    private String mNewAccountCountryCode = "";


    private VerificationCodeView captcha_view;
    private TextView resend_tv, target_tv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_by_code;
    }

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        captcha_view = findViewById(R.id.captcha_view);
        resend_tv = findViewById(R.id.resend_tv);
        target_tv = findViewById(R.id.target_tv);
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
            mJcsTitle.setMiddleTitle(getString(R.string.modify_phone));
        }
        getVerifyCode();
    }

    private void getVerifyCode() {
        presenter.getVerifyCode(resend_tv, target_tv);
    }

    @Override
    protected void bindListener() {
        captcha_view.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {

            @Override
            public void onTextChange(View view, String content) {
            }

            @Override
            public void onComplete(View view, String content) {
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                presenter.chekVerifyCode(content);
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

    @Override
    public void verified() {
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
}
