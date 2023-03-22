package com.jiechengsheng.city.features.job.collection

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_job_collection.*

/**
 * Created by Wangsw  2023/3/22 11:23.
 *
 */
class SavedFragment : BaseFragment() {

    val TAB_TITLES = arrayOf(StringUtils.getString(R.string.jobs), StringUtils.getString(R.string.company))


    override fun getLayoutId() = R.layout.fragment_job_collection
    override fun initView(view: View?) {
        pager.offscreenPageLimit = TAB_TITLES.size
        val adapter = InnerPagerAdapter(childFragmentManager, 0)
        pager.adapter = adapter;
        tabs_type.setViewPager(pager, TAB_TITLES);
    }

    override fun initData() = Unit

    override fun bindListener() {
        back_iv.setOnClickListener {
            activity?.finish()
        }
    }

    private class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {

            return if (position == 0) {
                JobCollectionFragment()
            } else {
                CompanyCollectionFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

}