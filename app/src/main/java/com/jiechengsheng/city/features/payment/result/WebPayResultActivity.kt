package com.jiechengsheng.city.features.payment.result

import android.os.Bundle
import android.view.View
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.payment.PayStatus
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.bills.record.BillsRecordActivity
import com.jiechengsheng.city.features.main.MainActivity
import com.jiechengsheng.city.features.payment.WebPayActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.utils.LocalLanguageUtil
import kotlinx.android.synthetic.main.activity_web_pay_result.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/4/24 17:11.
 * 支付结果页面
 */
@Deprecated(message = "PayResultActivity")
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

        when (moduleType) {
            PayUrlGet.BILL_PAY -> {

                if (languageLocale.language == "zh") {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill)
                } else {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_en)
                }
                payment_hint.visibility = View.GONE
            }
            else -> {

                if (languageLocale.language == "zh") {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_pay_complete)
                } else {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_pay_complete_en)
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
            when (moduleType) {
                PayUrlGet.BILL_PAY -> {
                    // 跳转至水电列表
                    EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))
                    startActivity(BillsRecordActivity::class.java)
                }
                else -> {
                    startActivityClearTop(MainActivity::class.java, Bundle().apply {
                        putInt(Constant.PARAM_TAB, 2)
                    })
                }
            }
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

        if (!payStatus) {
            return
        }

        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(this)

        when (moduleType) {
            PayUrlGet.BILL_PAY -> {
                // 缴费中
                if (languageLocale.language == "zh") {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill)
                } else {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_paying_bill_en)
                }
                pay_info_iv.setImageResource(R.mipmap.ic_paying_bills)
            }
            else -> {
                // 支付成功
                if (languageLocale.language == "zh") {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success)
                } else {
                    pay_status_title_iv.setImageResource(R.mipmap.ic_pay_success_en)
                }
                pay_info_iv.setImageResource(R.mipmap.ic_pay_success_common)
            }

        }



        continue_pay_tv.visibility = View.GONE
        // 支付成功
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_REFRESH_ORDER_LIST))

    }


}