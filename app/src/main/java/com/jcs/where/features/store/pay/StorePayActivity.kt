package com.jcs.where.features.store.pay

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.order.OrderSubmitActivity
import com.jcs.where.features.gourmet.takeaway.submit.OrderSubmitTakeawayActivity
import com.jcs.where.features.hotel.book.HotelBookActivity
import com.jcs.where.features.mall.buy.MallOrderCommitActivity
import com.jcs.where.features.order.parent.OrderActivity
import com.jcs.where.features.store.order.StoreOrderCommitActivity
import com.jcs.where.features.store.pay.info.PayInfoActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_pay.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2021/6/23 15:10.
 * 商城支付
 */
class StorePayActivity : BaseMvpActivity<StorePayPresenter>(), StorePayView, OnItemClickListener {


    /**
     * 0 商城订单
     * 1 水电订单
     * 2 美食
     * 3 外卖
     * 4 酒店
     * 5 新版商城
     */
    private var useType = 0

    private var totalPrice: Double = 0.0
    private var orderIds = java.util.ArrayList<Int>()
    private var selectedChannel: PayChannel? = null

    private lateinit var mAdapter: StorePayAdapter


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_store_pay

    override fun onBackPressed() = onBackClick()

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))
        intent.extras?.let {
            totalPrice = it.getDouble(Constant.PARAM_TOTAL_PRICE, 0.0)
            val ids = it.getIntegerArrayList(Constant.PARAM_ORDER_IDS)
            if (!ids.isNullOrEmpty()) {
                orderIds.addAll(ids)
            }
            useType = it.getInt(Constant.PARAM_TYPE)
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
            addItemDecoration(DividerDecoration(
                getColor(R.color.colorPrimary), SizeUtils.dp2px(20f),
                0, 0
            ).apply { setDrawHeaderFooter(false) })
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
                putInt(Constant.PARAM_TYPE, useType)
            })
            finish()
        }

        mJcsTitle.setBackIvClickListener {
            onBackClick()
        }
    }


    private fun onBackClick() {
        AlertDialog.Builder(this)
            .setTitle(R.string.prompt)
            .setMessage(R.string.give_up_payment_hint)
            .setCancelable(false)
            .setPositiveButton(R.string.continue_to_pay) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(R.string.give_up) { dialogInterface, i ->
                handleNegative()
                dialogInterface.dismiss()
                finish()
            }
            .create().show()
    }

    private fun handleNegative() {

        // 1.任务栈内存在各个提交订单activity时，跳转至订单列表

        val hotel = ActivityUtils.isActivityExistsInStack(HotelBookActivity::class.java)
        val food = ActivityUtils.isActivityExistsInStack(OrderSubmitActivity::class.java)
        val takeaway = ActivityUtils.isActivityExistsInStack(OrderSubmitTakeawayActivity::class.java)
        val store = ActivityUtils.isActivityExistsInStack(StoreOrderCommitActivity::class.java)
        val mall = ActivityUtils.isActivityExistsInStack(MallOrderCommitActivity::class.java)

        if (hotel || food || takeaway || store || mall) {

            // 2.关闭各个类型的提交订单页
            EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_CANCEL_PAY))
            startActivity(OrderActivity::class.java)
        }

    }

}