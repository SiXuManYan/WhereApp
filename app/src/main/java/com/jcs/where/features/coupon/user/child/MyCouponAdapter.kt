package com.jcs.where.features.coupon.user.child

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.UserCoupon
import com.jcs.where.utils.BusinessUtils

/**
 * Created by Wangsw  2022/3/3 14:49.
 * 用户券包
 */
class MyCouponAdapter : BaseMultiItemQuickAdapter<UserCoupon, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(UserCoupon.TYPE_COMMON, R.layout.item_coupon_user)
        addItemType(UserCoupon.TYPE_TITLE, R.layout.item_shop_coupon_title)
    }

    override fun convert(holder: BaseViewHolder, item: UserCoupon) {

        when (holder.itemViewType) {
            UserCoupon.TYPE_COMMON -> {
                bindCoupon(holder, item)
            }
            UserCoupon.TYPE_TITLE -> {
                bindTitle(holder, item)
            }
        }
    }

    private fun bindCoupon(holder: BaseViewHolder, item: UserCoupon) {

        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }

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
        BusinessUtils.setFormatText(price_tv, StringUtils.getString(R.string.price_unit), item.money, 14, 24)

        threshold_tv.text = item.doorsill

        // style
        when (item.couponType) {
            1 -> type_tv.setText(R.string.coupon_type_platform)
            2 -> type_tv.setText(R.string.coupon_type_business)
            3 -> {
                // 2水 3电 4网 5手机充值
                when (item.bill_type) {
                    2 -> type_tv.setText(R.string.water_utilities_coupon)
                    3 -> type_tv.setText(R.string.electricity_utilities_coupon)
                    4 -> type_tv.setText(R.string.internet_billing_coupon)
                    5 -> type_tv.setText(R.string.telecoms_coupon)
                }


            }
        }

        when (item.nativeType) {
            1 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_white)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_blue)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_blue_radius_2)
                use_tv.visibility = View.VISIBLE
                price_tv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
            }
            2 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_used)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_gray)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                use_tv.visibility = View.GONE
                price_tv.setTextColor(ColorUtils.getColor(R.color.white))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.white))
            }
            3 -> {
                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_expired)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_gray)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                use_tv.visibility = View.GONE
                price_tv.setTextColor(ColorUtils.getColor(R.color.white))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.white))
            }
        }
    }


    private fun bindTitle(holder: BaseViewHolder, item: UserCoupon) {

        val shopTitleTv = holder.getView<TextView>(R.id.shop_title_tv)

        val container = holder.getView<View>(R.id.shop_title_tv)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
        shopTitleTv.text = item.shop_name
    }
}