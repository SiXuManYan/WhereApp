package com.jcs.where.features.store.cart.child

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.gourmet.comment.FoodCommentFragment
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragemnt_store_cart.*

/**
 * Created by Wangsw  2021/7/5 13:40.
 * 购物车
 */
class StoreCartFragment : BaseMvpFragment<StoreCartPresenter>(), StoreCartView, SwipeRefreshLayout.OnRefreshListener, StoreCartValueChangeListener, OnChildSelectClick, OnGroupSelectClick {

    /**
     *  列表类型（0：自提，1：外卖）
     */
    private var listType = 0

    /**
     * 是否全选
     */
    private var isSelectAll = false

    private lateinit var mAdapter: StoreCartAdapter
    private lateinit var emptyView: EmptyView

    companion object {

        /**
         * 美食评论
         * @param listType  列表类型（0：自提，1：外卖）
         */
        fun newInstance(listType: Int): FoodCommentFragment {
            val fragment = FoodCommentFragment()
            fragment.arguments = Bundle().apply {
                putInt(Constant.PARAM_TYPE, listType)
            }
            return fragment
        }

    }

    override fun getLayoutId() = R.layout.fragemnt_store_cart

    override fun initView(view: View) {
        arguments?.let {
            listType = it.getInt(Constant.PARAM_TYPE, 0)
        }

        swipe_layout.setOnRefreshListener(this)
        emptyView = EmptyView(context).apply {
            showEmptyNothing()
        }
        mAdapter = StoreCartAdapter().apply {
            setEmptyView(emptyView)
            numberChangeListener = this@StoreCartFragment
            onChildSelectClick = this@StoreCartFragment
            onGroupSelectClick = this@StoreCartFragment

        }
        recycler_view.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(
                    ColorUtils.getColor(R.color.colorPrimary),
                    SizeUtils.dp2px(10f),
                    SizeUtils.dp2px(15f),
                    SizeUtils.dp2px(15f)).apply { setDrawHeaderFooter(true) })

        }

    }


    override fun initData() {
        presenter = StoreCartPresenter(this)

    }

    override fun bindListener() {
        select_all_tv.setOnClickListener {
            isSelectAll = !isSelectAll

            // ui
            if (isSelectAll) {
                VibrateUtils.vibrate(10)
                presenter.handleSelectAll(mAdapter, true)
                val handlePrice = presenter.handlePrice(mAdapter)
                total_price_tv.text = StringUtils.getString(R.string.price_unit_format, handlePrice.stripTrailingZeros().toPlainString())
            } else {
                presenter.handleSelectAll(mAdapter, false)
                total_price_tv.text = getString(R.string.price_unit_format, "0.00")
            }

            // data
            presenter.handleSelectAll(mAdapter, isSelectAll)
            select_all_tv.isChecked = isSelectAll
        }
    }

    override fun onRefresh() {

    }

    override fun onChildNumberChange(cartId: Int, add: Boolean) {

    }

    override fun onChildSelected(isChecked: Boolean) {

    }

    override fun onGroupSelected(nativeIsSelect: Boolean) {

    }


}