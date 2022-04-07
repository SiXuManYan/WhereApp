package com.jcs.where.features.hotel.map.child

import android.graphics.Color
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.hotel.detail.HotelDetailActivity2
import com.jcs.where.features.hotel.home.HotelHomeRecommendAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.calendar.JcsCalendarAdapter
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.single_recycler_view.*

/**
 * Created by Wangsw  2021/9/27 15:48.
 * 酒店列表
 */
class HotelChildFragment : BaseMvpFragment<HotelChildPresenter>(), HotelChildView {


    var searchInput: String? = null
    var star_level: String? = null
    var hotel_type_ids: String? = null
    var price_range: String? = null
    var grade: String? = null

    /** 房间数量 */
    var roomNumber = 1

    lateinit var mStartDateBean: JcsCalendarAdapter.CalendarBean
    lateinit var mEndDateBean: JcsCalendarAdapter.CalendarBean

    /** 搜索改变时，延时刷新 */
    private var needRefresh = false


    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: HotelHomeRecommendAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.single_recycler_view


    override fun initView(view: View) {
        initList()
    }

    private fun initList() {
        emptyView = EmptyView(context).apply {
            initEmpty(
                R.mipmap.ic_empty_search, R.string.empty_search,
                R.string.empty_search_hint, R.string.back
            ) {

            }
            action_tv.visibility = View.GONE
        }


        mAdapter = HotelHomeRecommendAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@HotelChildFragment)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener(this@HotelChildFragment)
        }

        content_rv.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(16f), SizeUtils.dp2px(15f), SizeUtils.dp2px(15f)))

        }
        val padding = SizeUtils.dp2px(15f)
        content_rv.setPaddingRelative(padding, 0, padding, 0)


    }

    override fun initData() {
        presenter = HotelChildPresenter(this)
    }

    override fun loadOnVisible() {
        if (isViewVisible) {
            presenter.getData(page, searchInput, star_level, hotel_type_ids, price_range, grade)
        }
    }

    override fun bindListener() = Unit

    override fun onLoadMore() {
        page++
        loadOnVisible()
    }

    override fun bindList(toMutableList: MutableList<HotelHomeRecommend>, lastPage: Boolean) {

        val loadMoreModule = mAdapter.loadMoreModule
        if (toMutableList.isEmpty()) {
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
            mAdapter.setNewInstance(toMutableList)
            loadMoreModule.checkDisableLoadMoreIfNotFullPage()

        } else {
            mAdapter.addData(toMutableList)
            if (lastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        HotelDetailActivity2.navigation(
            requireContext(),
            data.id,
            mStartDateBean,
            mEndDateBean,
            star_level,
            price_range,
            grade,
            roomNumber
        )
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && needRefresh) {
            loadOnVisible()
            needRefresh = false
        }

    }

    override fun onEventReceived(baseEvent: BaseEvent<*>?) {
        super.onEventReceived(baseEvent)
        if (baseEvent == null) {
            return
        }

        when (baseEvent.code) {
            EventCode.EVENT_REFRESH_CHILD -> {
                searchInput = baseEvent.message
                page = Constant.DEFAULT_FIRST_PAGE
                if (isViewVisible) {
                    loadOnVisible()
                } else {
                    needRefresh = true
                }
            }
            EventCode.EVENT_REFRESH_CHILD_START_DATE -> {
                mStartDateBean  = baseEvent.data as JcsCalendarAdapter.CalendarBean

            }
            EventCode.EVENT_REFRESH_CHILD_END_DATE -> {
                mEndDateBean  = baseEvent.data as JcsCalendarAdapter.CalendarBean
            }

        }
    }


}