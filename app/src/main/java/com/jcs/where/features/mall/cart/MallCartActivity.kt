package com.jcs.where.features.mall.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.VibrateUtils
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCartGroup
import com.jcs.where.api.response.mall.MallCartItem
import com.jcs.where.api.response.mall.MallSpecs
import com.jcs.where.api.response.mall.SkuDataSource
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.buy.MallOrderCommitActivity
import com.jcs.where.features.mall.detail.sku.MallSkuFragment
import com.jcs.where.features.mall.detail.sku.MallSkuSelectResult
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_mall_cart.*


/**
 * Created by Wangsw  2021/12/14 17:49.
 * 商城购物车
 */
class MallCartActivity : BaseMvpActivity<MallCartPresenter>(), MallCartView, MallSkuSelectResult {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var emptyView: EmptyView
    private lateinit var mAdapter: MallCartAdapter
    private lateinit var mSkuDialog: MallSkuFragment

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_mall_cart

    override fun initView() {
        swipe_layout.apply {
            setOnRefreshListener(this@MallCartActivity)
            swipe_layout.isEnabled = false
            setColorSchemeColors(ColorUtils.getColor(R.color.blue_4C9EF2))
        }
        emptyView = EmptyView(this).apply {
            showEmptyDefault()
            setEmptyImage(R.mipmap.ic_empty_search)
            this.empty_message_tv.text = StringUtils.getString(R.string.empty_data_cart)
        }
        mAdapter = MallCartAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            numberChangeListener = this@MallCartActivity
            onChildSelectClick = this@MallCartActivity
            onGroupSelectClick = this@MallCartActivity
            onChildReselectSkuClick = this@MallCartActivity
            onDeleteExpiredClick = View.OnClickListener {
                presenter.clearStoreCart(1)
            }

        }
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        mSkuDialog = MallSkuFragment().apply {
            selectResult = this@MallCartActivity
        }


    }

    override fun initData() {
        presenter = MallCartPresenter(this)
        onRefresh()
    }

    @SuppressLint("NotifyDataSetChanged")
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
                // 数量
                // select_count_tv.text = getString(R.string.total_format, presenter.getSelectedCount(mAdapter))
            } else {
                total_price_tv.text = getString(R.string.price_unit_format, "0.00")
                // 数量
                // select_count_tv.text = getString(R.string.total_format, 0)
            }

            select_all_tv.isChecked = isSelectAll
        }

        delete_tv.setOnClickListener {

            if (select_all_tv.isChecked) {
                presenter.clearStoreCart(0)
//                mAdapter.setNewInstance(null)
//                emptyView.showEmptyDefault()

                // 565665656


            } else {
                presenter.deleteCart(mAdapter)
            }
        }


        edit_tv.setOnClickListener {
            right_vs.displayedChild = 1
            bottom_vs.displayedChild = 1
            total_price_ll.visibility = View.GONE
            mAdapter.isEditMode = true
            mAdapter.notifyDataSetChanged()
            select_all_tv.isChecked = presenter.checkSelectAll(mAdapter)
        }

        cancel_tv.setOnClickListener {
            right_vs.displayedChild = 0
            bottom_vs.displayedChild = 0
            total_price_ll.visibility = View.VISIBLE
            mAdapter.isEditMode = false
            mAdapter.notifyDataSetChanged()
            select_all_tv.isChecked = presenter.checkSelectAll(mAdapter)
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
                select_all_tv.visibility = View.GONE
                bottom_rl.visibility = View.GONE
                bottom_v.visibility = View.GONE
            } else {
                loadMoreModule.loadMoreEnd()
            }
            presenter.getExpiredGoods()
            return
        }
        select_all_tv.visibility = View.VISIBLE
        bottom_rl.visibility = View.VISIBLE
        bottom_v.visibility = View.VISIBLE
        if (page == Constant.DEFAULT_FIRST_PAGE) {
            mAdapter.setNewInstance(data)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()
        } else {
            mAdapter.addData(data)

        }
        if (lastPage) {
            presenter.getExpiredGoods()
        } else {
            loadMoreModule.loadMoreComplete()
        }
        getNowPrice()

    }

    override fun onRefresh() {
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getData(page)
    }


    override fun onChildNumberChange(cartId: Int, add: Boolean, number: Int) {
        presenter.changeCartNumber(cartId, number)
    }


    override fun onChildSelected(checked: Boolean) {
        getNowPrice()
        select_all_tv.isChecked = if (checked) {
            presenter.checkSelectAll(mAdapter)
        } else {
            false
        }
    }


    override fun onGroupSelected(nativeIsSelect: Boolean) {

        select_all_tv.isChecked = if (nativeIsSelect) {
            presenter.checkSelectAll(mAdapter)
        } else {
            false
        }
        getNowPrice()
    }


    override fun changeNumberSuccess() {
        getNowPrice()
    }

    override fun deleteSuccess() {
        onRefresh()
        select_all_tv.isChecked = presenter.checkSelectAll(mAdapter)
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

    private var mAdapterPosition = 0
    private var mChildIndex = 0
    private lateinit var mGwcSource: MallCartItem


    override fun reselectSkuClick(childIndex: Int, adapterPosition: Int, source: MallCartItem) {
        mChildIndex = childIndex
        mAdapterPosition = adapterPosition
        mGwcSource = source

        val goodsInfo = source.goods_info ?: return
        mSkuDialog.data = SkuDataSource().apply {
            main_image = goodsInfo.main_image
            min_price = goodsInfo.min_price
            stock = 0
            attribute_list.clear()
            attribute_list.addAll(goodsInfo.attribute_list)
            specs.clear()
            specs.addAll(source.specs)
        }
        mSkuDialog.show(supportFragmentManager, mSkuDialog.tag)
    }

    override fun selectResult(mallSpecs: MallSpecs, goodNum: Int) {

        // test

//        if (BuildConfig.FLAVOR == "dev") {
//            changeSkuSuccess(mallSpecs, goodNum)
//        } else {
//            presenter.changeSku(mGwcSource.cart_id, mallSpecs.specs_id, goodNum, mallSpecs)
//        }
        presenter.changeSku(mGwcSource.cart_id, mallSpecs.specs_id, goodNum, mallSpecs)
    }


    override fun changeSkuSuccess(mallSpecs: MallSpecs, number: Int) {
        ToastUtils.showShort(R.string.modify_success)
        mGwcSource.apply {
            specs_id = mallSpecs.specs_id
            good_id = mallSpecs.goods_id
            good_num = number
        }
        mGwcSource.specs_info?.let {
            it.specs = mallSpecs.specs
            it.price = mallSpecs.price
            it.stock = mallSpecs.stock
            it.delete_status = 0
        }

        mAdapter.getItem(mAdapterPosition).gwc[mChildIndex] = mGwcSource
        mAdapter.notifyItemChanged(mAdapterPosition)
        getNowPrice()
    }

    override fun clearStoreCartSuccess() {
        onRefresh()
    }

    override fun bindExpired(apply: MallCartGroup) {
        mAdapter.addData(apply)
        mAdapter.loadMoreModule.loadMoreEnd()
    }

    override fun bindExpiredEmpty() {
        mAdapter.loadMoreModule.loadMoreEnd()
    }

}