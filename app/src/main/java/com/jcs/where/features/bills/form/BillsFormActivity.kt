package com.jcs.where.features.bills.form

import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity

/**
 * Created by Wangsw  2022/6/8 17:33.
 * 水电表单
 */
class BillsFormActivity : BaseMvpActivity<BillsFormPresenter>(), BillsFormView {


    private lateinit var mAdapter: BillsFormAdapter

    override fun getLayoutId() = R.layout.activity_bills_form

    override fun initView() {

    }

    override fun initData() {
        presenter = BillsFormPresenter(this)

    }

    override fun bindListener() {

    }
}