package com.jcs.where.features.account.bind;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.utils.Constant;
import com.jcs.where.utils.FeaturesUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/2/1 16:13.
 * 第三方登录的新用户绑定手机号
 */
public class BindPhoneActivity extends BaseMvpActivity<BindPhonePresenter> implements BindPhoneView {


    private ImageView password_rule_iv;

    private TextView
            get_verify_tv,
            country_tv;

    private AppCompatEditText
            phone_aet,
            verify_code_aet,
            password_aet;

    /**
     * 是否为密文
     */
    private boolean mIsCipherText = true;

    /**
     * 国家码
     * 默认 菲律宾+63前缀
     */
    private String mCountryPrefix = StringUtils.getStringArray(R.array.country_prefix)[0];
    private String mUserName;
    private String mUserId;
    private String mUserIcon;
    private int mLoginType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView() {
        phone_aet = findViewById(R.id.phone_aet);
        verify_code_aet = findViewById(R.id.verify_code_aet);
        password_aet = findViewById(R.id.password_aet);
        get_verify_tv = findViewById(R.id.get_verify_tv);
        password_rule_iv = findViewById(R.id.password_rule_iv);
        country_tv = findViewById(R.id.country_tv);
    }

    @Override
    protected void initData() {
        presenter = new BindPhonePresenter(this);
        Bundle bundle = getIntent().getExtras();
        mUserName = bundle.getString(Constant.PARAM_USER_NAME);
        mUserId = bundle.getString(Constant.PARAM_USER_ID);
        mUserIcon = bundle.getString(Constant.PARAM_USER_ICON);
        mLoginType = bundle.getInt(Constant.PARAM_THREE_PARTY_LOGIN_TYPE);
    }

    @Override
    protected void bindListener() {
        get_verify_tv.setOnClickListener(this::onVerifyGetClick);
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        country_tv.setOnClickListener(this::onCountryPrefixClick);
        findViewById(R.id.bind_tv).setOnClickListener(this::onBindClick);
    }

    /**
     * 获取验证码
     */
    private void onVerifyGetClick(View view) {
        String phone = phone_aet.getText().toString().trim();
        presenter.getVerifyCode(mCountryPrefix, phone, get_verify_tv);
    }

    /**
     * 切换密码显示类型
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
     * 选择国家开头
     */
    private void onCountryPrefixClick(View v) {
        FeaturesUtil.getCountryPrefix(this, countryCode -> {
            mCountryPrefix = countryCode;
            country_tv.setText(getString(R.string.country_code_format, countryCode));
        });
    }


    private void onBindClick(View view) {

        String account = phone_aet.getText().toString().trim();
        String verifyCode = verify_code_aet.getText().toString().trim();
        String password = password_aet.getText().toString().trim();
        presenter.bindPhone(mCountryPrefix, account, verifyCode, password, mUserName, mUserId, mUserIcon,mLoginType);
    }

    @Override
    public void bindSuccess() {
        ToastUtils.showShort(R.string.bind_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_BIND_PHONE_SUCCESS));
        finish();
    }
}
