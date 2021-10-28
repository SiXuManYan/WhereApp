package com.jcs.where.features.gourmet.restaurant.list

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jcs.where.R
import com.jcs.where.api.response.area.AreaResponse
import com.jcs.where.api.response.category.Category
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.bean.RestaurantListRequest
import com.jcs.where.features.gourmet.restaurant.detail.RestaurantDetailActivity
import com.jcs.where.features.gourmet.restaurant.list.filter.more.MoreFilterFragment.MoreFilter
import com.jcs.where.features.gourmet.restaurant.map.RestaurantMapActivity
import com.jcs.where.features.gourmet.takeaway.TakeawayActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.empty.EmptyView
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_gourmet_list.*
import kotlinx.android.synthetic.main.layout_filter.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Wangsw  2021/3/24 13:56.
 * 美食首页
 */
class RestaurantHomeActivity : BaseMvpActivity<RestaurantHomePresenter>(), RestaurantHomeView {

    private var page = Constant.DEFAULT_FIRST_PAGE

    private lateinit var mAdapter: DelicacyAdapter
    private lateinit var mRequest: RestaurantListRequest
    private lateinit var emptyView: EmptyView

    private var mCategoryId = 0
    private var mPidName = ""

    override fun getLayoutId() = R.layout.activity_gourmet_list

    override fun initView() {
        initExtra()
        initFilter()
        initList()
    }


    private fun initExtra() {
        val bundle = intent.extras ?: return
        mCategoryId = bundle.getInt(Constant.PARAM_PID, 89)
        mPidName = bundle.getString(Constant.PARAM_PID_NAME, "")
        food_tv.text = mPidName
    }

    private fun initFilter() {
        filter_pager.offscreenPageLimit = 2
        val adapter = RestaurantPagerAdapter(supportFragmentManager, 0)
        adapter.pid = mCategoryId
        adapter.pidName = mPidName
        filter_pager.adapter = adapter
    }


