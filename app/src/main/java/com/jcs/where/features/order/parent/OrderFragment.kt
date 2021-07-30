package com.jcs.where.features.order.parent

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.order.tab.OrderTabResponse
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseMvpFragment
import com.jcs.where.features.account.login.LoginActivity
import com.jcs.where.features.order.OrderChildFragment
import com.jcs.where.storage.entity.User
import kotlinx.android.synthetic.main.fragment_order_parent.*

/**
 * Created by Wangsw  2021/7/30 10:57.
 *
 */
class OrderFragment : BaseMvpFragment<OrderPresenter>(), OrderView {


    private var type: ArrayList<OrderTabResponse> = ArrayList()

    override fun getLayoutId() = R.layout.fragment_order_parent

    override fun initView(view: View) {
        BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.title_tv))

        if (User.isLogon()) {
            login_rl.visibility = View.GONE
        } else {
            login_rl.visibility = View.VISIBLE
        }

    }

    override fun needChangeStatusBarStatus() = true

    override fun initData() {
        presenter = OrderPresenter(this)
        presenter.getTabs()
    }

    override fun bindListener() {
        login_tv.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }
    }

    override fun bindTab(response: ArrayList<OrderTabResponse>, titles: ArrayList<String>) {
        type.addAll(response)


        viewpager.offscreenPageLimit = response.size
        val innerPagerAdapter = InnerPagerAdapter(childFragmentManager, 0)
        innerPagerAdapter.notifyDataSetChanged()
        viewpager.adapter = innerPagerAdapter


        tabs_type.setViewPager(viewpager, titles.toTypedArray())
    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        if (baseEvent.code == EventCode.EVENT_LOGIN_SUCCESS) {
            login_rl.visibility = View.GONE
            presenter.getTabs()
        }
    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = type[position].title

        override fun getItem(position: Int): Fragment = OrderChildFragment.getInstance(type[position].type)

        override fun getCount(): Int = type.size
    }


}