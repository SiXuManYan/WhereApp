package com.jiechengsheng.city.features.bills.place.phone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.bills.BillsOrderDiscount
import com.jiechengsheng.city.api.response.bills.BillsPlaceOrder
import com.jiechengsheng.city.api.response.bills.CallChargeChannelItem
import com.jiechengsheng.city.api.response.hotel.HotelOrderCommitResponse
import com.jiechengsheng.city.api.response.mall.request.BillCouponRequest
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.bills.place.coupon.BillCouponHome
import com.jiechengsheng.city.features.payment.WebPayActivity
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_bills_place_order_charges.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/7/7 15:14.
 * 话费充值下单
 */
class PhonePlaceOrderActivity : BaseMvpActivity<PhonePlacePresenter>(), PhonePlaceOrderView {


    var phone = ""

    private lateinit var selectItem: CallChargeChannelItem

    private var finalPrice = ""


    /** 优惠券id */
    private var currentCouponId = 0


    private lateinit var mSelectedCouponDialog: BillCouponHome


    companion object {

        fun navigation(context: Context, phone: String, selectItem: CallChargeChannelItem) {
            val bundle = Bundle().apply {
                putString(Constant.PARAM_PHONE, phone)
                putParcelable(Constant.PARAM_DATA, selectItem)
            }
            val intent = Intent(context, PhonePlaceOrderActivity::class.java).putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_bills_place_order_charges

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initExtra()
    }


    private fun initExtra() {
        intent.extras?.let {
            phone = it.getString(Constant.PARAM_PHONE, "")
            phone_tv.text = phone
            CacheUtil.getShareDefault().put(Constant.SP_CHARGES_PHONE ,phone )

            val data = it.getParcelable<CallChargeChannelItem>(Constant.PARAM_DATA)

            data?.let { item ->
                selectItem = item
                // total_money_tv.text = item.Denomination
                channel_tv.text = item.TelcoName
            }

        }
    }

    override fun initData() {
        presenter = PhonePlacePresenter(this)
        mSelectedCouponDialog = BillCouponHome()
        presenter.getBillsOrderDiscount(selectItem.Denomination, phone, currentCouponId)
    }

    override fun bindListener() {
        confirm_tv.setOnClickListener {
            showLoadingDialog(false)
            presenter.placeOrder(phone, selectItem, finalPrice, currentCouponId)
        }

        coupon_rl.setOnClickListener {

            mSelectedCouponDialog.apply {
                alreadySelectedCouponId = currentCouponId
                module = BillCouponRequest.MODULE_PHONE
                price = selectItem.Denomination
                account = phone
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
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                finish()
            }

            EventCode.EVENT_SELECTED_PLATFORM_COUPON -> {
                val selectedPlatformCouponId = baseEvent.data as Int
                currentCouponId = selectedPlatformCouponId
                presenter.getBillsOrderDiscount(selectItem.Denomination, phone, currentCouponId)
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


    override fun bindOrderDiscount(response: BillsOrderDiscount) {
        val price = response.price
        finalPrice = price
        total_money_tv.text = price

        if (BigDecimal(response.discounts_price).compareTo(BigDecimal.ZERO) == 1) {
            discount_rl.visibility = View.VISIBLE
            discount_tv.text = response.discount
            discount_price_tv.text = StringUtils.getString(R.string.price_unit_discount_format, response.discounts_price)
        } else {
            discount_rl.visibility = View.GONE
        }

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

}


interface PhonePlaceOrderView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)

    /** 设置折扣信息以及最终金额 */
    fun bindOrderDiscount(response: BillsOrderDiscount)

}

class PhonePlacePresenter(private var view: PhonePlaceOrderView) : BaseMvpPresenter(view) {


    /**
     * 获取折扣以及最终支付价格
     * @param oldPrice   原价
     * @param payAccount 充值手机号
     */
    fun getBillsOrderDiscount(oldPrice: String, payAccount: String, couponId: Int? = null) {

        requestApi(mRetrofit.billsOrderDiscount(1, oldPrice, payAccount, couponId),
            object : BaseMvpObserver<BillsOrderDiscount>(view) {
                override fun onSuccess(response: BillsOrderDiscount) {

                    view.bindOrderDiscount(response)
                }

            })

    }


    fun placeOrder(phone: String, selectItem: CallChargeChannelItem, finalPrice: String, couponId: Int) {

        if (finalPrice.isBlank()) {
            return
        }

        val apply = BillsPlaceOrder().apply {
            bill_type = 1
            cellphone_no = phone

            amount = selectItem.Denomination
//            amount = finalPrice
            telco = selectItem.TelcoName
            ext_tag = selectItem.ExtTag
            coupon_id = couponId
        }

        requestApi(mRetrofit.billsPlaceOrder(apply), object : BaseMvpObserver<HotelOrderCommitResponse>(view) {
            override fun onSuccess(response: HotelOrderCommitResponse) {
                view.commitSuccess(response)
            }

        })

    }

}


