package com.jcs.where.features.bills.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.bills.preview.BillsPlaceOrderActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_form.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/6/8 17:33.
 * 水电表单
 */
class BillsFormActivity : BaseMvpActivity<BillsFormPresenter>(), BillsFormView {


    /** 渠道名称 */
    private var billerTag = ""

    /** 账单类型 */
    private var billsType = 0

    /** 渠道描述 */
    private var description = ""

    /** 渠道服务费（加上充值费用为支付费用） */
    private var serviceCharge = BigDecimal.ZERO

    private var userInputMoney = BigDecimal.ZERO

    private var fieldDetail = ArrayList<FieldDetail>()

    private lateinit var mAdapter: BillsFormAdapter

    override fun isStatusDark() = true

    companion object {

        fun navigation(
            context: Context,
            billerTag: String,
            description: String,
            serviceCharge: Double,
            fieldDetail: ArrayList<FieldDetail>,
            billsType: Int,
        ) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, billsType)
                putString(Constant.PARAM_TAG, billerTag)
                putString(Constant.PARAM_DESCRIPTION, description)
                putDouble(Constant.PARAM_SERVICE_CHARGE, serviceCharge)
                putParcelableArrayList(Constant.PARAM_DATA, fieldDetail)
            }
            val intent = Intent(context, BillsFormActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_bills_form

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
        initList()
    }


    private fun initExtra() {
        intent.extras?.let {
            billsType = it.getInt(Constant.PARAM_TYPE, 0)
            billerTag = it.getString(Constant.PARAM_TAG, "")
            description = it.getString(Constant.PARAM_DESCRIPTION, "")


            val parcelableArrayList = it.getParcelableArrayList<FieldDetail>(Constant.PARAM_DATA)
            fieldDetail.addAll(parcelableArrayList!!)
            serviceCharge = BigDecimal(it.getDouble(Constant.PARAM_SERVICE_CHARGE, 0.0))
        }
        rule_tv.text = getString(R.string.service_charge_format, serviceCharge)
    }


    private fun initList() {

        channel_tv.text = billerTag
        desc_tv.text = description

        mAdapter = BillsFormAdapter().apply {
            setNewInstance(fieldDetail)
        }
        channel_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@BillsFormActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f),
                SizeUtils.dp2px(15f),
                0))
        }

    }

    override fun initData() {
        presenter = BillsFormPresenter(this)

    }

    override fun bindListener() {
        amount_et.addTextChangedListener(
            afterTextChanged = {
                userInputMoney = BusinessUtils.getSafeBigDecimal(it.toString())
            }
        )


        mJcsTitle.setBackIvClickListener {
            onBackClick()
        }

        next_tv.setOnClickListener {
            val finalMoney = BigDecimalUtil.add(userInputMoney, serviceCharge)
            BillsPlaceOrderActivity.navigation(this, billerTag, finalMoney.toDouble(), fieldDetail, billsType)
        }

    }

    override fun onBackPressed() {
        onBackClick()
    }

    private fun onBackClick() {
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(R.string.give_up_bills_payment_hint)
            .setCancelable(false)
            .setPositiveButton(R.string.confirm) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialogInterface, i ->
                dialogInterface.dismiss()
                finish()
            }
            .create().show()
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                finish()
            }
        }

    }

}