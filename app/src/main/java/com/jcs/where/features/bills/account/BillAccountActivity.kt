package com.jcs.where.features.bills.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillAccount
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.account.edit.BillAccountEditActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_account.*
import java.util.ArrayList

/**
 * Created by Wangsw  2022/12/9 13:46.
 * 缴费账号列表
 */
class BillAccountActivity : BaseMvpActivity<BillAccountPresenter>(), BillAccountView, OnItemClickListener, OnItemChildClickListener {

    /** 账单类型（1-话费，2-水费，3-电费，4-网费） */
    private var billsType = 0

    private lateinit var mAdapter: BillAccountAdapter

    override fun getLayoutId() = R.layout.activity_bills_account

    override fun initView() {
        billsType = intent.getIntExtra(Constant.PARAM_TYPE , 0)
        initContent()
    }

    private fun initContent() {

        mAdapter = BillAccountAdapter().apply {
            setOnItemClickListener(this@BillAccountActivity)
            addChildClickViewIds(R.id.edit_tv)
            setOnItemChildClickListener(this@BillAccountActivity)
        }
        content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@BillAccountActivity, LinearLayoutManager.VERTICAL, false)

            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

    }

    override fun initData() {
        presenter = BillAccountPresenter(this)
        presenter.getBillAccount(billsType)
    }

    override fun bindListener() {
        add_tv.setOnClickListener {

        }
    }

    override fun bindAccount(response: ArrayList<BillAccount>) {
        mAdapter.setNewInstance(response)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val billAccount = mAdapter.data[position]

        setResult(Activity.RESULT_OK, Intent()
            .putExtra(Constant.PARAM_FIRST, billAccount.first_field)
            .putExtra(Constant.PARAM_SECOND, billAccount.second_field)
        )
        finish()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val billAccount = mAdapter.data[position]
        startActivity(BillAccountEditActivity::class.java , Bundle().apply {
            putInt(Constant.PARAM_TYPE , billsType)
            putParcelable(Constant.PARAM_DATA , billAccount)
        })
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_BILL_HISTORY_ACCOUNT-> {
                presenter.getBillAccount(billsType)
            }

        }

    }


}