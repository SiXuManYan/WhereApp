package com.jcs.where.features.pay

import com.jcs.where.R
import com.jcs.where.api.response.gourmet.order.OrderResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_pay.*

/**
 * Created by Wangsw  2021/4/22 17:12.
 * 支付
 */
class PayActivity : BaseMvpActivity<PayPresenter>(), PayView {

    private lateinit var totalPrice: String
    private lateinit var orders: ArrayList<OrderResponse>

    override fun getLayoutId() = R.layout.activity_pay

    override fun initView() {
        orders = intent.getParcelableArrayListExtra<OrderResponse>(Constant.PARAM_DATA)!!
        totalPrice = intent.getStringExtra(Constant.PARAM_TOTAL_PRICE)!!

        val price = getString(R.string.price_unit_format, totalPrice)
        total_tv.text = price
        pay_tv.text = price
    }

    override fun isStatusDark() = true

    override fun initData() {

    }

    override fun bindListener() {
        pay_tv.setOnClickListener {
            showComing()
        }
    }


}