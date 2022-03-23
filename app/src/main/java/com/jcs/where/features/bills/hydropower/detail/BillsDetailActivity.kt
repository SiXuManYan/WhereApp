package com.jcs.where.features.bills.hydropower.detail

import android.view.View
import com.jcs.where.R
import com.jcs.where.api.response.order.bill.BillOrderDetails
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.com100.ExtendChatActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_bills_detail.*


/**
 * Created by Wangsw  2021/7/20 10:49.
 * 水电订单详情
 */
class BillsDetailActivity : BaseMvpActivity<BillsDetailPresenter>(), BillsDetailView {

    private var orderId = 0

    override fun getLayoutId() = R.layout.activity_bills_detail

    override fun isStatusDark() = true

    override fun initView() {

        intent.extras?.let {
            orderId = it.getInt(Constant.PARAM_ORDER_ID, 0)
        }

    }

    override fun initData() {

        presenter = BillsDetailPresenter(this)
        presenter.getDetail(orderId)
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        service_ll.setOnClickListener {
            startActivityAfterLogin(ExtendChatActivity::class.java)
        }
    }

    override fun bindDetail(data: BillOrderDetails) {


        price_tv.text = getString(R.string.price_unit_format, data.price.toPlainString())
        order_number_tv.text = data.trade_no
        created_date_tv.text = data.created_at
        pay_way_tv.text = data.pay_type

        payment_name_tv.text = getString(R.string.payment_name_format, data.bank_card_account)
        payment_account_tv.text = getString(R.string.payment_account_format, data.bank_card_number)
        account_name_tv.text = data.account_name
        present_address_tv.text = data.present_address
        contact_no_tv.text = data.contact_no
        amount_tv.text = getString(R.string.price_unit_format, data.amount_due)

        val billsPayment = data.bills_payment
        if (!billsPayment.isNullOrBlank()) {
            bills_payment_tv.text = billsPayment
        }
        biller_tv.text = data.biller
        account_number_tv.text = data.account_number
        soa_invoice_no_tv.text = data.invoice_no
        date_tv.text = data.date
        due_date_tv.text = data.due_date
        input_date_tv.text = data.statement_date
        status_tv.text = presenter.getStatus(data.order_status)

        presenter.getStatusDescText(status_desc_tv, data.order_status)
        val electricityCompany = data.electricity_company
        if (electricityCompany.isEmpty()) {
            company_ll.visibility = View.GONE
        } else {
            company_ll.visibility = View.VISIBLE
            company_tv.text = electricityCompany
        }


    }

}