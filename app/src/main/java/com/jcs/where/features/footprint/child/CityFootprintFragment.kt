package com.jcs.where.features.footprint.child

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.footprint.Footprint
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.features.mechanism.MechanismActivity
import com.jcs.where.features.travel.detail.TravelDetailActivity
import com.jcs.where.news.NewsDetailActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarDialog
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2022/2/10 10:33.
 *
 */
class CityFootprintFragment : BaseMvpFragment<FootprintPresenter>(), FootprintView, OnItemClickListener {

    /**
     * 0 普通收藏
     * 1 商品收藏
     */
    var type = 0

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: FootprintAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout.apply {
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                presenter.getData(page, type)
            }
        }

        emptyView = EmptyView(requireContext()).apply {
            initEmpty(R.mipmap.ic_empty_card_coupon, R.string.no_content)
        }

        mAdapter = FootprintAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@CityFootprintFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                presenter.getData(page, type)
            }
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(15f), 0, 0))
        }
    }


    override fun initData() {
        presenter = FootprintPresenter(this)

    }

    override fun loadOnVisible() {
        presenter.getData(page, type)
    }


    override fun bindListener() = Unit


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

        val itemViewType = adapter.getItemViewType(position)
        if (itemViewType == Footprint.TYPE_TITLE) {
            return
        }

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

            Footprint.TYPE_GOOD -> {
                MallDetailActivity.navigation(requireContext(), module.id)
            }

        }


    }

}


