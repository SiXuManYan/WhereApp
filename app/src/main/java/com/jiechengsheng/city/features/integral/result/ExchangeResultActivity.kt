package com.jiechengsheng.city.features.integral.result

import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseActivity
import com.jiechengsheng.city.features.coupon.user.MyCouponActivity
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