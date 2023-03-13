package com.jiechengsheng.city.features.order.parent

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.BarUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.order.tab.OrderTabResponse
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseMvpFragment
import com.jiechengsheng.city.features.account.login.LoginActivity
import com.jiechengsheng.city.features.order.OrderChildFragment
import com.jiechengsheng.city.features.order.refund.RefundOrderFragment
import com.jiechengsheng.city.storage.entity.User
import kotlinx.android.synthetic.main.fragment_order_parent.*

/**
 * Created by Wangsw  2021/7/30 10:57.
 *
 */
class OrderFragment : BaseMvpFragment<OrderPresenter>(), OrderView {

    var needBack = false

    private var mType: ArrayList<OrderTabResponse> = ArrayList()

    override fun getLayoutId() = R.layout.fragment_order_parent

    override fun initView(view: View?) {

        view?.let {
            BarUtils.addMarginTopEqualStatusBarHeight(view.findViewById(R.id.title_rl))
        }

        login_rl.visibility = if (User.isLogon()) {
            View.GONE
        } else {
            View.VISIBLE
        }

        order_back_iv.visibility = if (needBack) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tabs_type.setSelectTextScale(1.0f)

        order_empty.apply {
            hideEmptyContainer()
        }


    }


    override fun initData() {
        presenter = OrderPresenter(this)
    }

    override fun loadOnVisible() {
        presenter.getTabs(true)
    }

    override fun bindListener() {
        login_tv.setOnClickListener {
            startActivity(LoginActivity::class.java)
        }

        order_back_iv.setOnClickListener {
            activity?.finish()
        }
    }

    override fun bindTab(response: ArrayList<OrderTabResponse>, titles: ArrayList<String>, isInit: Boolean) {
        order_empty.visibility = View.GONE
        mType.clear()
        mType.addAll(response)

        if (isInit) {
            viewpager.offscreenPageLimit = response.size
            val innerPagerAdapter = InnerPagerAdapter(childFragmentManager, 0)
            innerPagerAdapter.notifyDataSetChanged()
            viewpager.adapter = innerPagerAdapter
            tabs_type.setViewPager(viewpager, titles.toTypedArray())
        } else {
            tabs_type.mTitles.clear()
            tabs_type.mTitles.addAll(titles)
            tabs_type.notifyDataSetChanged()
        }

    }


    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_LOGIN_SUCCESS -> {
                login_rl.visibility = View.GONE
                val isInit = mType.isEmpty()

                presenter.getTabs(isInit)
            }
            EventCode.EVENT_SIGN_OUT -> {
                login_rl.visibility = View.VISIBLE
            }
            EventCode.EVENT_REFRESH_ORDER_LIST,
            EventCode.EVENT_ORDER_COMMIT_SUCCESS,
            -> {
                presenter.getTabs(false)
            }
            else -> {
            }
        }

    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = mType[position].title

        override fun getItem(position: Int): Fragment {


            val typeValue = mType[position].type

            return if (typeValue == 6) {
                // 商城售后
                RefundOrderFragment()
            } else {
                OrderChildFragment.getInstance(typeValue)
            }


        }

        override fun getCount(): Int = mType.size
    }

    override fun getTabError() {
        if (order_empty.visibility != View.VISIBLE) {
            order_empty.visibility = View.VISIBLE
            order_empty.showNetworkError {
                presenter.getTabs(true)
                order_empty.visibility = View.GONE
            }

        }
    }


}