package com.jcs.where.features.gourmet.cart

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.Constant
import com.jcs.where.widget.NumberView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import kotlinx.android.synthetic.main.activity_upgrade.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/7 14:49.
 * 购物车
 */
class ShoppingCartActivity : BaseMvpActivity<ShoppingCartPresenter>(), ShoppingCartView,
        OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        NumberView.OnValueChangerListener, ShoppingCartAdapter.OnUserSelectListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: ShoppingCartAdapter
    private var isEditMode = false

    override fun getLayoutId() = R.layout.activity_shopping_cart

    override fun initView() {

        mAdapter = ShoppingCartAdapter()
        mAdapter.apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            setEmptyView(R.layout.view_empty_data_brvah_default)
            loadMoreModule.setOnLoadMoreListener(this@ShoppingCartActivity)
            numberChangeListener = this@ShoppingCartActivity
            onUserSelectListener = this@ShoppingCartActivity
        }

        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(DividerDecoration(
                ColorUtils.getColor(R.color.colorPrimary),
                SizeUtils.dp2px(10f),
                SizeUtils.dp2px(15f),
                SizeUtils.dp2px(15f)).apply { setDrawHeaderFooter(true) })


    }

    override fun isStatusDark() = true

    override fun initData() {
        swipe_layout.setOnRefreshListener(this)
        presenter = ShoppingCartPresenter(this)
        presenter.getData(page)
    }

    var totalPrice: BigDecimal = BigDecimal.ZERO

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }

        edit_tv.setOnClickListener {
            isEditMode = true
            right_vs.displayedChild = 1
            bottom_vs.displayedChild = 1
            total_price_title_tv.visibility = View.GONE
            total_price_tv.visibility = View.GONE
        }

        cancel_tv.setOnClickListener {
            isEditMode = false
            right_vs.displayedChild = 0
            bottom_vs.displayedChild = 0
            total_price_title_tv.visibility = View.VISIBLE
            total_price_tv.visibility = View.VISIBLE
        }


        select_all_cb.setOnCheckedChangeListener { _, isChecked ->
            mAdapter.m_select_all_iv.performClick()

            if (isChecked) {
                select_all_cb.isEnabled = false
                // 计算价格
                Handler(Looper.getMainLooper()).postDelayed({
                    select_all_cb.isEnabled = true
                    presenter.handlePrice(mAdapter, total_price_tv)
                }, 55)
            } else {
                totalPrice = BigDecimal.ZERO
                total_price_tv.text = getString(R.string.price_unit_format, totalPrice.stripTrailingZeros().toPlainString())
            }
        }

    }


    private fun handlePrice2(): BigDecimal {

        var totalPrice: BigDecimal = BigDecimal.ZERO
        mAdapter.data.forEachIndexed { _, data ->
            data.products.forEach {
                if (it.nativeIsSelect) {
                    val price = it.good_data.price
                    val goodNum = it.good_num
                    val currentItemPrice = BigDecimalUtil.mul(price, BigDecimal(goodNum))
                    totalPrice = BigDecimalUtil.add(currentItemPrice, totalPrice)
                }
            }
        }
        return totalPrice

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page)
    }


    override fun bindList(data: MutableList<ShoppingCartResponse>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete()
            } else {
                loadMoreModule.loadMoreEnd()
            }
            return
        }
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onNumberChange(cartId: Int, isAdd: Boolean) {

    }

    override fun onTitleSelectClick() {
       presenter.handlePrice(mAdapter,total_price_tv)
    }

    override fun onChildSelectClick() {
        presenter.handlePrice(mAdapter,total_price_tv)
    }

}