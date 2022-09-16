package com.jcs.where.features.bills.place

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.response.bills.BillsOrderDiscount
import com.jcs.where.api.response.bills.FieldDetail
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_place_order.*
import java.math.BigDecimal

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

    /** 用户输入的原始金额 */
    private var userInputMoney: Double = 0.0

    /** 经过优惠计算后，的原始金额 */
    private var userInputMoneyAfterDiscount = ""

    /** 服务费 */
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


//        val totalMoney = BigDecimalUtil.addUnNecessary(userInputMoney, serviceCharge)
//        total_money_tv.text = totalMoney.toPlainString()

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
        presenter.billsOrderDiscount(billsType, userInputMoney.toString(), fieldDetail[0].nativeUserInput)
    }

    override fun bindOrderDiscount(response: BillsOrderDiscount) {
        val price = response.price
        userInputMoneyAfterDiscount = price

        val totalMoney =
            BigDecimalUtil.addUnNecessary(BusinessUtils.getSafeBigDecimal(userInputMoneyAfterDiscount), BigDecimal(serviceCharge))
        total_money_tv.text = totalMoney.toPlainString()

        if (BigDecimal(response.discounts_price).compareTo(BigDecimal.ZERO) == 1) {
            discount_rl.visibility = View.VISIBLE
            discount_tv.text = response.discount
            discount_price_tv.text = StringUtils.getString(R.string.price_unit_discount_format, response.discounts_price)
        } else {
            discount_rl.visibility = View.GONE
        }

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
            presenter.placeOrder(billerTag, firstField, secondField, userInputMoneyAfterDiscount, billsType)

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


    override fun onError(errorResponse: ErrorResponse?) {
        dismissLoadingDialog()
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(errorResponse?.getErrMsg())
            .setCancelable(false)
            .setPositiveButton(R.string.i_know) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

}