package com.jcs.where.features.payment.result

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2022/4/24 17:11.
 * 支付结果页面
 */
class WebPayResultActivity  :BaseMvpActivity <WebPayResultPresenter>(),WebPayResultView{


    override fun getLayoutId() = R.layout.activity_web_pay_result
    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() = Unit

    override fun bindListener() {
        TODO("Not yet implemented")
    }
}