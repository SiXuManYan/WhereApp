package com.jcs.where.features.footprint

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.collection.MyCollection
import com.jcs.where.api.response.footprint.Footprint
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.store.detail.StoreDetailActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_refresh_list.*
import kotlinx.android.synthetic.main.activity_refresh_list.recycler
import kotlinx.android.synthetic.main.activity_refresh_list.swipe_layout
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/11/18 16:41.
 * 我的足迹
 */
class FootprintActivity : BaseMvpActivity<FootprintPresenter>(), FootprintView, OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: FootprintAdapter
    private lateinit var emptyView: EmptyView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_refresh_list

    override fun initView() {
        mJcsTitle.setMiddleTitle(getString(R.string.mine_footprint_title))
        swipe_layout.apply {
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadData()
            }
        }

        emptyView = EmptyView(this).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_content)
        }

        mAdapter = FootprintAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@FootprintActivity)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadData()
            }
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(15f), 0, 0))
        }
    }


    override fun initData() {
        presenter = FootprintPresenter(this)
        loadData()
    }


    override fun bindListener() = Unit

    private fun loadData() = presenter.getData(page)

    override fun bindData(data: MutableList<Footprint>, lastPage: Boolean) {
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

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        val module = data.module_data ?: return

        when (data.type) {
            Footprint.TYPE_HOTEL -> {
                val dialog = JcsCalendarDialog()
                dialog.initCalendar(this@FootprintActivity)
                HotelDetailActivity2.navigation(this, module.id, dialog.startBean, dialog.endBean)
            }
            Footprint.TYPE_TRAVEL -> {
                TravelDetailActivity.navigation(this@FootprintActivity, module.id)
            }
            Footprint.TYPE_GENERAL -> {
                MechanismActivity.navigation(this@FootprintActivity, module.id)
            }
            Footprint.TYPE_RESTAURANT -> {
                RestaurantDetailActivity.navigation(this@FootprintActivity, module.id)
            }
            Footprint.TYPE_STORE -> {
                StoreDetailActivity.navigation(this@FootprintActivity, module.id)
            }
            Footprint.TYPE_NEWS -> {
                startActivity(NewsDetailActivity::class.java, Bundle().apply {
                    putString(Constant.PARAM_NEWS_ID, module.id.toString())
                })
            }

        }


    }
}