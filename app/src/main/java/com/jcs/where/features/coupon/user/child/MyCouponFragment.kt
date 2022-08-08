package com.jcs.where.features.coupon.user.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jcs.where.R
import com.jcs.where.api.response.UserCoupon
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.coupon.good.CouponGoodActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/3/2 15:09.
 *
 */
class MyCouponFragment : BaseMvpFragment<MyCouponPresenter>(), MyCouponView {


    /** type 类型  1未使用 2 已使用 3已过期 */
    var type = 0

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: MyCouponAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View?) {
        swipe_layout.apply {

            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
        }

        emptyView = EmptyView(requireContext()).apply {
            setEmptyImage(R.mipmap.ic_empty_coupon)
            setEmptyHint(R.string.no_coupon)
            addEmptyList(this)
        }

        mAdapter = MyCouponAdapter().apply {
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.rule_tv, R.id.use_tv)
            setOnItemChildClickListener(this@MyCouponFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadOnVisible()
            }

        }

        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }

    }

    override fun initData() {
        presenter = MyCouponPresenter(this)
    }

    override fun bindListener() = Unit

    override fun loadOnVisible() {
        presenter.getData(page, type)
    }

    override fun bindData(data: MutableList<UserCoupon>, lastPage: Boolean) {
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
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        if (adapter.getItemViewType(position) != UserCoupon.TYPE_COMMON) {
            return
        }
        val userCoupon = mAdapter.data[position]

        when (view.id) {
            R.id.rule_tv -> {
                BusinessUtils.showRule(requireContext(), userCoupon.rule)
            }
            R.id.use_tv -> {
                startActivity(CouponGoodActivity::class.java, Bundle().apply {
                    putInt(Constant.PARAM_ID, userCoupon.id)
                    putInt(Constant.PARAM_SHOP_ID, userCoupon.shop_id)
                })
            }
        }
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_COUPON_GET -> {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
            else -> {}
        }


    }


}