package com.jiechengsheng.city.features.payment.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.ErrorResponse
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.main.MainActivity
import com.jiechengsheng.city.features.payment.counter.PayCounterPresenter
import com.jiechengsheng.city.features.payment.counter.PayCounterView
import com.jiechengsheng.city.features.payment.result.PayResultActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_web_pay.*

/**
 * Created by Wangsw  2023/3/15 9:51.
 * 网页支付
 */
class WebPaymentActivity : BaseMvpActivity<PayCounterPresenter>(), PayCounterView {


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


    private var webError = false


    override fun getLayoutId() = R.layout.activity_web_pay


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
                Intent(context, WebPaymentActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun isStatusDark() = true

    override fun initView() {
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent)
        initExtra()

        empty_view.apply {
            setEmptyImage(R.mipmap.ic_pay_info_error)
            setEmptyMessage(R.string.pay_error_title)
            setEmptyHint(R.string.pay_error_hint)
        }

        initWeb()
    }

    private fun initWeb() {
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                empty_view.visibility = View.GONE
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                webError = true

            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                webError = true
            }


        }
        web_view.webChromeClient = object : WebChromeClient() {


            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (!title.isNullOrBlank()) {
                    mJcsTitle.setMiddleTitle(title)
                } else {
                    mJcsTitle.setMiddleTitle(getString(R.string.pay_order_title))
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {

                    if (webError) {
                        empty_view.apply {
                            setEmptyImage(R.mipmap.ic_empty_system_upgrade)
                            empty_message_tv.visibility = View.GONE
                            setEmptyHint(R.string.pay_error_hint_system_update)

                        }
                        empty_view.visibility = View.VISIBLE
                        empty_view.showEmptyContainer()
                    } else {
                        empty_view.visibility = View.GONE
                    }
                }
            }

        }
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
        }
    }

    override fun initData() {
        presenter = PayCounterPresenter(this)
        presenter.doWherePay(moduleType, orderIds, channelCode, PayUrlGet.ONE_TIME_PAYMENT)
    }

    override fun bindListener() {
        mJcsTitle.setBackIvClickListener {
            if (moduleType == PayUrlGet.BILL_PAY) {
                // 支付账单，跳转至支付结果
                PayResultActivity.navigation(this, moduleType, orderIds, amountToPaid)
            } else {
                // 其他板块，跳转至订单列表
                startActivityClearTop(MainActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_TAB, 2)
                })
            }
            finish()
        }
    }

    override fun onBackPressed() = Unit


    override fun payFinish(redirectUrl: String) {
        web_view.loadUrl(redirectUrl)
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }


    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        empty_view.visibility = View.VISIBLE
        empty_view.showEmptyContainer()
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