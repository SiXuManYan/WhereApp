package com.jcs.where.features.bills.hydropower.record

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2021/6/28 16:16.
 * 水电缴费记录
 */
class PaymentRecordActivity : BaseMvpActivity<PaymentRecordPresenter>(), PaymentRecordView {

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.payment_record))
    }

    override fun initData() {

    }

    override fun bindListener() {

    }
}