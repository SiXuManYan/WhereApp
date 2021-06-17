package com.jcs.where.features.store.filter

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.detail.StoreDetailActivity
import com.jcs.where.features.store.filter.screen.ScreenFilterFragment
import com.jcs.where.features.store.search.StoreSearchAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import kotlinx.android.synthetic.main.activity_store_filer.*
import kotlinx.android.synthetic.main.layout_filter_store.*

/**
 * Created by Wangsw  2021/6/9 10:24.
 * 商城筛选
 */
class StoreFilterActivity : BaseMvpActivity<StoreFilterPresenter>(), StoreFilterView, OnItemChildClickListener, OnLoadMoreListener {


    private lateinit var mAdapter: StoreSearchAdapter
    private lateinit var thirdCategoryAdapter: ThirdCategoryAdapter
    private lateinit var emptyView: EmptyView


    /** 一级分类 id */
    private var mPid = 0
    private var page = Constant.DEFAULT_FIRST_PAGE
    private var cateIds: ArrayList<Int> = ArrayList()
    private var moreFilter: ScreenFilterFragment.ScreenMoreFilter? = null


    override fun getLayoutId() = R.layout.activity_store_filer

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        intent.extras?.let {
            mPid = it.getInt(Constant.PARAM_PID, 0)
        }

        initFilter()

        initThirdCategory()
        initContent()
    }

    override fun isStatusDark() = true

    /**
     * 三级分类
     */
    private fun initThirdCategory() {
        thirdCategoryAdapter = ThirdCategoryAdapter()
        thirdCategoryAdapter.setOnItemClickListener { _, _, position ->

            // ui
            val data = thirdCategoryAdapter.data
            data.forEachIndexed { index, category ->
                category.nativeIsSelected = index == position
            }
            thirdCategoryAdapter.notifyDataSetChanged()

            // 筛选 [父级id(即第0项),选中id]
            cateIds.clear()
            val selected = data[position]
            cateIds.apply {
                if (position != 0) {
                    add(data[0].id)
                }
                add(selected.id)
            }
            page = Constant.DEFAULT_FIRST_PAGE
            presenter.getData(page, cateIds, moreFilter)
        }


        three_category_rv.apply {
            adapter = thirdCategoryAdapter
            layoutManager = LinearLayoutManager(this@StoreFilterActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**
     * 筛选
     */
    private fun initFilter() {

        // 筛选
        filter_pager.offscreenPageLimit = 2
        filter_pager.adapter = StoreFilterPagerAdapter(supportFragmentManager, 0).apply {
            pid = mPid
        }

        filter_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                for (i in 0 until category_ll.childCount) {
                    changeFilterTabStyle(position, i)
                }
            }
        })

        // 分类
        classification_ll.setOnClickListener {
            switchFilterPager(0)
        }

        // 其他筛选类型
        screen_ll.setOnClickListener {
            switchFilterPager(1)
        }
        dismiss_view.setOnClickListener {
            handleFilterVisible(View.GONE)
        }


    }

    private fun initContent() {
        emptyView = EmptyView(this).apply {
            showEmptyNothing()
        }


        mAdapter = StoreSearchAdapter().apply {
            setEmptyView(emptyView)
            setOnItemChildClickListener(this@StoreFilterActivity)
        }

        mAdapter.loadMoreModule.apply {
            setOnLoadMoreListener(this@StoreFilterActivity)
            isAutoLoadMore = true
            isEnableLoadMoreIfNotFullPage = true

        }
        recycler_view.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


    }


    override fun initData() {
        presenter = StoreFilterPresenter(this)
        presenter.getData(page, cateIds, moreFilter)
    }

    override fun bindListener() {


    }

    /**
     *  筛选内容
     */
    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        val data = baseEvent.data

        page = Constant.DEFAULT_FIRST_PAGE
        when (data) {
            is Category -> {

                val default = Category().apply {
                    this.id = data.id
                    name = getString(R.string.all)
                    nativeIsSelected = true
                }

                val list = data.child_categories.apply {
                    add(0, default)
                }

                thirdCategoryAdapter.setNewInstance(list)
                three_category_rv.visibility = View.VISIBLE
                cateIds.clear()
                cateIds.add(data.id)
                presenter.getData(page, cateIds, moreFilter)
                handleFilterVisible(View.GONE)
            }
            is ScreenFilterFragment.ScreenMoreFilter -> {
                presenter.getData(page, cateIds, data)
                handleFilterVisible(View.GONE)
            }
        }

    }

    private fun switchFilterPager(index: Int) {
        val currentItem = filter_pager.currentItem

        if (filter_container_ll.visibility == View.GONE) {
            handleFilterVisible(View.VISIBLE)
            changeFilterTabStyle(currentItem, index)
        } else {
            if (currentItem == index) {
                handleFilterVisible(View.GONE)
            }
        }

        filter_pager.setCurrentItem(index, true)
    }


    private fun handleFilterVisible(visibility: Int) {
        filter_container_ll.visibility = visibility

        if (visibility == View.GONE) {

            for (index in 0 until category_ll.childCount) {
                // 清空tab选中状态
                val childTabLL = category_ll.getChildAt(index) as LinearLayout
                val tabText = childTabLL.getChildAt(0) as CheckedTextView
                val tabImage = childTabLL.getChildAt(1) as ImageView
                tabText.isChecked = false
                tabText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                tabImage.setImageResource(R.mipmap.ic_store_down)
            }
        }
    }

    /**
     * 切换tab状态
     */
    private fun changeFilterTabStyle(pagerPosition: Int, tabIndex: Int) {

        val childTabLL = category_ll.getChildAt(tabIndex) as LinearLayout
        val tabText = childTabLL.getChildAt(0) as CheckedTextView
        val tabImage = childTabLL.getChildAt(1) as ImageView
        if (pagerPosition == tabIndex) {
            tabText.isChecked = true
            tabImage.setImageResource(R.mipmap.ic_store_up)
            tabText.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        } else {
            tabText.isChecked = false
            tabImage.setImageResource(R.mipmap.ic_store_down)
            tabText.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        }

    }

    override fun bindData(data: MutableList<StoreRecommend>, lastPage: Boolean) {
        val loadMoreModule = mAdapter.loadMoreModule
        if (data.isEmpty()) {
            if (page == Constant.DEFAULT_FIRST_PAGE) {
                loadMoreModule.loadMoreComplete()
                emptyView.showEmptyDefault()
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
        val data = mAdapter.data[position]
        startActivity(StoreDetailActivity::class.java, Bundle().apply {
            putInt(Constant.PARAM_ID, data.id)
        })
    }

    override fun onLoadMore() {
        page++
        presenter.getData(page, cateIds, moreFilter)
    }

}