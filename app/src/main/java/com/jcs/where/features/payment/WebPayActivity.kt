package com.jcs.where.features.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_web_pay.*

/**
 * Created by Wangsw  2022/4/24 10:22.
 * 网页支付
 */
class WebPayActivity : BaseMvpActivity<WebParPresenter>(), WebPayView {



    private var useType = 0

    private var orderIds = java.util.ArrayList<Int>()

    override fun isStatusDark() = true

    companion object {

        fun navigation(context: Context, useType: Int, orderIds: ArrayList<Int>) {
            val bundle = Bundle().apply {
                putInt(Constant.PARAM_TYPE, useType)
                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
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

    }

    private fun initExtra() {
        intent.extras?.let {
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            useType = it.getInt(Constant.PARAM_TYPE)
        }
    }

    override fun initData() {

        presenter = WebParPresenter(this)
        showLoadingDialog()
        presenter.getPayUrl(useType, orderIds)
        web_view.webViewClient = object :WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return true
            }
        }

    }

    override fun bindListener() {

    }

    override fun bindUrl(redirectUrl: String) {
        dismissLoadingDialog()
        web_view.loadUrl(redirectUrl)
    }

    override fun onBackPressed() {


        super.onBackPressed()
    }

}