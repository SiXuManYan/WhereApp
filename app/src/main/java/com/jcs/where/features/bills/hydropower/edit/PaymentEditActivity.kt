package com.jcs.where.features.bills.hydropower.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jcs.where.R
import com.jcs.where.api.request.bills.BillsOrderCommit
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.hydropower.pay.BillsPayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.product.ProductUtils
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
        if (billType == 2) {
            company_ll.visibility = View.VISIBLE
        }else{
            company_ll.visibility = View.GONE
        }
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
        company_ll.setOnClickListener {
            showCompanyDialog()
        }

        val watcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {


                if (!ProductUtils.checkEditEmpty(
                        account_name_et,
                        present_address_et,
                        contact_no_et,
                        amount_et,
                        biller_et,
                        account_number_et,
                        soa_invoice_no_et
                    )
                ) {
                    next_tv.isEnabled = false
                    next_tv.alpha = 0.7f
                } else {
                    next_tv.isEnabled = true
                    next_tv.alpha = 1f
                }


            }

        }

        account_name_et.addTextChangedListener(watcher)
        present_address_et.addTextChangedListener(watcher)
        contact_no_et.addTextChangedListener(watcher)
        amount_et.addTextChangedListener(watcher)
        biller_et.addTextChangedListener(watcher)
        account_number_et.addTextChangedListener(watcher)
        soa_invoice_no_et.addTextChangedListener(watcher)

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
            val company = company_tv.text.toString().trim()

            if (dateStr.isEmpty()) {
                ToastUtils.showShort(R.string.select_date)
                return@setOnClickListener
            }
            if (dueDate.isEmpty()) {
                ToastUtils.showShort(R.string.select_date)
                return@setOnClickListener
            }
            if (inputDate.isEmpty()) {
                ToastUtils.showShort(R.string.select_date)
                return@setOnClickListener
            }
            if (billType == 2 && company.isBlank()) {
                ToastUtils.showShort(R.string.select_power_company)
                return@setOnClickListener
            }


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
                electricity_company = company
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

    private fun showCompanyDialog() {
        val addressDialog = BottomSheetDialog(this, R.style.bottom_sheet_edit)
        val view = LayoutInflater.from(this).inflate(R.layout.electricity_company, null)
        addressDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
        }

        view.findViewById<TextView>(R.id.cancel_tv).setOnClickListener {
            addressDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.item_1_tv).setOnClickListener {
            company_tv.text = getString(R.string.penelco)
            addressDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.item_2_tv).setOnClickListener {
            company_tv.text = getString(R.string.afab)
            addressDialog.dismiss()
        }
        addressDialog.show()
    }


}