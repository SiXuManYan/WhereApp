package com.jcs.where.features.payment.counter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.jcs.where.R
import com.jcs.where.api.response.pay.PayCounterChannel
import com.jcs.where.api.response.pay.PayCounterChannelDetail
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_pay_counter.*

/**
 * Created by Wangsw  2023/3/7 16:07.
 * 收银台
 */
class PayCounterActivity : BaseMvpActivity<PayCounterPresenter>(), PayCounterView, OnItemChildClickListener {

    private var lastSelectIndex = -1
    private lateinit var mAdapter: PayCounterAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_pay_counter

    override fun initView() {

        initContent()
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
        presenter.getChannel()
    }

    override fun bindPayCounter(response: MutableList<PayCounterChannel>) {
        mAdapter.setNewInstance(response)
    }


    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        lastSelectIndex = position
        val payCounter = mAdapter.data[position]
        when (view.id) {
            R.id.item_container_rl -> {

            }
            R.id.view_balance_tv -> {
                presenter.getChannelDetail(payCounter.channel_code)

            }
            R.id.to_bind_tv -> {

            }
            else -> {}
        }
    }

    override fun bindChannelDetail(response: PayCounterChannelDetail) {

        BusinessUtils.showBalance(this,
            title = getString(R.string.check_balance),
            channelName = response.channel_code,
            balanceTitle = getString(R.string.balance),
            balance = response.balance.toPlainString(),
            onCancelClickListener = null,
            onConfirmClickListener = null
        )
    }


    override fun bindListener() {
        view_bind_channel_tv.setOnClickListener {
            // 查看已绑定支付渠道列表
        }
    }


}