package com.jcs.where.features.store.pay

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.pay.info.PayInfoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_pay.*

/**
 * Created by Wangsw  2021/6/23 15:10.
 * 商城支付
 */
class StorePayActivity : BaseMvpActivity<StorePayPresenter>(), StorePayView, OnItemClickListener {

    private var totalPrice: Double = 0.0
    private var orderIds = java.util.ArrayList<Int>()
    private var selectedChannel: PayChannel? = null

    private lateinit var mAdapter: StorePayAdapter


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_pay

    override fun initView() {
        intent.extras?.let {
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE, 0.0)
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
        }

        initPayChannel()
        total_tv.text = getString(R.string.price_unit_format, totalPrice.toString())
    }

    private fun initPayChannel() {
        mAdapter = StorePayAdapter()
        mAdapter.setOnItemClickListener(this@StorePayActivity)

        pay_channel_rv.apply {
            adapter = mAdapter
            isNestedScrollingEnabled = true
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerDecoration(getColor(R.color.colorPrimary), SizeUtils.dp2px(20f),
                    0, 0).apply { setDrawHeaderFooter(false) })
        }
    }

    override fun initData() {
        presenter = StorePayPresenter(this)
        presenter.getPayChannel()

    }


    override fun bindData(response: ArrayList<PayChannel>) {
        mAdapter.setNewInstance(response)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        mAdapter.data.forEachIndexed { index, payChannel ->
            if (index == position) {
                payChannel.nativeSelected = true
                selectedChannel = payChannel
            } else {
                payChannel.nativeSelected = false
            }
        }
        mAdapter.notifyDataSetChanged()
    }


    override fun bindListener() {
        pay_tv.setOnClickListener {
            if (selectedChannel == null) {
                ToastUtils.showShort(getString(R.string.select_pay_way))
                return@setOnClickListener
            }

            startActivityAfterLogin(PayInfoActivity::class.java, Bundle().apply {
                putDouble(Constant.PARAM_TOTAL_PRICE, totalPrice)
                putSerializable(Constant.PARAM_DATA, selectedChannel)
                putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
                putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_ESTORE)
            })
        }
    }

}