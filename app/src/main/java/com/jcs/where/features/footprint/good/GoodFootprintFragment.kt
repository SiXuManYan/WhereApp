package com.jcs.where.features.footprint.good

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.footprint.Footprint
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.footprint.FootprintAdapter
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/2/10 15:49.
 *
 */
class GoodFootprintFragment : BaseMvpFragment<GoodFootprintPresenter>(), GoodFootprintView, OnItemClickListener {

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: FootprintAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout.apply {
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadData()
            }
        }

        emptyView = EmptyView(requireContext()).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_content)
        }

        mAdapter = FootprintAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@GoodFootprintFragment)
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
        presenter = GoodFootprintPresenter(this)
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
                dialog.initCalendar(activity)
                HotelDetailActivity2.navigation(requireContext(), module.id, dialog.startBean, dialog.endBean)
            }
            Footprint.TYPE_TRAVEL -> {
                TravelDetailActivity.navigation(requireContext(), module.id)
            }
            Footprint.TYPE_GENERAL -> {
                MechanismActivity.navigation(requireContext(), module.id)
            }
            Footprint.TYPE_RESTAURANT -> {
                RestaurantDetailActivity.navigation(requireContext(), module.id)
            }
            Footprint.TYPE_STORE -> {
                // StoreDetailActivity.navigation(this@FootprintActivity, module.id)
            }
            Footprint.TYPE_NEWS -> {
                startActivity(NewsDetailActivity::class.java, Bundle().apply {
                    putString(Constant.PARAM_NEWS_ID, module.id.toString())
                })
            }

        }


    }
}


interface GoodFootprintView : BaseMvpView {
    fun bindData(data: MutableList<Footprint>, lastPage: Boolean)

}

class GoodFootprintPresenter(private var view: GoodFootprintView) : BaseMvpPresenter(view) {
    fun getData(page: Int) {

        requestApi(mRetrofit.getGoodFootprint(page), object : BaseMvpObserver<PageResponse<Footprint>>(view) {
            override fun onSuccess(response: PageResponse<Footprint>) {

                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }
}