    private fun initList() {
        // list
        emptyView = EmptyView(this).apply {
            initEmpty(R.mipmap.ic_empty_search, R.string.empty_search, R.string.empty_search_hint, R.string.back) {}
            action_tv.visibility = View.GONE
        }

        mAdapter = DelicacyAdapter().apply {
            setEmptyView(emptyView)
            loadMoreModule.isAutoLoadMore = true
            loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            addChildClickViewIds(R.id.takeaway_support_tv)
            setOnItemChildClickListener(this@RestaurantHomeActivity)
            setOnItemClickListener(this@RestaurantHomeActivity)
            loadMoreModule.setOnLoadMoreListener(this@RestaurantHomeActivity)
        }
        recycler.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerDecoration(
                    Color.TRANSPARENT,
                    SizeUtils.dp2px(16f),
                    SizeUtils.dp2px(8f),
                    SizeUtils.dp2px(8f)
                ).apply {
                    setDrawHeaderFooter(false)
                })
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

    }


    override fun isStatusDark() = true

    override fun initData() {
        presenter = RestaurantHomePresenter(this)
        mRequest = RestaurantListRequest()
        mRequest.category_id = mCategoryId.toString()
        onRefresh()
    }

    override fun bindListener() {
        swipe_layout.setOnRefreshListener(this)
        back_iv.setOnClickListener { finish() }
        clearIv.setOnClickListener { onClearSearchClick() }
        area_filter_ll.setOnClickListener { onAreaFilterClick() }
        food_filter_ll.setOnClickListener { onFoodFilterClick() }
        other_filter_ll.setOnClickListener { onOtherFilterClick() }
        dismiss_view.setOnClickListener { onFilterDismissClick() }
        filter_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageSelected(position: Int) {
                for (i in 0 until category_ll.childCount) {
                    changeFilterTabStyle(position, i)
                }
            }
        })
        city_et.setOnEditorActionListener { _: TextView, actionId: Int, _: KeyEvent ->

            val searchKey = city_et.text.toString().trim()
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mRequest.search_input = searchKey
                onRefresh()
                KeyboardUtils.hideSoftInput(city_et)
                return@setOnEditorActionListener true
            }
            false
        }
        city_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable) {
                val trim = s.toString().trim()
                clearIv.visibility = if (trim.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        })
        map_iv.setOnClickListener {
            startActivity(RestaurantMapActivity::class.java, Bundle().apply {
                putString(Constant.PARAM_ID, mCategoryId.toString())
            })
        }
    }

    private fun onClearSearchClick() {
        city_et.setText("")
        if (mRequest.search_input.isNotEmpty()) {
            mRequest.search_input = null
            onRefresh()
        }
    }

    override fun onRefresh() {
        swipe_layout.isRefreshing = true
        page = Constant.DEFAULT_FIRST_PAGE
        presenter.getList(page, mRequest)
    }

    override fun onLoadMore() {
        page++
        presenter.getList(page, mRequest)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        val bundle = Bundle()
        bundle.putString(Constant.PARAM_ID, data.id)
        startActivity(TakeawayActivity::class.java, bundle)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val data = mAdapter.data[position]
        startActivity(RestaurantDetailActivity::class.java, Bundle().apply {
            putString(Constant.PARAM_ID, data.id)
        })
    }

    override fun bindList(data: MutableList<RestaurantResponse>, isLastPage: Boolean) {
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
            if (isLastPage) {
                loadMoreModule.loadMoreEnd()
            } else {
                loadMoreModule.loadMoreComplete()
            }
        }
    }

    /**
     * 区域筛选
     */
    private fun onAreaFilterClick() = switchFilterPager(0)

    /**
     * 美食类型筛选
     */
    private fun onFoodFilterClick() = switchFilterPager(1)

    /**
     * 其他筛选
     */
    private fun onOtherFilterClick() = switchFilterPager(2)

    private fun onFilterDismissClick() {
        handleFilterVisible(View.GONE)

        // 清空tab选中状态
        val childTabLL = category_ll.getChildAt(filter_pager.currentItem) as LinearLayout
        val tabText = childTabLL.getChildAt(0) as CheckedTextView
        val tabImage = childTabLL.getChildAt(1) as ImageView
        tabText.isChecked = false
        tabText.paint.isFakeBoldText = false
        tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
    }

    private fun switchFilterPager(index: Int) {
        val currentItem = filter_pager.currentItem
        if (filter_container_ll.visibility == View.GONE) {
            handleFilterVisible(View.VISIBLE)
            // 切换tab状态
            changeFilterTabStyle(currentItem, index)
        } else {
            if (currentItem == index) {
                handleFilterVisible(View.GONE)

                // 清空tab选中状态
                val childTabLL = category_ll.getChildAt(index) as LinearLayout
                val tabText = childTabLL.getChildAt(0) as CheckedTextView
                val tabImage = childTabLL.getChildAt(1) as ImageView
                tabText.isChecked = false
                tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
            }
        }
        filter_pager.setCurrentItem(index, true)
    }

    private fun handleFilterVisible(visibility: Int) {
        filter_container_ll.visibility = visibility
    }

    private fun changeFilterTabStyle(pagerPosition: Int, tabIndex: Int) {
        val childTabLL = category_ll.getChildAt(tabIndex) as LinearLayout
        val tabText = childTabLL.getChildAt(0) as CheckedTextView
        val tabImage = childTabLL.getChildAt(1) as ImageView
        if (pagerPosition == tabIndex) {
            tabText.isChecked = true
            tabText.paint.isFakeBoldText = true
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_blue)
        } else {
            tabText.isChecked = false
            tabImage.setImageResource(R.mipmap.ic_arrow_filter_black)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        val data = baseEvent.data
        if (data is AreaResponse) {
            // 区域筛选
            mRequest.trading_area_id = data.id
            val name = data.name
            if (!TextUtils.isEmpty(name)) {
                area_tv.text = name
            }
        } else if (data is Category) {
            // 美食列别筛选
            val id = data.id
            if (id == 0) {
                mRequest.category_id = null
            } else {
                mRequest.category_id = id.toString()
            }
            val name = data.name
            if (!TextUtils.isEmpty(name)) {
                food_tv.text = name
            }
        } else if (data is MoreFilter) {
            // 其他筛选
            val more = data
            mRequest.per_price = more.per_price
            mRequest.service = more.service
            mRequest.sort = more.sort
        }
        onRefresh()
        dismiss_view.performClick()
    }
}