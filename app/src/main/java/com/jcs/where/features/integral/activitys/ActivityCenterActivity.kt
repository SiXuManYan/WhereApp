package com.jcs.where.features.integral.activitys

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.SizeUtils
import com.jcs.where.R
import com.jcs.where.api.response.integral.IntegralTag
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.integral.activitys.child.IntegralChildFragment
import com.jcs.where.features.integral.record.IntegralRecordActivity
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_activity_center.*

/**
 * Created by Wangsw  2022/9/21 10:10.
 * 活动中心
 */
class ActivityCenterActivity : BaseMvpActivity<ActivityCenterPresenter>(), ActivityCenterView {


    private var tabTitles = ArrayList<IntegralTag>()


    override fun isStatusDark() = true

    private lateinit var mThirdAdapter: CenterTagAdapter

    private lateinit var pagerAdapter: InnerPagerAdapter

    override fun getLayoutId() = R.layout.activity_activity_center


    override fun initView() {

        initFilter()
        pagerAdapter = InnerPagerAdapter(supportFragmentManager, 0)
    }


    private fun initFilter() {

        mThirdAdapter = CenterTagAdapter()
        tab_rv.apply {
            adapter = mThirdAdapter
            layoutManager = LinearLayoutManager(this@ActivityCenterActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(DividerDecoration(Color.TRANSPARENT, SizeUtils.dp2px(8f), 0, 0))
        }
    }


    override fun bindTab(response: ArrayList<IntegralTag>) {
        mThirdAdapter.setNewInstance(response)
        tabTitles.clear()
        tabTitles.addAll(response)
        pager.apply {
            offscreenPageLimit = response.size
            pagerAdapter.notifyDataSetChanged()
            adapter = pagerAdapter
        }
    }

    override fun initData() {
        presenter = ActivityCenterPresenter(this)
        presenter.getTab()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun bindListener() {
        mThirdAdapter.setOnItemClickListener { _, _, position ->
            pager.setCurrentItem(position, true)
            mThirdAdapter.data.forEachIndexed { index, category ->
                category.nativeIsSelected = index == position
            }
            mThirdAdapter.notifyDataSetChanged()


        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageSelected(position: Int) {
                mThirdAdapter.data.forEachIndexed { index, category ->
                    category.nativeIsSelected = index == position
                }
                mThirdAdapter.notifyDataSetChanged()
                tab_rv.smoothScrollToPosition(position)
            }
        })

        record_tv.setOnClickListener {
            startActivity(IntegralRecordActivity::class.java)
        }


    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {


        override fun getPageTitle(position: Int): CharSequence = tabTitles[position].title

        override fun getItem(position: Int): Fragment = IntegralChildFragment().apply { type = position }

        override fun getCount(): Int = tabTitles.size
    }

}