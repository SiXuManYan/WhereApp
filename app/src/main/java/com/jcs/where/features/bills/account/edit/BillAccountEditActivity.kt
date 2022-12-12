package com.jcs.where.features.bills.account.edit

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.google.gson.JsonElement
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillAccount
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.account.BillAccountPresenter
import com.jcs.where.features.bills.account.BillAccountView
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_bills_account_edit.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/12/9 14:53.
 * 缴费历史账号 添加和编辑
 */
class BillAccountEditActivity : BaseMvpActivity<BillAccountPresenter>(), BillAccountView {

    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    private var billsType = 0
    private var cacheData: BillAccount? = null

    private var allEditView: ArrayList<AppCompatEditText> = ArrayList()

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_bills_account_edit

    override fun initView() {
        intent.extras?.let {
            billsType = it.getInt(Constant.PARAM_TYPE)
            cacheData = it.getParcelable<BillAccount>(Constant.PARAM_DATA)
        }


        val accountEt = findViewById<AppCompatEditText>(R.id.account_et)
        val accountNameEt = findViewById<AppCompatEditText>(R.id.account_name_et)
        allEditView.add(accountEt)
        allEditView.add(accountNameEt)

    }

    override fun initData() {
        presenter = BillAccountPresenter(this)
        if (cacheData != null) {
            delete_tv.visibility = View.VISIBLE
            account_et.setText(cacheData!!.first_field)
            account_name_et.setText(cacheData!!.second_field)
            select_tv.isChecked = cacheData!!.status == 1
            title_tv.setText(R.string.edit_account)
        } else {
            title_tv.setText(R.string.add_account)
        }

    }


    override fun handleSuccess(response: JsonElement) {
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_BILL_HISTORY_ACCOUNT))
        finish()
    }

    override fun bindListener() {
        allEditView.forEach {
            it.addTextChangedListener(
                afterTextChanged = {
                    val result = it.toString().trim()
                    if (result.isBlank()) {
                        BusinessUtils.setViewClickable(false, save_tv)
                    } else {
                        val isBlank = BusinessUtils.checkEditBlank(allEditView)
                        BusinessUtils.setViewClickable(!isBlank, save_tv)
                    }
                }
            )
        }

        save_tv.setOnClickListener {

            val first = account_et.text.toString().trim()
            val second = account_name_et.text.toString().trim()
            if (first.isBlank() || second.isBlank()) {
                return@setOnClickListener
            }

            if (cacheData != null) {
                presenter.editAccount(billsType,
                    first,
                    second,
                    select_tv.isChecked, cacheData!!.id)
            } else {
                presenter.addAccount(billsType,
                    first,
                    second,
                    select_tv.isChecked)
            }
        }

        select_tv.setOnClickListener {
            val checked = select_tv.isChecked
            select_tv.isChecked = !checked

        }

        delete_tv.setOnClickListener {
            AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.confirm_delete_account))
                .setPositiveButton(R.string.confirm) { _: DialogInterface?, _: Int -> presenter.deleteAccount(cacheData!!.id) }
                .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
                .create().show()
        }
    }


}