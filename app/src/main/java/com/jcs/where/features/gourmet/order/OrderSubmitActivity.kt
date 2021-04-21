package com.jcs.where.features.gourmet.order

import android.graphics.Color
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_order_submit.*

/**
 * Created by Wangsw  2021/4/20 16:54.
 *  提交订单
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
        recycler_view.addItemDecoration(DividerDecoration(
                ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(10f), 0, 0).apply { setDrawHeaderFooter(false) })


        val data = intent.getSerializableExtra(Constant.PARAM_DATA) as ArrayList<ShoppingCartResponse>
        mTotalPrice = intent.getStringExtra(Constant.PARAM_TOTAL_PRICE)!!
        mData.addAll(data)

        val dataList: ArrayList<Products> = ArrayList()
        mData.forEach {
            it.products.forEach { child ->
                dataList.add(child)
            }
        }
        mAdapter.setNewInstance(dataList)

    }

    override fun initData() {
        presenter = OrderSubmitPresenter(this)
    }

    override fun bindListener() {

    }


}