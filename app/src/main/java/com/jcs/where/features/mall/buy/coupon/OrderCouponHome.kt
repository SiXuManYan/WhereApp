package com.jcs.where.features.mall.buy.coupon

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jcs.where.R
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.base.BaseEvent
import com.jcs.where.base.EventCode
import com.jcs.where.base.mvp.BaseBottomSheetDialogFragment
import com.jcs.where.base.mvp.FixedHeightBottomSheetDialog
import com.jcs.where.features.mall.buy.coupon.child.OrderCouponFragment
import kotlinx.android.synthetic.main.fragment_order_coupon_home.*

/**
 * Created by Wangsw  2022/3/9 14:38.
 *
 */
class OrderCouponHomeFragment : BaseBottomSheetDialogFragment<OrderCouponHomePresenter>(), OrderCouponHomeView {

    var alreadySelectedCouponId = 0
    var specsIdsJsonStr = ""
    var goodsJsonStr = ""


    val titles =
        arrayOf(StringUtils.getString(R.string.available), StringUtils.getString(R.string.unavailable))

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        FixedHeightBottomSheetDialog(requireContext(), theme, ScreenUtils.getScreenHeight() * 9 / 10)

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

            val apply = OrderCouponFragment().apply {
                type = position + 1
                specsIdsJsonStr = this@OrderCouponHomeFragment.specsIdsJsonStr
                goodsJsonStr = this@OrderCouponHomeFragment.goodsJsonStr
                selectedCouponId = alreadySelectedCouponId
            }
            return apply

        }

        override fun getCount(): Int = titles.size
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_COUPON_SELECTED -> {
                dismiss()
            }
            else -> {}
        }



    }

}


interface OrderCouponHomeView : BaseMvpView

class OrderCouponHomePresenter(private var view: OrderCouponHomeView) : BaseMvpPresenter(view)