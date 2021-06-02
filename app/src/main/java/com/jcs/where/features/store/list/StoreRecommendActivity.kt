package com.jcs.where.features.store.list

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.base.mvp.BaseMvpActivity
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
        val manager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        banner_rv.apply {
            layoutManager = manager
            adapter = mBannerAdapter
        }

        val helper = GridPagerSnapHelper(2, 4)
        helper.attachToRecyclerView(banner_rv)

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


    }

    override fun bindBanner(response: ArrayList<Category>) =
            mBannerAdapter.setNewInstance(response)

    override fun bindRecommend(response: ArrayList<StoreRecommend>) =
            mAdapter.setNewInstance(response)
}