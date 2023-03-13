package com.jiechengsheng.city.features.mall.buy.coupon

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.StringUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.base.BaseEvent
import com.jiechengsheng.city.base.EventCode
import com.jiechengsheng.city.base.mvp.BaseBottomSheetDialogFragment
import com.jiechengsheng.city.base.mvp.FixedHeightBottomSheetDialog
import com.jiechengsheng.city.features.mall.buy.coupon.child.OrderCouponFragment
import kotlinx.android.synthetic.main.fragment_order_coupon_home.*

/**
 * Created by Wangsw  2022/3/9 14:38.
 *
 */
class OrderCouponHomeFragment : BaseBottomSheetDialogFragment<OrderCouponHomePresenter>(), OrderCouponHomeView {

    var alreadySelectedCouponId: Int? = 0


    /** 所有商品(可用平台券) */
    var goodsJsonStr: String? = null


    /** 具体店铺商品(店铺券) */
    var shopGoodsJson: String? = null

    /** 店铺id (店铺券)*/
    var shopId: Int? = null


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
                listType = position + 1
                selectedCouponId = alreadySelectedCouponId

                // 获取平台券时使用
                goodsJsonStr = this@OrderCouponHomeFragment.goodsJsonStr

                // 获取店铺券时使用
                shopGoodsJson = this@OrderCouponHomeFragment.shopGoodsJson
                shopId = this@OrderCouponHomeFragment.shopId

            }
            return apply

        }

        override fun getCount(): Int = titles.size
    }

    override fun onEventReceived(baseEvent: BaseEvent<*>) {
        super.onEventReceived(baseEvent)
        when (baseEvent.code) {
            EventCode.EVENT_SELECTED_PLATFORM_COUPON,
            EventCode.EVENT_SELECTED_SHOP_COUPON, -> {
                dismiss()
            }
            else -> {}
        }


    }

}


interface OrderCouponHomeView : BaseMvpView

class OrderCouponHomePresenter(private var view: OrderCouponHomeView) : BaseMvpPresenter(view)