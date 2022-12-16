package com.jcs.where.features.job.record

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseFragment
import com.jcs.where.features.job.record.applied.AppliedFragment
import com.jcs.where.features.job.record.interviews.InterviewsFragment
import com.jcs.where.features.message.MessageCenterActivity
import kotlinx.android.synthetic.main.fragment_job_apply.*

/**
 * Created by Wangsw  2022/12/15 11:25.
 * 职位申请与面试邀约
 */
class JobRecordFragment : BaseFragment() {

    private val TAB_TITLES =
        arrayOf(StringUtils.getString(R.string.job_applied), StringUtils.getString(R.string.job_interviews))

    override fun getLayoutId() = R.layout.fragment_job_apply

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

                AppliedFragment()
            } else {
                InterviewsFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return super.getPageTitle(position)
        }
    }

}