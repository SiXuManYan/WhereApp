package com.jcs.where.features.bills.record.result

import com.jcs.where.R
import com.jcs.where.api.response.bills.BillStatus
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.activity_bills_result.*

/**
 * Created by Wangsw  2022/6/11 15:00.
 * 缴费状态
 */
class BillsRecommitResultActivity : BaseMvpActivity<BillsRecommitResultPresenter>(), BillsRecommitResultView {

    var orderId = 0

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_bills_result

    override fun initView() {
        initDefaultUI()
        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }
    }

    private fun initDefaultUI() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)
        if (languageLocale.language == "zh") {
            pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_fail)
        } else {
            pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_fail_en)
        }

    }

    override fun initData() {
        presenter = BillsRecommitResultPresenter(this)
        presenter.getStatus(orderId)
    }

    override fun bindListener() {
        finish_tv.setOnClickListener {
            finish()
        }

        view_order_tv.setOnClickListener {
            finish()
        }
    }

    override fun bindStatus(response: BillStatus) {
        val status = response.transaction_status

        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)


        if (status) {
            if (languageLocale.language == "zh") {
                pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success_bill)
            } else {
                pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success_bill_en)
            }
            pay_info_iv.setImageResource(R.mipmap.ic_pay_success_bill2)
        } else {
            if (languageLocale.language == "zh") {
                pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_fail)
            } else {
                pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_fail_en)
            }
            pay_info_iv.setImageResource(R.mipmap.ic_pay_fail_bill)
        }

    }

}