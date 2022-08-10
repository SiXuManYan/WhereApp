package com.jcs.where.features.mall.home

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallCategory
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.cart.MallCartActivity
import com.jcs.where.features.mall.home.child.MallHomeChildFragment
import com.jcs.where.features.search.SearchAllActivity
import com.jcs.where.utils.AnimationUtils
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_mall_home.*


/**
 * Created by Wangsw  2021/11/30 16:22.
 * 商城首页
 */
class MallHomeActivity : BaseMvpActivity<MallHomePresenter>(), MallHomeView {

    private lateinit var mAdapter: TopCategoryAdapter

    private var firstCategory: ArrayList<MallCategory> = ArrayList()

    override fun getLayoutId() = R.layout.activity_mall_home

    override fun isStatusDark() = true

    override fun initView() {
        initTop()
    }

    private fun initTop() {

        mAdapter = TopCategoryAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val selectCategory = mAdapter.data[position]

                // 更新标题
                pager_vp.currentItem = position
                showFilter(false)
            }
        }

        top_category_rv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

    }

    private fun showFilter(show: Boolean) {
        if (show) {
            top_category_ll.visibility = View.VISIBLE
            all_iv.setImageResource(R.mipmap.ic_up_black)
        } else {
            top_category_ll.visibility = View.GONE
            all_iv.setImageResource(R.mipmap.ic_down_black)

        }
    }


    override fun initData() {
        presenter = MallHomePresenter(this)
        presenter.getFirstCategory(true)
    }

    override fun bindListener() {

        cart_iv.setOnClickListener {
            startActivityAfterLogin(MallCartActivity::class.java)
        }

        search_ll.setOnClickListener {
            startActivity(SearchAllActivity::class.java, Bundle().apply {
                putInt(Constant.PARAM_TYPE, 6)
            })
        }
        all_iv.setOnClickListener {
            showFilter(top_category_ll.visibility != View.VISIBLE)
        }
        dismiss_view.setOnClickListener {
            showFilter(false)
        }

    }


    override fun bindCategory(response: ArrayList<MallCategory>, titles: ArrayList<String>) {

        if (response.isEmpty()) {
            return
        }
        firstCategory.clear()
        firstCategory.addAll(response)


        pager_vp.offscreenPageLimit = response.size
        pager_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })

        val innerPagerAdapter = InnerPagerAdapter(supportFragmentManager, 0)
        innerPagerAdapter.notifyDataSetChanged()
        pager_vp.adapter = innerPagerAdapter

        tabs_stl.setViewPager(pager_vp, titles.toTypedArray())

        mAdapter.setNewInstance(firstCategory)
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = firstCategory[position].name

        override fun getItem(position: Int): Fragment = MallHomeChildFragment().apply {
            targetFirstCategory = firstCategory[position]
        }

        override fun getCount(): Int = firstCategory.size
    }
}