package com.jcs.where.features.mall.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jcs.where.R
import com.jcs.where.api.response.category.Category
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.mall.home.child.MallHomeChildFragment
import kotlinx.android.synthetic.main.activity_mall_home.*

/**
 * Created by Wangsw  2021/11/30 16:22.
 * 商城首页
 */
class MallHomeActivity : BaseMvpActivity<MallHomePresenter>(), MallHomeView {

    private var firstCategory: ArrayList<Category> = ArrayList()

    override fun getLayoutId() = R.layout.activity_mall_home

    override fun initView() {

    }

    override fun initData() {
        presenter = MallHomePresenter(this)
    }

    override fun bindListener() {
        cart_iv.setOnClickListener {
            // todo 新版商城购物车
        }

        search_ll.setOnClickListener {
            // TODO 新版商城筛选
        }

    }

    override fun bindCategory(response: ArrayList<Category>, titles: ArrayList<String>) {

        firstCategory.addAll(response)
        val innerPagerAdapter = InnerPagerAdapter(supportFragmentManager, 0).apply {
            notifyDataSetChanged()
        }
        pager_vp.apply {
            offscreenPageLimit = response.size
            adapter = innerPagerAdapter
        }
        tabs_type.setViewPager(pager_vp)
    }


    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = firstCategory[position].name

        override fun getItem(position: Int): Fragment = MallHomeChildFragment().apply {
            targetFirstCategory = firstCategory[position]
        }

        override fun getCount(): Int = firstCategory.size
    }
}