package com.jiechengsheng.city.features.bills.place

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
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.response.bills.BillsOrderDiscount
import com.jiechengsheng.city.api.response.bills.FieldDetail
import com.jiechengsheng.city.api.response.hotel.HotelOrderCommitResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.bills.place.coupon.BillCouponHome
import com.jiechengsheng.city.features.payment.WebPayActivity
import com.jiechengsheng.city.utils.BigDecimalUtil
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_bills_place_order.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/6/9 14:53.
 * 统一下单
 */
class BillsPlaceOrderActivity : BaseMvpActivity<BillsPlaceOrderPresenter>(), BillsPlaceOrderView {


    /** 账单类型   1-话费，2-水费，3-电费，4-网费 */
    private var billsType = 0
    private var billerTag = ""

    /** 账号 */
    private var firstField = ""

    /** 用户名 */
    private var secondField = ""

    /** 用户输入的原始金额 */
    private var userInputMoney: Double = 0.0

    /** 经过优惠计算后，的原始金额 */
    private var userInputMoneyAfterDiscount = ""

    /** 服务费 */
    private var serviceCharge: Double = 0.0

    private var fieldDetail = ArrayList<FieldDetail>()

    private lateinit var mAdapter: BillsPlaceOrderAdapter

    /** 优惠券id */
    private var currentCouponId = 0

    private lateinit var mSelectedCouponDialog: BillCouponHome

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
        mSelectedCouponDialog = BillCouponHome()
        presenter.billsOrderDiscount(billsType, userInputMoney.toString(), fieldDetail[0].nativeUserInput, currentCouponId)
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

        // 用户提示
        val hint = response.hint
        if (hint.isNullOrBlank()) {
            user_hint_tv.visibility = View.GONE
        } else {
            user_hint_tv.visibility = View.VISIBLE
            user_hint_tv.text = hint
        }

        // 优惠券
        val couponId = response.coupon_id
        if (couponId != 0) {
            currentCouponId = couponId
            coupon_rl.visibility = View.VISIBLE
            coupon_price_tv.text = getString(R.string.price_unit_discount_format, response.coupon_price)
        } else {
            coupon_rl.visibility = View.GONE
        }

    }

    override fun bindListener() {
        confirm_tv.setOnClickListener {
            showLoadingDialog(false)
            mAdapter.data.forEachIndexed { index, fieldDetail ->
                when (index) {
                    0 -> firstField = fieldDetail.nativeUserInput
                    1 -> secondField = fieldDetail.nativeUserInput
                }
            }
            presenter.placeOrder(billerTag, firstField, secondField, userInputMoney.toString(), billsType, currentCouponId)
        }

        coupon_rl.setOnClickListener {

            mSelectedCouponDialog.apply {
                alreadySelectedCouponId = currentCouponId
                module = billsType
                price = userInputMoney.toString()
                account = firstField
            }
            mSelectedCouponDialog.show(supportFragmentManager, mSelectedCouponDialog.tag)
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
            EventCode.EVENT_REFRESH_ORDER_LIST -> finish()
            EventCode.EVENT_SELECTED_PLATFORM_COUPON -> {
                val selectedPlatformCouponId = baseEvent.data as Int
                currentCouponId = selectedPlatformCouponId
                presenter.billsOrderDiscount(billsType, userInputMoney.toString(), fieldDetail[0].nativeUserInput, currentCouponId)
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