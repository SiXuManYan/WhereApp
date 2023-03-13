package com.jiechengsheng.city.features.mall.buy.coupon.child

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.UserCoupon
import com.jiechengsheng.city.utils.BusinessUtils

/**
 * Created by Wangsw  2022/3/9 15:09.
 * 提交订单优惠券
 */
class OrderCouponAdapter : BaseQuickAdapter<UserCoupon, BaseViewHolder>(R.layout.item_coupon_order_select) {

    /** 1 可用优惠券 2不可用 */
    var listType = 1


    override fun convert(holder: BaseViewHolder, item: UserCoupon) {

        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if ((holder.adapterPosition - headerLayoutCount) == 0) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }

        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)
        val checked_ck = holder.getView<ImageView>(R.id.checked_ck)


        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)

        name_tv.text = item.name
        time_tv.text = StringUtils.getString(R.string.hotel_date_format, item.start_time, item.end_time)

        BusinessUtils.setFormatText(price_tv, StringUtils.getString(R.string.price_unit), item.money, 14, 24)

        if (listType == 1) {
            checked_ck.visibility = View.VISIBLE

            if (item.nativeSelected) {
                checked_ck.setImageResource(R.mipmap.ic_checked_blue)
            } else {
                checked_ck.setImageResource(R.mipmap.ic_un_checked)
            }

        } else {
            checked_ck.visibility = View.GONE
        }

        threshold_tv.text = item.doorsill

        // style
        // 券类型
        when (item.couponType) {
            1 -> type_tv.setText(R.string.coupon_type_platform)
            2 -> type_tv.setText(R.string.coupon_type_business)
        }

    }


}