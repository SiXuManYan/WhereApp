package com.jiechengsheng.city.features.job.record

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.job.Job
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.BaseFragment
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.fragment_job_apply.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Wangsw  2022/12/15 11:25.
 * 职位申请与面试邀约
 */
class JobRecordFragment : BaseFragment() {

    val TAB_TITLES = arrayOf(StringUtils.getString(R.string.job_applied), StringUtils.getString(R.string.job_interviews))

    private var isFromNotice = false

    override fun getLayoutId() = R.layout.fragment_job_apply

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventBus = EventBus.getDefault()
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        val eventBus = EventBus.getDefault()
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this)
        }
    }

    override fun initView(view: View?) {
        arguments?.let {
            isFromNotice = it.getBoolean(Constant.PARAM_FROM_NOTICE, false)
        }

        pager.offscreenPageLimit = TAB_TITLES.size
        val adapter = InnerPagerAdapter(childFragmentManager, 0)
        pager.adapter = adapter;
        tabs_type.setViewPager(pager, TAB_TITLES);
    }

    override fun initData() {
        if (isFromNotice) {
            tabs_type.currentTab = 1
        }
    }

    override fun bindListener() {
        back_iv.setOnClickListener {
            activity?.finish()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventReceived(baseEvent: BaseEvent<*>) {
        when (baseEvent.code) {
            EventCode.EVENT_NAVIGATION_TO_JOB_INTERVIEWS -> {
                tabs_type.currentTab = 1
            }
            else -> {}
        }
    }


    private class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {

            var type = 0
            if (position == 0) type = Job.REQUEST_APPLIED
            if (position == 1) type = Job.REQUEST_INTERVIEWS

            val apply = Bundle().apply {
                putInt(Constant.PARAM_TYPE, type)
            }
            val fragment = JobRecordChildFragment()
            fragment.arguments = apply
            return fragment
        }

        override fun getCount(): Int {
            return 2
        }
    }

}