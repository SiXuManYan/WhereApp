package com.jiechengsheng.city.features.payment.token

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.pay.PayCounterChannelDetail
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.payment.counter.PayCounterPresenter
import com.jiechengsheng.city.features.payment.counter.PayCounterView
import com.jiechengsheng.city.features.payment.result.PayResultActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_token_payment.*

/**
 * Created by Wangsw  2023/3/14 14:07.
 * 令牌支付
 */
class TokenPaymentActivity : BaseMvpActivity<PayCounterPresenter>(), PayCounterView {


    /** 订单 Id */
    private var orderIds = java.util.ArrayList<Int>()

    /**
     *  支付场景
     *  @see PayUrlGet.HOTEL
     *  @see PayUrlGet.RESTAURANT
     *  @see PayUrlGet.TAKEAWAY
     *  @see PayUrlGet.BILL_PAY
     *  @see PayUrlGet.MALL
     */
    private var moduleType = ""

    /** 客户端计算好的待支付金额 */
    private var amountToPaid = ""

    /** 渠道编码 */
    private var channelCode = ""

    /** 渠道名称 */
    private var channelName = ""

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_token_payment


    companion object {

        fun navigation(
            context: Context,
            moduleType: String,
            orderIds: ArrayList<Int>,
            amountToPaid: String,
            channelName: String,
            channelCode: String,
        ) {
            val bundle = Bundle().apply {
                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                putString(Constant.PARAM_AMOUNT, amountToPaid)
                putString(Constant.PARAM_MODULE_TYPE, moduleType)
                putString(Constant.PARAM_CHANNEL_NAME, channelName)
                putString(Constant.PARAM_CHANNEL_CODE, channelCode)
            }

            val intent = if (User.isLogon()) {
                Intent(context, TokenPaymentActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)
        initExtra()
    }


    private fun initExtra() {
        intent.extras?.let {
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            moduleType = it.getString(Constant.PARAM_MODULE_TYPE, "")
            amountToPaid = it.getString(Constant.PARAM_AMOUNT, "0")
            channelName = it.getString(Constant.PARAM_CHANNEL_NAME, "")
            channelCode = it.getString(Constant.PARAM_CHANNEL_CODE, "")
            amount_tv.text = amountToPaid
            channel_name_tv.text = channelName

        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }


    override fun initData() {
        presenter = PayCounterPresenter(this)
        presenter.getChannelBalance(channelCode,channelName)
    }


    override fun bindChannelBalance(response: PayCounterChannelDetail,channelName:String) {
        pay_bt.text = response.balance.stripTrailingZeros().toPlainString()
    }


    override fun bindListener() {
        pay_bt.setOnClickListener {
            presenter.doWherePay(moduleType, orderIds, channelCode, PayUrlGet.TOKENIZED_PAYMENT)
        }
    }

    override fun payFinish(redirectUrl: String) {
        PayResultActivity.navigation(this, moduleType, orderIds, amountToPaid)
        finish()
    }



//    override fun onEventReceived(baseEvent: BaseEvent<*>) {
//        super.onEventReceived(baseEvent)
//        when (baseEvent.code) {
//            EventCode.EVENT_REFRESH_ORDER_LIST -> {
//                finish()
//            }
//            else -> {}
//        }
//    }

}