package com.jiechengsheng.city.features.setting.phone.verify.password;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.jiechengsheng.city.R;
import com.jiechengsheng.city.base.BaseEvent;
import com.jiechengsheng.city.base.EventCode;
import com.jiechengsheng.city.base.mvp.BaseMvpActivity;
import com.jiechengsheng.city.features.setting.phone.confirm.NewPhoneActivity;
import com.jiechengsheng.city.utils.FeaturesUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Wangsw  2021/2/4 15:09.
 * 修改手机号，密码验证
 */
public class PasswordVerifyActivity extends BaseMvpActivity<PasswordVerifyPresenter> implements PasswordVerifyView {


    private ImageView password_rule_iv;
    private AppCompatEditText password_aet;
    private TextView confirm_tv;

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
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white));
        password_rule_iv = findViewById(R.id.password_rule_iv);
        password_aet = findViewById(R.id.password_aet);
        confirm_tv = findViewById(R.id.confirm_tv);
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
        confirm_tv.setOnClickListener(this::onConfirmClick);
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
                    confirm_tv.setAlpha(0.5f);
                }


            }
        });
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
