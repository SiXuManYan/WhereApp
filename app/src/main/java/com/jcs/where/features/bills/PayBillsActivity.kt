package com.jcs.where.features.bills

import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.bills.hydropower.edit.PaymentEditActivity
import com.jcs.where.features.bills.hydropower.record.PaymentRecordActivity
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
            startActivity(PaymentEditActivity::class.java)
        }

        electric_ll.setOnClickListener {
            showComing()
        }

        internet_ll.setOnClickListener {
            showComing()
        }
        mJcsTitle.setFirstRightIvClickListener {
            startActivity(PaymentRecordActivity::class.java)
        }

    }


}