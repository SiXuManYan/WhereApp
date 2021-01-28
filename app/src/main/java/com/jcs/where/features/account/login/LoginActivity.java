package com.jcs.where.features.account.login;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;

/**
 * Created by Wangsw  2021/1/28 16:43.
 * 注册登录 + 密码登录
 */
public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        presenter = new LoginPresenter(this);
    }

    @Override
    protected void bindListener() {

    }


}
