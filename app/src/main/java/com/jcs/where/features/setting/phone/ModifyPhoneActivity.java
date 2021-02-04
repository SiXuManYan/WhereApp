package com.jcs.where.features.setting.phone;

import com.jcs.where.R;
import com.jcs.where.base.mvp.BaseMvpActivity;

/**
 * Created by Wangsw  2021/2/4 14:49.
 * 修改手机号
 */
public class ModifyPhoneActivity extends BaseMvpActivity<ModifyPhonePresenter> implements ModifyPhoneView {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_phone;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        presenter = new ModifyPhonePresenter(this);
    }

    @Override
    protected void bindListener() {

    }


}
