package com.jcs.where.features.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.jcs.where.R
import com.jcs.where.api.request.payment.PayUrlGet
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.payment.result.WebPayResultActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_web_pay.*

/**
 * Created by Wangsw  2022/4/24 10:22.
 * 网页支付
 */
class WebPayActivity : BaseMvpActivity<WebParPresenter>(), WebPayView {


    private var orderIds = java.util.ArrayList<Int>()


    private var moduleType = ""
    private var useType = 0

    /** 上次未完成支付的url */
    private var lastPayUrl = ""

    override fun isStatusDark() = true

    companion object {

        fun navigation(context: Context, useType: Int, orderIds: ArrayList<Int>, lastPayUrl: String? = null) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, useType)
                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                lastPayUrl?.let {
                    putString(Constant.PARAM_LAST_PAY_URL, lastPayUrl)
                }

            }

            val intent = if (User.isLogon()) {
                Intent(context, WebPayActivity::class.java).putExtras(bundle)
            } else {
                Intent(context, LoginActivity::class.java)
            }

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.activity_web_pay

    override fun initView() {
        initExtra()
        empty_view.apply {
            setEmptyImage(R.mipmap.ic_pay_info_error)
            setEmptyMessage(R.string.pay_error_title)
            setEmptyHint(R.string.pay_error_hint)
        }

        web_view.webViewClient = object : WebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                empty_view.visibility = View.VISIBLE
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                empty_view.visibility = View.VISIBLE
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                empty_view.visibility = View.VISIBLE
            }


        }
    }

    private fun initExtra() {
        intent.extras?.let {
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            useType = it.getInt(Constant.PARAM_TYPE)

            moduleType = when (useType) {
                Constant.PAY_INFO_BILLS -> PayUrlGet.BILL_PAY
                Constant.PAY_INFO_FOOD -> PayUrlGet.RESTAURANT
                Constant.PAY_INFO_TAKEAWAY -> PayUrlGet.TAKEAWAY
                Constant.PAY_INFO_HOTEL -> PayUrlGet.HOTEL
                Constant.PAY_INFO_MALL -> PayUrlGet.MALL
                else -> ""
            }

            lastPayUrl = it.getString(Constant.PARAM_LAST_PAY_URL, "")

        }
    }

    override fun initData() {

        presenter = WebParPresenter(this)


        if (lastPayUrl.isBlank()) {
            showLoadingDialog()
            presenter.getPayUrl(moduleType, orderIds)
        }else {
            web_view.loadUrl(lastPayUrl)
        }
    }

    override fun bindListener() {

    }

    override fun bindUrl(redirectUrl: String) {
        lastPayUrl = redirectUrl
        dismissLoadingDialog()
        web_view.loadUrl(redirectUrl)
    }

    override fun onBackPressed() {

        startActivity(WebPayResultActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_TYPE, useType)
            putString(Constant.PARAM_MODULE_TYPE, moduleType)
            putString(Constant.PARAM_LAST_PAY_URL,lastPayUrl)
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
        })
        super.onBackPressed()
    }

}