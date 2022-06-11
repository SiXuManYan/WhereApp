package com.jcs.where.features.bills.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_place_order.*

/**
 * Created by Wangsw  2022/6/9 14:53.
 * 统一下单
 */
class BillsPlaceOrderActivity : BaseMvpActivity<BillsPlaceOrderPresenter>(), BillsPlaceOrderView {


    /** 账单类型 */
    private var billsType = 0
    private var billerTag = ""
    private var firstField = ""
    private var secondField = ""
    private var userInputMoney: Double = 0.0
    private var serviceCharge: Double = 0.0
    private var fieldDetail = ArrayList<FieldDetail>()

    private lateinit var mAdapter: BillsPlaceOrderAdapter

    override fun isStatusDark() = true


    companion object {

        fun navigation(
            context: Context,
            billerTag: String,
            userInputMoney: Double,
            serviceMoney: Double,
            fieldDetail: ArrayList<FieldDetail>,
            billsType: Int,
        ) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, billsType)
                putString(Constant.PARAM_TAG, billerTag)
                putDouble(Constant.PARAM_USER_MONEY, userInputMoney)
                putDouble(Constant.PARAM_SERVICE_MONEY, serviceMoney)
                putParcelableArrayList(Constant.PARAM_DATA, fieldDetail)
            }
            val intent = Intent(context, BillsPlaceOrderActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_bills_place_order

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
        initList()
    }


    private fun initExtra() {
        intent.extras?.let {
            billsType = it.getInt(Constant.PARAM_TYPE, 0)
            billerTag = it.getString(Constant.PARAM_TAG, "")
            userInputMoney = it.getDouble(Constant.PARAM_USER_MONEY, 0.0)
            serviceCharge = it.getDouble(Constant.PARAM_SERVICE_MONEY, 0.0)
            val parcelableArrayList = it.getParcelableArrayList<FieldDetail>(Constant.PARAM_DATA)
            fieldDetail.addAll(parcelableArrayList!!)

        }
    }

    private fun initList() {


        val totalMoney = BigDecimalUtil.add(userInputMoney, serviceCharge)
        total_money_tv.text = totalMoney.toPlainString()

        mAdapter = BillsPlaceOrderAdapter().apply {
            setNewInstance(fieldDetail)
        }
        content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@BillsPlaceOrderActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(1f), SizeUtils.dp2px(15f), 0))
        }
    }


    override fun initData() {
        presenter = BillsPlaceOrderPresenter(this)
    }

    override fun bindListener() {
        confirm_tv.setOnClickListener {
            showLoadingDialog(false)
            mAdapter.data.forEachIndexed { index, fieldDetail ->
                when (index) {
                    0 -> {
                        firstField = fieldDetail.nativeUserInput
                    }
                    1 -> {
                        secondField = fieldDetail.nativeUserInput
                    }
                }
            }
            presenter.placeOrder(billerTag, firstField, secondField, userInputMoney, billsType)

        }
    }

    override fun commitSuccess(response: HotelOrderCommitResponse) {
        dismissLoadingDialog()
        val orderIds = ArrayList<Int>()
        val order = response.order
        orderIds.add(order!!.id)
        WebPayActivity.navigation(this, Constant.PAY_INFO_BILLS, orderIds)
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


    override fun onError(errorResponse: ErrorResponse) {
        dismissLoadingDialog()
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(errorResponse.getErrMsg())
            .setCancelable(false)
            .setPositiveButton(R.string.i_know) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

}