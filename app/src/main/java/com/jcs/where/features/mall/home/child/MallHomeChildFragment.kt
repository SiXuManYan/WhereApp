package com.jcs.where.features.mall.home.child

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.category.StoryBannerCategory
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.fragment_mall_home_child.*

/**
 * Created by Wangsw  2021/11/30 17:01.
 * 商城首页商品
 */
class MallHomeChildFragment : BaseMvpFragment<MallHomeChildPresenter>(), MallHomeChildView, OnItemClickListener {

    /** 当前页对应的一级分类 */
    lateinit var targetFirstCategory: Category

    private lateinit var mBannerAdapter: MallHomeChildBannerAdapter
    private lateinit var mAdapter: MallRecommendAdapter

    override fun getLayoutId() = R.layout.fragment_mall_home_child

    override fun initView(view: View) {
        initBanner()
        initContent()
    }

    private fun initBanner() {
        if (!::targetFirstCategory.isInitialized) return

        point_view.apply {
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            commonDrawableResId = R.drawable.shape_point_selected_d8d8d8
        }

        mBannerAdapter = MallHomeChildBannerAdapter()
        banner_rv.apply {
            adapter = mBannerAdapter
            layoutManager    = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(banner_rv)


        banner_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        point_view.onPageSelected(firstItemPosition)
                    }
                }
            }
        })



    }

    private fun initContent() {
        mAdapter = MallRecommendAdapter()
        mAdapter.setOnItemClickListener(this@MallHomeChildFragment)
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
    }

    override fun initData() {
        presenter = MallHomeChildPresenter(this)
        loadData()
    }

    private fun loadData() {
        presenter.handleBanner(targetFirstCategory)
        presenter.getRecommend()
    }

    override fun bindListener() {

    }

    override fun bindBannerData(result: ArrayList<StoryBannerCategory>) {
        mBannerAdapter.setNewInstance(result)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        // todo 新版商城详情
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)

        when (baseEvent.code) {
            EventCode.EVENT_SELECTED_CATEGORY -> {
                val category = baseEvent.data as Category
                targetFirstCategory = category
                loadData()
            }
            else -> {
            }
        }




    }


}