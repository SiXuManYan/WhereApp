package com.jcs.where.features.bills.place.phone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.bills.BillsPlaceOrder
import com.jcs.where.api.response.bills.CallChargeChannelItem
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_bills_place_order_charges.*

/**
 * Created by Wangsw  2022/7/7 15:14.
 * 话费充值下单
 */
class PhonePlaceOrderActivity : BaseMvpActivity<PhonePlacePresenter>(), PhonePlaceOrderView {


    var phone = ""

    private lateinit var selectItem: CallChargeChannelItem


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
        BarUtils.setStatusBarColor(this,Color.WHITE)
        initExtra()
    }


    private fun initExtra() {
        intent.extras?.let {
            phone = it.getString(Constant.PARAM_PHONE, "")
            phone_tv.text = phone

            val data = it.getParcelable<CallChargeChannelItem>(Constant.PARAM_DATA)

            data?.let { item ->
                selectItem = item
                total_money_tv.text = item.Denomination
                channel_tv.text = item.TelcoName
            }

        }
    }

    override fun initData() {
        presenter = PhonePlacePresenter(this)
    }

    override fun bindListener() {
        confirm_tv.setOnClickListener {
            showLoadingDialog(false)
            presenter.placeOrder(phone, selectItem)
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


interface PhonePlaceOrderView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)

}

class PhonePlacePresenter(private var view: PhonePlaceOrderView) : BaseMvpPresenter(view) {


    fun placeOrder(phone: String, selectItem: CallChargeChannelItem) {

        val apply = BillsPlaceOrder().apply {
            bill_type = 1
            cellphone_no = phone

            amount = selectItem.Denomination
            telco = selectItem.TelcoName
            ext_tag = selectItem.ExtTag

        }

        requestApi(mRetrofit.billsPlaceOrder(apply), object : BaseMvpObserver<HotelOrderCommitResponse>(view) {
            override fun onSuccess(response: HotelOrderCommitResponse) {
                view.commitSuccess(response)
            }

        })

    }

}


