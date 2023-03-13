package com.jiechengsheng.city.features.collection.city

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.collection.MyCollection
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jiechengsheng.city.features.hotel.detail.HotelDetailActivity2
import com.jiechengsheng.city.features.mechanism.MechanismActivity
import com.jiechengsheng.city.features.travel.detail.TravelDetailActivity
import com.jiechengsheng.city.news.NewsDetailActivity
import com.jiechengsheng.city.news.NewsVideoActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.view.empty.EmptyView
import com.jiechengsheng.city.widget.calendar.JcsCalendarDialog
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/11/16 16:13.
 * 收藏列表
 */
class CollectionFragment : BaseMvpFragment<CollectionPresenter>(), CollectionView, OnItemClickListener {

    /** type 类型（1：同城，2：文章，3：视频） */
    var type = 0

    private var page = Constant.DEFAULT_FIRST_PAGE
    private lateinit var mAdapter: CollectionAdapter
    private lateinit var emptyView: EmptyView

    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout.apply {
            setBackgroundColor(Color.WHITE)
            setOnRefreshListener {
                page = Constant.DEFAULT_FIRST_PAGE
                loadOnVisible()
            }
        }
        emptyView = EmptyView(requireContext()).apply {
            setEmptyImage(R.mipmap.ic_empty_favorite)
            setEmptyHint(R.string.empty_favorite)
            addEmptyList(this)
        }

        mAdapter = CollectionAdapter().apply {
            setEmptyView(emptyView)
            setOnItemClickListener(this@CollectionFragment)
            loadMoreModule.isEnableLoadMoreIfNotFullPage = true
            loadMoreModule.setOnLoadMoreListener {
                page++
                loadOnVisible()
            }
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(DividerDecoration(Color.WHITE, SizeUtils.dp2px(15f), 0, 0))
        }

    }

    override fun initData() {
        presenter = CollectionPresenter(this)
    }

    override fun bindListener() = Unit

    override fun loadOnVisible() {
        presenter.getData(page, type)
    }

    override fun bindData(data: MutableList<MyCollection>, lastPage: Boolean) {
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

        val collection = mAdapter.data[position]

        when (mAdapter.getItemViewType(position)) {

            MyCollection.TYPE_HOTEL -> {
                collection.hotel?.let {
                    val dialog = JcsCalendarDialog()
                    dialog.initCalendar()
                    HotelDetailActivity2.navigation(requireContext(), it.id, dialog.startBean, dialog.endBean)
                }
            }
            MyCollection.TYPE_TRAVEL -> {
                collection.travel?.let {
                    TravelDetailActivity.navigation(requireContext(), it.id)
                }
            }
            MyCollection.TYPE_GENERAL -> {
                collection.general?.let {
                    MechanismActivity.navigation(requireContext(), it.id)
                }
            }
            MyCollection.TYPE_RESTAURANT -> {
                collection.restaurant?.let {
                    RestaurantDetailActivity.navigation(requireContext(), it.id)
                }
            }
            MyCollection.TYPE_STORE -> {
                collection.estore?.let {
                   // StoreDetailActivity.navigation(requireContext(), it.id)
                }
            }
            MyCollection.TYPE_NEWS -> {
                collection.news?.let {
                    if (it.content_type == 1) {
                        startActivity(NewsDetailActivity::class.java, Bundle().apply {
                            putString(Constant.PARAM_NEWS_ID, it.id.toString())
                        })
                    }
                    if (it.content_type == 2) {
                        startActivity(NewsVideoActivity::class.java, Bundle().apply {
                            putString(Constant.PARAM_NEWS_ID, it.id.toString())
                        })
                    }
                }
            }

        }


    }

}