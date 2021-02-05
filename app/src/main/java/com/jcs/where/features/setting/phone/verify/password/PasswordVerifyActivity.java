package com.jcs.where.features.setting.phone.verify.password;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;

import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.setting.phone.confirm.NewPhoneActivity;
import com.jcs.where.utils.FeaturesUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/2/4 15:09.
 * 修改手机号，密码验证
 */
public class PasswordVerifyActivity extends BaseMvpActivity<PasswordVerifyPresenter> implements PasswordVerifyView {


    private ImageView password_rule_iv;
    private AppCompatEditText password_aet;

    /**
     * 是否为密文
     */
    private boolean mOldIsCipherText = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_by_password;
    }

    @Override
    protected void initView() {
        password_rule_iv = findViewById(R.id.password_rule_iv);
        password_aet = findViewById(R.id.password_aet);
    }

    @Override
    protected void initData() {
        presenter = new PasswordVerifyPresenter(this);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }


    @Override
    protected void bindListener() {
        password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        findViewById(R.id.confirm_tv).setOnClickListener(this::onConfirmClick);
    }


    /**
     * 切换密码显示类型
     */
    private void onPasswordRuleClick(View view) {
        if (mOldIsCipherText) {
            // 切换至明文
            FeaturesUtil.editOpen(password_aet, password_rule_iv, R.mipmap.ic_login_eye_open_gray);
        } else {
            // 切换至密文
            FeaturesUtil.editDismiss(password_aet, password_rule_iv, R.mipmap.ic_login_eye_close_gray);
        }
        mOldIsCipherText = !mOldIsCipherText;
    }

    private void onConfirmClick(View view) {
        String password = password_aet.getText().toString().trim();
        presenter.checkPassword(password);
    }

    @Override
    public void passwordCheckPass() {
        startActivity(NewPhoneActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(BaseEvent<?> baseEvent) {

        int code = baseEvent.code;
        switch (code) {
            case EventCode.EVENT_REFRESH_USER_INFO:
                // 手机号更改成功
                finish();
                break;
            default:
                break;
        }
    }
}
