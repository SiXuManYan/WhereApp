package com.jcs.where.features.setting.phone.verify.password;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;

/**
 * Created by Wangsw  2021/2/4 15:09.
 * 修改手机号，密码验证
 */
public class PasswordVerifyActivity extends BaseMvpActivity<PasswordVerifyPresenter> implements PasswordVerifyView {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_by_password;
    }
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
            presenter = new PasswordVerifyPresenter(this);
    }

    @Override
    protected void bindListener() {

    }

}
