package com.jcs.where.features.bills.hydropower.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.TextView
import com.jcs.where.R
import com.jcs.where.api.request.bills.BillsOrderCommit
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.hydropower.pay.BillsPayActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_payment_edit.*
import java.util.*

/**
 * Created by Wangsw  2021/6/29 13:48.
 *  水电表单
 */
class PaymentEditActivity : BaseMvpActivity<PaymentEditPresenter>(), PaymentEditView {

    /**
     * 	账单类型（1-水费，2-电费）
     */
    private var billType = 0


    override fun getLayoutId() = R.layout.activity_payment_edit

    override fun isStatusDark() = true

    override fun initView() {

        intent.extras?.let {
            billType = it.getInt(Constant.PARAM_TYPE)
        }
    }

    override fun initData() {
        presenter = PaymentEditPresenter(this)
    }

    override fun bindListener() {

        date_tv.setOnClickListener {
            selectDate(date_tv)
        }
        due_date_tv.setOnClickListener {
            selectDate(due_date_tv)
        }
        input_date_tv.setOnClickListener {
            selectDate(input_date_tv)
        }

        next_tv.setOnClickListener {

            val accountName = account_name_et.text.toString().trim()
            val presentAddress = present_address_et.text.toString().trim()
            val contactNo = contact_no_et.text.toString().trim()
            val amount = amount_et.text.toString().trim()
            val billerName = biller_et.text.toString().trim()
            val accountNumber = account_number_et.text.toString().trim()
            val soainvoiceNo = soa_invoice_no_et.text.toString().trim()

            val dateStr = date_tv.text.toString().trim()
            val dueDate = due_date_tv.text.toString().trim()
            val inputDate = input_date_tv.text.toString().trim()

            val apply = BillsOrderCommit().apply {
                bill_type = billType
                account_name = accountName
                account_number = accountNumber
                present_address = presentAddress
                contact_no = contactNo
                biller = billerName
                amount_due = amount
                invoice_no = soainvoiceNo
                date = dateStr
                due_date = dueDate
                statement_date = inputDate
            }

            startActivity(BillsPayActivity::class.java, Bundle().apply {
                putSerializable(Constant.PARAM_DATA, apply)
            })

        }


    }

    private fun selectDate(textView: TextView) {
        val ca = Calendar.getInstance()
        val mYear = ca[Calendar.YEAR]
        val mMonth = ca[Calendar.MONTH]
        val mDay = ca[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            textView.text = getString(R.string.birthday_format, year, (month + 1), dayOfMonth)

        }, mYear, mMonth, mDay)
        datePickerDialog.show()
    }


/*    override fun onEventReceived(baseEvent: BaseEvent<*>) {

        if (baseEvent.code == EventCode.EVENT_BILLS_PAY_SUCCESS) {
            finish()
        }
    }*/

}