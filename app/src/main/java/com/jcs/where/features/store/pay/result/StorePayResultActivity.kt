package com.jcs.where.features.store.pay.result

import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.bills.hydropower.record.PaymentRecordActivity
import com.jcs.where.home.HomeActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_pay_result.*

/**
 * Created by Wangsw  2021/6/24 16:20.
 *  支付成功
 */
class StorePayResultActivity : BaseActivity() {

    /**
     * 0 商城订单
     * 1 水电订单
     */
    private var useType = 0


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_pay_result

    override fun initView() {


        intent.extras?.let {

            useType = it.getInt(Constant.PARAM_TYPE)
        }

    }

    override fun initData() = Unit

    override fun bindListener() {
        check_order_tv.setOnClickListener {

            if (useType == Constant.PAY_INFO_ESTORE) {
                startActivityClearTop(HomeActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_TAB, 2)
                })
            }

            if (useType == Constant.PAY_INFO_ESTORE_BILLS) {
                startActivityClearTop(PaymentRecordActivity::class.java, null)
            }
            finish()
        }
    }
}