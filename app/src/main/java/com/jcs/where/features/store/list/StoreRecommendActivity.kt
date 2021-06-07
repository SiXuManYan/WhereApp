package com.jcs.where.features.store.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.category.StoryBannerCategory
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.search.history.SearchHistoryActivity
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_list.*
import java.util.*

/**
 * Created by Wangsw  2021/6/1 10:33.
 * estore 商城
 */
class StoreRecommendActivity : BaseMvpActivity<StoreRecommendPresenter>(), StoreRecommendView {

    private lateinit var mBannerAdapter: StoreBannerAdapter
    private lateinit var mAdapter: StoreRecommendAdapter

    override fun getLayoutId() = R.layout.activity_store_list

    override fun initView() {


        initBanner()

        initContent()

    }

    override fun isStatusDark() = true

    private fun initBanner() {
        mBannerAdapter = StoreBannerAdapter()
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        banner_rv.apply {
            layoutManager = manager
            adapter = mBannerAdapter
        }

        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(banner_rv)

        point_view.apply {
            selectedDrawableResId = R.drawable.shape_point_selected
            commonDrawableResId = R.drawable.shape_point_selected_9999
        }

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
        mAdapter = StoreRecommendAdapter()

        val gridLayoutManager = GridLayoutManager(this, 2)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0).apply {
            setDrawHeaderFooter(false)
        }

        content_rv.apply {
            adapter = mAdapter
            layoutManager = gridLayoutManager
            addItemDecoration(decoration)
        }
    }

    override fun initData() {
        presenter = StoreRecommendPresenter(this)

        presenter.getBanner()
        presenter.getRecommend()

    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            finish()
        }
        search_ll.setOnClickListener {
            startActivity(SearchHistoryActivity::class.java)
        }
    }


    override fun bindRecommend(response: ArrayList<StoreRecommend>) =
            mAdapter.setNewInstance(response)

    override fun bindBannerData(result: ArrayList<StoryBannerCategory>) {
        mBannerAdapter.setNewInstance(result)
        point_view.setPointCount(result.size)
    }
}