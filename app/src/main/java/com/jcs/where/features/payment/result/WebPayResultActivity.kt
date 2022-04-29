package com.jcs.where.features.payment.result

import android.os.Bundle
import android.view.View
import com.jcs.where.R
import com.jcs.where.api.request.payment.PayStatus
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.main.MainActivity
import com.jcs.where.features.payment.WebPayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.activity_web_pay_result.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/4/24 17:11.
 * 支付结果页面
 */
class WebPayResultActivity : BaseMvpActivity<WebPayResultPresenter>(), WebPayResultView {


    private var orderIds = java.util.ArrayList<Int>()

    private var moduleType = ""

    private var useType = 0

    /** 上次未完成支付的url */
    private var lastPayUrl = ""

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_web_pay_result

    override fun initView() {
        initExtra()
        initDefaultUI()
    }

    private fun initDefaultUI() {
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)
        if (languageLocale.language == "zh") {
            pay_status_title_iv.setImageResource(R.mipmap.ic_pay_complete)
        } else {
            pay_status_title_iv.setImageResource(R.mipmap.ic_pay_complete_en)
        }
    }

    private fun initExtra() {
        intent.extras?.let {
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            moduleType = it.getString(Constant.PARAM_MODULE_TYPE, "")
            useType = it.getInt(Constant.PARAM_TYPE)
            lastPayUrl = it.getString(Constant.PARAM_LAST_PAY_URL, "")
        }
    }

    override fun initData() {
        presenter = WebPayResultPresenter(this)
        presenter.getPayStatus(moduleType, orderIds)
    }

    override fun onBackPressed() = Unit

    override fun bindListener() {
        finish_tv.setOnClickListener {
            startActivityClearTop(MainActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TAB, 2)
            })
            finish()
        }
        view_order_tv.setOnClickListener {
            finish_tv.performClick()
        }
        continue_pay_tv.setOnClickListener {
            WebPayActivity.navigation(this, useType, orderIds, lastPayUrl)
            finish()
        }

    }

    override fun bindPayStatus(response: PayStatus) {

        val payStatus = response.pay_status
        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)


        if (payStatus) {
            if (languageLocale.language == "zh") {
                pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success)
            } else {
                pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success_en)
            }
            continue_pay_tv.visibility = View.GONE
            // 支付成功
            EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
        }

    }


}