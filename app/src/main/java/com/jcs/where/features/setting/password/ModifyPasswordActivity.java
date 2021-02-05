package com.jcs.where.features.setting.password;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ToastUtils;
import com.jcs.where.R;
import com.jcs.where.base.BaseEvent;
import com.jcs.where.base.EventCode;
import com.jcs.where.base.mvp.BaseMvpActivity;
import com.jcs.where.features.account.login.LoginActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.FeaturesUtil;
import com.jcs.where.utils.SPKey;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wangsw  2021/2/4 14:34.
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseMvpActivity<ModifyPasswordPresenter> implements ModifyPasswordView {


    private AppCompatEditText
            old_password_aet,
            new_password_aet;
    private ImageView
            old_password_rule_iv,
            new_password_rule_iv;

    /**
     * 是否为密文
     */
    private boolean mOldIsCipherText = true;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    protected void initView() {
        old_password_aet = findViewById(R.id.old_password_aet);
        new_password_aet = findViewById(R.id.new_password_aet);
        old_password_rule_iv = findViewById(R.id.old_password_rule_iv);
        new_password_rule_iv = findViewById(R.id.new_password_rule_iv);
    }

    @Override
    protected void initData() {
        presenter = new ModifyPasswordPresenter(this);
    }

    @Override
    protected void bindListener() {
        old_password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        new_password_rule_iv.setOnClickListener(this::onPasswordRuleClick);
        findViewById(R.id.confirm_tv).setOnClickListener(this::onConfirmClick);
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    /**
     * 切换密码显示类型
     */
    private void onPasswordRuleClick(View view) {
        if (mOldIsCipherText) {
            // 切换至明文
            FeaturesUtil.editOpen(old_password_aet, old_password_rule_iv, R.mipmap.ic_login_eye_open_gray);
            FeaturesUtil.editOpen(new_password_aet, new_password_rule_iv, R.mipmap.ic_login_eye_open_gray);
        } else {
            // 切换至密文
            FeaturesUtil.editDismiss(old_password_aet, old_password_rule_iv, R.mipmap.ic_login_eye_close_gray);
            FeaturesUtil.editDismiss(new_password_aet, new_password_rule_iv, R.mipmap.ic_login_eye_close_gray);
        }
        mOldIsCipherText = !mOldIsCipherText;
    }


    private void onConfirmClick(View view) {
        String oldPassword = old_password_aet.getText().toString().trim();
        String newPassword = new_password_aet.getText().toString().trim();
        presenter.modifyPassword(oldPassword, newPassword);
    }


    @Override
    public void modifyPasswordSuccess() {
        ToastUtils.showShort(R.string.password_reset_success);
        EventBus.getDefault().post(new BaseEvent<>(EventCode.EVENT_REFRESH_USER_INFO));
        finish();
    }
}
