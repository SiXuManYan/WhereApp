package com.jcs.where.features.bills

import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pay_bills.*

/**
 * Created by Wangsw  2021/4/15 14:12.
 * 水电缴费
 */
class PayBillsActivity : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_pay_bills

    override fun isStatusDark() = true

    override fun initView() {
        showComing()
    }

    override fun initData() {

    }

    override fun bindListener() {
        prepaid_ll.setOnClickListener {
            showComing()
        }
        water_ll.setOnClickListener {
            showComing()
        }

        electric_ll.setOnClickListener {
            showComing()
        }

        internet_ll.setOnClickListener {
            showComing()
        }

    }


}