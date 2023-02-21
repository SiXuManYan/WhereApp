package com.jcs.where.features.bills.place.coupon

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.features.mall.buy.coupon.OrderCouponHomePresenter
import com.jcs.where.features.mall.buy.coupon.OrderCouponHomeView
import kotlinx.android.synthetic.main.fragment_order_coupon_home.*

/**
 * Created by Wangsw  2022/10/12 14:38.
 *
 */
class BillCouponHome : BaseBottomSheetDialogFragment<OrderCouponHomePresenter>(), OrderCouponHomeView {

    /** 1-话费，2-水费，3-电费，4-网费 */
    var module = 0

    /** 金额 */
    var price = ""

    /** 账号 */
    var account = ""

    /** 已选择购物券id */
    var alreadySelectedCouponId = 0

    val titles =
        arrayOf(StringUtils.getString(R.string.available), StringUtils.getString(R.string.unavailable))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 8/ 10)

    override fun getLayoutId() = R.layout.fragment_order_coupon_home

    override fun initView(parent: View) {
        pager.apply {
            offscreenPageLimit = titles.size
            adapter = InnerPagerAdapter(childFragmentManager, 0)
        }
        tabs.setViewPager(pager)
    }

    override fun initData() {
        presenter = OrderCouponHomePresenter(this)
    }

    override fun bindListener() {
        close_iv.setOnClickListener {
            dismiss()
        }
    }

    private inner class InnerPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getPageTitle(position: Int): CharSequence = titles[position]

        override fun getItem(position: Int): Fragment {

            val apply = BillCouponChild().apply {
                listType = position + 1
                selectedCouponId = alreadySelectedCouponId
                module = this@BillCouponHome.module
                price = this@BillCouponHome.price
                account = this@BillCouponHome.account
            }
            return apply

        }

        override fun getCount(): Int = titles.size
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SELECTED_PLATFORM_COUPON -> dismiss()
            else -> {}
        }


    }

}
