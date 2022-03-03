package com.jcs.where.features.coupon.user.child

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.UserCoupon

/**
 * Created by Wangsw  2022/3/3 14:49.
 * 用户券包
 */
class MyCouponAdapter : BaseQuickAdapter<UserCoupon, BaseViewHolder>(R.layout.item_coupon_user),LoadMoreModule {



    override fun convert(holder: BaseViewHolder, item: UserCoupon) {

        val left_ll = holder.getView<LinearLayout>(R.id.left_ll)
        val right_ll = holder.getView<LinearLayout>(R.id.right_ll)

        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)


        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val use_tv = holder.getView<TextView>(R.id.use_tv)


        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)



        name_tv.text = item.name
        time_tv.text = StringUtils.getString(R.string.hotel_date_format, item.start_time, item.end_time)
        price_tv.apply {
            text = StringUtils.getString(R.string.price_unit_format, item.money)

        }
        threshold_tv.text = item.doorsill

        // style

        when (item.nativeType) {
            1 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_white)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_blue)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_blue_radius_2)
                use_tv.visibility = View.VISIBLE
            }
            2 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_used)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_gray)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                use_tv.visibility = View.GONE
            }
            3 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_expired)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_gray)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                use_tv.visibility = View.GONE
            }

        }

    }
}