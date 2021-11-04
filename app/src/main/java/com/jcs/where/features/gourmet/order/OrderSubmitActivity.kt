package com.jcs.where.features.gourmet.order

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.api.response.gourmet.order.FoodOrderSubmitData
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.pay.StorePayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_order_submit.*

/**
 * Created by Wangsw  2021/4/20 16:54.
 * 美食提交订单
 */
class OrderSubmitActivity : BaseMvpActivity<OrderSubmitPresenter>(), OrderSubmitView {

    private lateinit var mAdapter: OrderSubmitAdapter
    private val mData: ArrayList<ShoppingCartResponse> = ArrayList()
    private lateinit var mTotalPrice: String

    override fun getLayoutId() = R.layout.activity_order_submit

    override fun isStatusDark() = true

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)

        val emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }
        mAdapter = OrderSubmitAdapter().apply {
            setEmptyView(emptyView)
        }
        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycler_view.addItemDecoration(DividerDecoration(
                ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(false) })
    }

    override fun initData() {
        presenter = OrderSubmitPresenter(this)

        val data = intent.getSerializableExtra(Constant.PARAM_DATA) as ArrayList<ShoppingCartResponse>
        mTotalPrice = intent.getStringExtra(Constant.PARAM_TOTAL_PRICE)!!
        mData.addAll(data)

        val dataList: ArrayList<Products> = ArrayList()
        mData.forEach {
            it.products.forEach { child ->
                if (child.nativeIsSelect) {
                    dataList.add(child)
                }

            }
        }
        mAdapter.setNewInstance(dataList)
        total_price_tv.text = getString(R.string.price_unit_format, mTotalPrice)
    }

    override fun bindListener() {
        buy_after_tv.setOnClickListener {

            if (mAdapter.data.isEmpty()) {
                return@setOnClickListener
            }
            val phone = phone_et.text.toString().trim()

            presenter.submitOrder(mAdapter.data, phone)
        }
    }

    override fun summitSuccess(response: FoodOrderSubmitData) {

        val orderIds = ArrayList<Int>()
        response.orders.forEach {
            orderIds.add(it.id)
        }

        startActivityAfterLogin(StorePayActivity::class.java, Bundle().apply {
            putDouble(Constant.PARAM_TOTAL_PRICE, mTotalPrice.toDouble())
            putIntegerArrayList(Constant.PARAM_ORDER_IDS, orderIds)
            putInt(Constant.PARAM_TYPE, Constant.PAY_INFO_FOOD)
        })
        finish()
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_CANCEL_PAY) {
            finish()
        }
    }


}