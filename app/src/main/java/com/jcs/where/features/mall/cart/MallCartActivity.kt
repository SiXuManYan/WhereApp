package com.jcs.where.features.mall.cart

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.buy.MallOrderCommitActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_mall_cart.*



/**
 * Created by Wangsw  2021/12/14 17:49.
 * 商城购物车
 */
class MallCartActivity : BaseMvpActivity<MallCartPresenter>(), MallCartView {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: MallCartAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_cart

    override fun initView() {
        swipe_layout.apply {
            setOnRefreshListener(this@MallCartActivity)
            setColorSchemeColors(ColorUtils.getColor(R.color.blue_4C9EF2))
        }
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
        }
        mAdapter = MallCartAdapter().apply {
            setEmptyView(emptyView)
            numberChangeListener = this@MallCartActivity
            onChildSelectClick = this@MallCartActivity
            onGroupSelectClick = this@MallCartActivity

        }
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


    }

    override fun initData() {
        presenter = MallCartPresenter(this)
        onRefresh()
    }

    override fun bindListener() {
        select_all_tv.setOnClickListener {
            var isSelectAll = select_all_tv.isChecked
            isSelectAll = !isSelectAll

            // data
            presenter.handleSelectAll(mAdapter, isSelectAll)

            // ui
            if (isSelectAll) {
                VibrateUtils.vibrate(10)
                val handlePrice = presenter.handlePrice(mAdapter)
                total_price_tv.text =
                    StringUtils.getString(R.string.price_unit_format, handlePrice.stripTrailingZeros().toPlainString())
//                select_count_tv.text = getString(R.string.total_format, presenter.getSelectedCount(mAdapter))
            } else {
                total_price_tv.text = getString(R.string.price_unit_format, "0.00")
//                select_count_tv.text = getString(R.string.total_format, 0)
            }

            select_all_tv.isChecked = isSelectAll
        }

        delete_tv.setOnClickListener {

            if (select_all_tv.isChecked) {
                presenter.clearStoreCart()
                mAdapter.setNewInstance(null)
                emptyView.showEmptyDefault()
            } else {

                presenter.deleteCart(mAdapter)
            }
        }


        edit_tv.setOnClickListener {
            right_vs.displayedChild = 1
            bottom_vs.displayedChild = 1
            total_price_tv.visibility = View.GONE

        }

        cancel_tv.setOnClickListener {
            right_vs.displayedChild = 0
            bottom_vs.displayedChild = 0
            total_price_tv.visibility = View.VISIBLE

        }


        settlement_tv.setOnClickListener {

            val selectedCount = presenter.getSelectedCount(mAdapter)
            if (selectedCount <= 0) {
                ToastUtils.showShort(R.string.please_select_a_product)
                return@setOnClickListener
            }

            val selectedData = presenter.getSelectedData(mAdapter)

            startActivityAfterLogin(MallOrderCommitActivity::class.java, Bundle().apply {
                putSerializable(Constant.PARAM_DATA, selectedData)
            })
        }


    }

    override fun bindData(data: MutableList<MallCartGroup>, lastPage: Boolean) {
        if (swipe_layout.isRefreshing) {
            swipe_layout.isRefreshing = false
        }

        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                mAdapter.setNewInstance(null)
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
        getNowPrice()
    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }

    override fun onChildNumberChange(cartId: Int, add: Boolean) = Unit

    override fun onChildNumberChange(cartId: Int, add: Boolean, number: Int) {
        presenter.changeCartNumber(cartId,number)
    }


    override fun onChildSelected(checked: Boolean) {

    }

    override fun onGroupSelected(nativeIsSelect: Boolean) {

    }

    private fun getNowPrice() {
        val handlePrice = presenter.handlePrice(mAdapter)
        val toPlainString = handlePrice.toPlainString()
        total_price_tv.text = StringUtils.getString(R.string.price_unit_format, toPlainString)
    }



    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_ORDER_LIST -> {
//                // 支付成功，手动调用接口删除
//                presenter.deleteCart(mAdapter)
            }
            EventCode.EVENT_CANCEL_PAY -> {
                finish()
            }
        }
    }


}