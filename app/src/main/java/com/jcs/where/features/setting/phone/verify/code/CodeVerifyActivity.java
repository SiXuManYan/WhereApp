package com.jcs.where.features.setting.phone.verify.code;

import com.jcs.where.base.mvp.BaseMvpActivity;

/**
 * Created by Wangsw  2021/2/4 15:12.
 * 修改手机号 -通过验证码验证
 */
public class CodeVerifyActivity extends BaseMvpActivity<CodeVerifyPresenter> implements CodeVerifyView {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        presenter = new CodeVerifyPresenter(this);
    }

    @Override
    protected void bindListener() {

    }


}
