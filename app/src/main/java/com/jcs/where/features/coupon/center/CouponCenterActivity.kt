package com.jcs.where.features.coupon.center

import android.graphics.Color
import android.os.Handler
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jcs.where.R
import com.jcs.where.api.response.Coupon
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refresh_list.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Wangsw  2022/3/5 14:42.
 * 领券中心
 */
class CouponCenterActivity : BaseMvpActivity<CouponCenterPresenter>(), CouponCenterView {


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: CouponCenterAdapter
    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {

        mJcsTitle.setMiddleTitle(getString(R.string.coupon_center))

        swipe_layout.apply {
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadData()
            }
        }

        emptyView = EmptyView(this).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_coupon)
        }

        mAdapter = CouponCenterAdapter().apply {
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadData()
            }
            setEmptyView(emptyView)
            addChildClickViewIds(R.id.rule_tv, R.id.get_tv)
            setOnItemChildClickListener(this@CouponCenterActivity)
        }

        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(15f), 0, 0))
        }
    }


    override fun initData() {
        presenter = CouponCenterPresenter(this)
        loadData()
    }

    private fun loadData() {
        presenter.getData(page)
    }

    override fun bindListener() = Unit

    override fun bindData(data: MutableList<Coupon>, lastPage: Boolean) {
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
        view.isClickable = false

        val userCoupon = mAdapter.data[position]

        when (view.id) {
            R.id.rule_tv -> {
                BusinessUtils.showRule(this, userCoupon.rule)
            }
            R.id.get_tv -> {
                presenter.getCoupon(userCoupon.id, userCoupon.couponType)
            }
            else -> {}
        }
        Handler(mainLooper).postDelayed({ view.isClickable = true },2000)

    }

    override fun getCouponResult(message: String) {
        ToastUtils.showShort(message)
        page = Constant.DEFAULT_FIRST_PAGE
        loadData()
        EventBus.getDefault().post(BaseEvent<Any>(EventCode.EVENT_COUPON_GET))

    }
}