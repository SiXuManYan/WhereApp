package com.jiechengsheng.city.features.payment.counter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.request.payment.PayUrlGet
import com.jiechengsheng.city.api.response.pay.PayCounterChannel
import com.jiechengsheng.city.api.response.pay.PayCounterChannelDetail
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.main.MainActivity
import com.jiechengsheng.city.features.payment.result.PayResultActivity
import com.jiechengsheng.city.features.payment.token.TokenPaymentActivity
import com.jiechengsheng.city.features.payment.tokenized.TokenizedActivity
import com.jiechengsheng.city.features.payment.web.WebPaymentActivity
import com.jiechengsheng.city.features.web.WebViewActivity
import com.jiechengsheng.city.storage.entity.User
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_pay_counter.*

/**
 * Created by Wangsw  2023/3/7 16:07.
 * 收银台
 */
class PayCounterActivity : BaseMvpActivity<PayCounterPresenter>(), PayCounterView, OnItemChildClickListener {


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

    private var lastSelectIndex = -1

    private var needRefreshBindStatus = false

    private lateinit var mAdapter: PayCounterAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_pay_counter


    companion object {

        fun navigation(context: Context, moduleType: String, orderIds: ArrayList<Int>, amountToPaid: String) {
            val bundle = Bundle().apply {
                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                putString(Constant.PARAM_MODULE_TYPE, moduleType)
                putString(Constant.PARAM_AMOUNT, amountToPaid)
            }

            val intent = if (User.isLogon()) {
                Intent(context, PayCounterActivity::class.java).putExtras(bundle)
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
        initContent()
    }

    private fun initExtra() {

        intent.extras?.let {
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            moduleType = it.getString(Constant.PARAM_MODULE_TYPE, "")
            amountToPaid = it.getString(Constant.PARAM_AMOUNT, "0")
            amount_tv.text = amountToPaid
        }
    }


    private fun initContent() {

        mAdapter = PayCounterAdapter().apply {
            addChildClickViewIds(R.id.view_balance_tv, R.id.to_bind_tv, R.id.item_container_rl)
            setOnItemChildClickListener(this@PayCounterActivity)
        }

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = manager
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), 0, 0))
        }


    }

    override fun initData() {
        presenter = PayCounterPresenter(this)
        onRefresh()
    }

    private fun onRefresh() = presenter.getChannel()

    override fun bindPayCounter(response: MutableList<PayCounterChannel>) {
        mAdapter.setNewInstance(response)
    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        lastSelectIndex = position
        val payCounter = mAdapter.data[position]
        val channelCode = payCounter.channel_code
        val channelName = payCounter.title

        when (view.id) {
            R.id.view_balance_tv -> presenter.getChannelBalance(channelCode, channelName)
            R.id.to_bind_tv -> presenter.getBindTokenUrl(channelCode)
            R.id.item_container_rl -> {
                if (payCounter.is_tokenized_pay && payCounter.is_auth) {
                    // 令牌支付
                    TokenPaymentActivity.navigation(this,
                        moduleType,
                        orderIds,
                        amountToPaid,
                        channelName,
                        channelCode)

                } else {
                    // h5 支付
                    WebPaymentActivity.navigation(this, moduleType,
                        orderIds,
                        amountToPaid,
                        channelName,
                        channelCode)
                    finish()
                }

            }
            else -> {}
        }
    }


    override fun onResume() {
        super.onResume()
        if (needRefreshBindStatus) {
            onRefresh()
            needRefreshBindStatus = false
        }
    }

    override fun setBindTokenUrl(authH5Url: String) {
        // 绑定令牌支付
        if (authH5Url.isNotBlank()) {
            needRefreshBindStatus = true
            WebViewActivity.navigation(this, authH5Url)
        }
    }

    override fun bindChannelBalance(response: PayCounterChannelDetail, channelName: String) {

        BusinessUtils.showBalance(this,
            title = getString(R.string.check_balance),
            channelName = channelName,
            balanceTitle = getString(R.string.balance),
            balance = response.balance.toPlainString(),
            onCancelClickListener = null,
            onConfirmClickListener = null
        )
    }


    override fun bindListener() {
        mJcsTitle.setBackIvClickListener {
            AlertDialog.Builder(this,R.style.GrayDialogTheme)
                .setCancelable(false)
                .setTitle(R.string.hint)
                .setMessage(getString(R.string.give_up_payment_hint))
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
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
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        view_bind_channel_tv.setOnClickListener {
            // 查看已绑定支付渠道列表
            startActivityAfterLogin(TokenizedActivity::class.java)
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_UNBIND_PAY_TOKEN_SUCCESS,
            EventCode.EVENT_REFRESH_PAY_TOKEN_BIND_STATUS,
            -> {
                onRefresh()
            }
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
                finish()
            }
            else -> {}
        }


    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out)
    }


    override fun onBackPressed() = Unit


}