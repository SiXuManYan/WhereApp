package com.jcs.where.features.bills

import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.bills.hydropower.edit.PaymentEditActivity
import com.jcs.where.features.bills.hydropower.record.PaymentRecordActivity
import com.jcs.where.utils.Constant
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
            startActivityAfterLogin(PaymentEditActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 1)
            })
        }

        electric_ll.setOnClickListener {
            showComing()
        }

        internet_ll.setOnClickListener {
            showComing()
        }
        mJcsTitle.setFirstRightIvClickListener {
            startActivityAfterLogin(PaymentRecordActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 2)
            })
        }

    }


}