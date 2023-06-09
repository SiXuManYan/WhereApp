package com.jiechengsheng.city.features.gourmet.cart

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.cart.Products
import com.jiechengsheng.city.api.response.gourmet.cart.ShoppingCartResponse
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.gourmet.order.OrderSubmitActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.NumberView
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/7 14:49.
 * 餐饮美食购物车
 *
 */
class ShoppingCartActivity : BaseMvpActivity<ShoppingCartPresenter>(), ShoppingCartView,
    OnLoadMoreListener,
    SwipeRefreshLayout.OnRefreshListener,
    NumberView.OnValueChangerListener,
    ShoppingCartAdapter.OnUserSelectListener {

    private lateinit var mAdapter: ShoppingCartAdapter
    private var page = Constant.DEFAULT_FIRST_PAGE
    private var isEditMode = false
    private var isSelectAll = false

    override fun getLayoutId() = R.layout.activity_shopping_cart

    private lateinit var emptyView: EmptyView

    override fun initView() {
        BarUtils.setStatusBarColor(this, getColor(R.color.white))

        emptyView = EmptyView(this).apply {
            setEmptyImage(R.mipmap.ic_empty_cart)
            setEmptyHint(R.string.cart_is_empty)
            addEmptyList(this)
        }

        mAdapter = ShoppingCartAdapter().apply {
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            setEmptyView(emptyView)
            loadMoreModule.setOnLoadMoreListener(this@ShoppingCartActivity)
            numberChangeListener = this@ShoppingCartActivity
            onUserSelectListener = this@ShoppingCartActivity
        }

        recycler_view.adapter = mAdapter
        recycler_view.addItemDecoration(DividerDecoration(
            ColorUtils.getColor(R.color.grey_F5F5F5), 1, 0, 0
        ).apply { setDrawHeaderFooter(true) })


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

        select_all_ll.setOnClickListener {
            isSelectAll = !isSelectAll

            if (isSelectAll) {
                VibrateUtils.vibrate(10)
                presenter.handleSelectAll(mAdapter, true)
                val handlePrice = presenter.handlePrice(mAdapter)
                total_price_tv.text =
                    StringUtils.getString(R.string.price_unit_format, handlePrice.toPlainString())
            } else {
                presenter.handleSelectAll(mAdapter, false)
                totalPrice = BigDecimal.ZERO
                total_price_tv.text = getString(R.string.price_unit_format, totalPrice.toPlainString())
            }

            changeSelectImage(isSelectAll)
        }

        // 删除
        val delete = ArrayList<Int>()
        val deleteItem = ArrayList<Products>()
        val deleteParent = ArrayList<ShoppingCartResponse>()
        delete_tv.setOnClickListener {
            delete.clear()
            deleteItem.clear()
            deleteParent.clear()

            mAdapter.data.forEach {
                it.products.forEach { child ->
                    if (child.nativeIsSelect) {
                        delete.add(child.cart_id)
                        deleteItem.add(child)
                    }
                }
            }

            if (deleteItem.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.hint)
                    .setMessage(getString(R.string.confirm_delete_hint))
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        presenter.deleteCart(delete)
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog?.dismiss()
                    }
                    .create()
                    .show()
            }
        }

        // 提交订单
        buy_after_tv.setOnClickListener {
            if (mAdapter.data.isEmpty()) {
                return@setOnClickListener
            }
            val totalPrice = presenter.handlePrice(mAdapter)
            if (totalPrice.compareTo(BigDecimal.ZERO) < 1) {
                // TODO 未考虑特价。判断是否有选中，而不是价格为0
                ToastUtils.showShort(getString(R.string.please_select_a_product))
                return@setOnClickListener
            }

            startActivityAfterLogin(OrderSubmitActivity::class.java, Bundle().apply {

                val selectedList = presenter.getSelectedList(mAdapter)

                putSerializable(Constant.PARAM_DATA, selectedList)

                putString(Constant.PARAM_TOTAL_PRICE, totalPrice.toPlainString())
            })
        }

    }

    private fun changeSelectImage(selected: Boolean) {
        if (selected) {
            all_iv.setImageResource(R.mipmap.ic_checked_blue)
        } else {
            all_iv.setImageResource(R.mipmap.ic_un_checked)
        }

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
                mAdapter.setNewInstance(null)
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyContainer()
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
        getNowPrice()

    }

    override fun onNumberChange(cartId: Int, isAdd: Boolean, goodNum: Int) {

        // 重新计算价格
        getNowPrice()

        //
        presenter.changeCartNumber(cartId, isAdd)

    }

    override fun onTitleSelectClick(isSelected: Boolean) {
        getNowPrice()

        isSelectAll = if (isSelected) {
            presenter.checkSelectAll(mAdapter)
        } else {
            false
        }
        changeSelectImage(isSelectAll)
    }

    private fun getNowPrice() {
        val handlePrice = presenter.handlePrice(mAdapter)
        val toPlainString = handlePrice.toPlainString()
        total_price_tv.text = StringUtils.getString(R.string.price_unit_format, toPlainString)
    }


    override fun onChildSelectClick(isSelected: Boolean) {
        getNowPrice()

        if (isSelected) {
            isSelectAll = true
            presenter.checkSelectAll(mAdapter)
        } else {
            isSelectAll = false
        }
        changeSelectImage(isSelectAll)
    }

    override fun deleteSuccess() {
        onRefresh()
    }

}