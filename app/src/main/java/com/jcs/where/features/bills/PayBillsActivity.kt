package com.jcs.where.features.bills

import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.bills.hydropower.edit.PaymentEditActivity
import com.jcs.where.features.bills.hydropower.record.PaymentRecordActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.activity_pay_bills.*

/**
 * Created by Wangsw  2021/4/15 14:12.
 * 水电缴费
 */
class PayBillsActivity : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_pay_bills

    override fun isStatusDark() = false

    override fun initView() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)
        if (languageLocale.language == "zh") {
            hydropower_banner_iv.setImageResource(R.mipmap.ic_hydropower_chn)
        }else{
            hydropower_banner_iv.setImageResource(R.mipmap.ic_hydropower_en)
        }

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
            startActivityAfterLogin(PaymentEditActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 2)
            })
        }

        internet_ll.setOnClickListener {
            showComing()
        }
        record_iv.setOnClickListener {
            startActivityAfterLogin(PaymentRecordActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 2)
            })
        }

    }


}