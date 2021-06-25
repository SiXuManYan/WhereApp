package com.jcs.where.features.store.pay.result

import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import kotlinx.android.synthetic.main.activity_store_pay_result.*

/**
 * Created by Wangsw  2021/6/24 16:20.
 *  支付成功
 */
class StorePayResultActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_store_pay_result

    override fun initView() = Unit

    override fun initData() = Unit

    override fun bindListener() {
        check_order_tv.setOnClickListener {

        }
    }
}