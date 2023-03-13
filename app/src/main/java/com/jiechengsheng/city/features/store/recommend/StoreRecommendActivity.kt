package com.jiechengsheng.city.features.store.recommend

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.*
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.category.StoryBannerCategory
import com.jiechengsheng.city.api.response.store.StoreRecommend
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.search.SearchAllActivity
import com.jiechengsheng.city.features.store.cart.StoreCartActivity
import com.jiechengsheng.city.features.store.detail.StoreDetailActivity
import com.jiechengsheng.city.utils.Constant
import com.jiechengsheng.city.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_store_list.*
import java.util.*

/**
 * Created by Wangsw  2021/6/1 10:33.
 * estore 商城
 */
class StoreRecommendActivity : BaseMvpActivity<StoreRecommendPresenter>(), StoreRecommendView, OnItemClickListener {

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
            selectedDrawableResId = R.drawable.shape_point_selected_377bff
            commonDrawableResId = R.drawable.shape_point_selected_d8d8d8
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
        mAdapter.setOnItemClickListener(this@StoreRecommendActivity)
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val decoration = DividerDecoration(ColorUtils.getColor(R.color.transplant), SizeUtils.dp2px(10f), 0, 0)
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
            startActivity(SearchAllActivity::class.java,Bundle().apply {
                putInt(Constant.PARAM_TYPE , 5)
            })
        }
        cart_iv.setOnClickListener {
            startActivityAfterLogin(StoreCartActivity::class.java)
        }
    }


    override fun bindRecommend(response: ArrayList<StoreRecommend>) =
            mAdapter.setNewInstance(response)

    override fun bindBannerData(result: ArrayList<StoryBannerCategory>) {
        mBannerAdapter.setNewInstance(result)
        point_view.setPointCount(result.size)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        startActivity(StoreDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
        })

    }


}