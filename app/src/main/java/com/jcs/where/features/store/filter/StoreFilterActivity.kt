package com.jcs.where.features.store.filter

import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.filter.screen.ScreenFilterFragment
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_store_filer.*
import kotlinx.android.synthetic.main.layout_filter_store.*

/**
 * Created by Wangsw  2021/6/9 10:24.
 * 商城筛选
 */
class StoreFilterActivity : BaseMvpActivity<StoreFilterPresenter>(), StoreFilterView {

    /** 一级分类 id */
    private var mPid = 0

    override fun getLayoutId() = R.layout.activity_store_filer

    override fun initView() {


        intent.extras?.let {
            mPid = it.getInt(Constant.PARAM_PID, 0)
        }

        initFilter()
        initThirdCategory()

    }


    private fun initThirdCategory() {
        three_category_rv
    }

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


    }

    override fun initData() {
        presenter = StoreFilterPresenter(this)
    }

    override fun bindListener() {



    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        // 筛选内容
        val data = baseEvent.data

        when (data) {
            is Category -> {

            }
            is ScreenFilterFragment.ScreenMoreFilter -> {

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
                // 清空tab选中状态
                val childTabLL = category_ll.getChildAt(index) as LinearLayout
                val tabText = childTabLL.getChildAt(0) as CheckedTextView
                val tabImage = childTabLL.getChildAt(1) as ImageView
                tabText.isChecked = false
                tabImage.setImageResource(R.mipmap.ic_store_down)
            }
        }

        filter_pager.setCurrentItem(index, true)
    }


    private fun handleFilterVisible(visibility: Int) {
        filter_container_ll.visibility = visibility
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
        } else {
            tabText.isChecked = false
            tabImage.setImageResource(R.mipmap.ic_store_down)
        }


    }

}