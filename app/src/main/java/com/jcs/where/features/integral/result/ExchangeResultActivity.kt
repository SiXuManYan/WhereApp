package com.jcs.where.features.integral.result

import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.coupon.user.MyCouponActivity
import com.jcs.where.features.integral.record.IntegralRecordActivity
import kotlinx.android.synthetic.main.activity_exchange_result.*

/**
 * Created by Wangsw  2023/2/10 16:03.
 *
 */
class ExchangeResultActivity : BaseActivity() {

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_exchange_result

    override fun initView() {

    }

    override fun initData() = Unit

    override fun bindListener() {
        view_coupon_tv.setOnClickListener {
            startActivityAfterLogin(MyCouponActivity::class.java)
        }
    }
}