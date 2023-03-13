package com.jiechengsheng.city.features.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.collection.city.CollectionFragment
import com.jiechengsheng.city.features.collection.good.CollectionGoodFragment
import com.jiechengsheng.city.features.collection.shop.CollectionShopFragment
import kotlinx.android.synthetic.main.actitiy_collection.*

/**
 * Created by Wangsw  2021/11/16 15:48.
 * 我的收藏
 */
class CollectionActivity : BaseMvpActivity<CollectionPresenter>(), CollectionView {

    val TAB_TITLES =
        arrayOf(
            StringUtils.getString(R.string.collection_tab_same_city),
            StringUtils.getString(R.string.collection_tab_article),
            StringUtils.getString(R.string.collection_tab_video),
            StringUtils.getString(R.string.collection_tab_good),
            StringUtils.getString(R.string.collection_tab_mall)
        )

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.actitiy_collection

    override fun initView() {
        pager.apply {
            offscreenPageLimit = TAB_TITLES.size
            adapter = InnerPagerAdapter(supportFragmentManager, 0)
        }
        tabs.setViewPager(pager)

    }

    override fun initData() = Unit

    override fun bindListener() = Unit

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = TAB_TITLES[position]

        override fun getItem(position: Int): Fragment {

            return when (position) {
                0, 1, 2 -> {
                    CollectionFragment().apply {
                        type = position + 1
                    }
                }
                3 -> {
                    CollectionGoodFragment()
                }
                else -> {
                    CollectionShopFragment()
                }
            }


        }

        override fun getCount(): Int = TAB_TITLES.size
    }

}