package com.jcs.where.features.pay

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2021/4/22 17:12.
 * 支付
 */
class PayActivity :BaseMvpActivity< PayPresenter>(), PayView{

    override fun getLayoutId() = R.layout.activity_pay

    override fun initView() {

    }

    override fun initData() {
    }

    override fun bindListener() {
    }


}