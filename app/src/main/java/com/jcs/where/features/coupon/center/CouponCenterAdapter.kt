package com.jcs.where.features.coupon.center

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.*
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.Coupon
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.BusinessUtils
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/3/5 14:52.
 * 领券中心、店铺优惠券
 */
class CouponCenterAdapter : BaseMultiItemQuickAdapter<Coupon, BaseViewHolder>() ,LoadMoreModule{

    init {
        addItemType(Coupon.TYPE_COMMON, R.layout.item_coupon_center)
        addItemType(Coupon.TYPE_FOR_SHOP_HOME_PAGE, R.layout.item_coupon_center_for_shop_home)
    }


    override fun convert(holder: BaseViewHolder, item: Coupon) {


        when (holder.itemViewType) {

            Coupon.TYPE_COMMON -> {
                addTopMargin(holder)
                bindLeft(holder, item)
                bindRight(holder, item)
                setCommonStyle(holder, item)
            }

            Coupon.TYPE_FOR_SHOP_HOME_PAGE -> {
                resetWith(holder)
                bindLeft(holder, item)
                bindRight(holder, item)
                setShopStyle(holder, item)
            }

        }


    }


    private fun addTopMargin(holder: BaseViewHolder) {
        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
    }


    private fun resetWith(holder: BaseViewHolder) {
        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.width = ScreenUtils.getScreenWidth() * 4 / 5

    }

    private fun bindLeft(holder: BaseViewHolder, item: Coupon) {
        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)

        val schedule_pb = holder.getView<ProgressBar>(R.id.schedule_pb)
        val schedule_tv = holder.getView<TextView>(R.id.schedule_tv)
        val rule_tv = holder.getViewOrNull<TextView>(R.id.rule_tv)

        // 券类型
        when (item.couponType) {
            1 -> type_tv.setText(R.string.coupon_type_platform)
            2 -> type_tv.setText(R.string.coupon_type_business)
        }

        // 名称
        name_tv.text = item.name

        // 有效期
        val time_tv = holder.getView<TextView>(R.id.time_tv)
        time_tv.text = StringUtils.getString(R.string.hotel_date_format, item.start_time, item.end_time)

        // 领取进度、使用规则
        if (item.coupon_residue_type == 2) {

            rule_tv?.visibility = View.GONE
            schedule_pb.visibility = View.VISIBLE
            schedule_tv.visibility = View.VISIBLE

            val progress = BigDecimalUtil.mul(BigDecimalUtil.div(item.get_num, item.num), BigDecimal(100))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                schedule_pb.setProgress(progress.toInt(), true)
            } else {
                schedule_pb.progress = progress.toInt()
            }
            schedule_tv.text = StringUtils.getString(R.string.take_progress, progress.toPlainString())
        } else {
            rule_tv?.visibility = View.VISIBLE
            schedule_pb.visibility = View.GONE
            schedule_tv.visibility = View.GONE
        }


    }


    private fun bindRight(holder: BaseViewHolder, item: Coupon) {
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)

        // 面值
        BusinessUtils.setFormatText(price_tv, StringUtils.getString(R.string.price_unit), item.money, 14, 24)

        // 领取限制
        threshold_tv.text = item.doorsill
    }


    private fun setCommonStyle(holder: BaseViewHolder, item: Coupon) {

        val right_ll = holder.getView<LinearLayout>(R.id.right_ll)

        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val get_tv = holder.getView<TextView>(R.id.get_tv)
        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)


        // style
        when (item.coupon_residue_type) {

            2 -> {
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_orange)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_red_radius_2)
                get_tv.setText(R.string.get_now)
                get_tv.setBackgroundResource(R.drawable.shape_white_radius_16)
                get_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
                price_tv.setTextColor(ColorUtils.getColor(R.color.white))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.white))
            }
            1 -> {

                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_pink)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                get_tv.setText(R.string.robbed)
                get_tv.setBackgroundResource(R.drawable.shape_grey_radius_16_b7b7b7)
                get_tv.setTextColor(ColorUtils.getColor(R.color.white))
                price_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            }

        }
    }

    private fun setShopStyle(holder: BaseViewHolder, item: Coupon) {

        val left_ll = holder.getView<LinearLayout>(R.id.left_ll)
        val right_ll = holder.getView<LinearLayout>(R.id.right_ll)

        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val get_tv = holder.getView<TextView>(R.id.get_tv)
        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)


        // style
        when (item.coupon_residue_type) {

            2 -> {

                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_pink_small)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_orange_small)

                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_red_radius_2)
                get_tv.setText(R.string.get_now)
                get_tv.setBackgroundResource(R.drawable.shape_white_radius_16)
                get_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
                price_tv.setTextColor(ColorUtils.getColor(R.color.white))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.white))
            }
            1 -> {

                left_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_left_pink_small_expired)
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_pink_small)

                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                get_tv.setText(R.string.robbed)
                get_tv.setBackgroundResource(R.drawable.shape_grey_radius_16_b7b7b7)
                get_tv.setTextColor(ColorUtils.getColor(R.color.white))
                price_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            }

        }
    }


}