package com.jcs.where.features.coupon.center

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.Coupon
import com.jcs.where.utils.BigDecimalUtil
import java.math.BigDecimal

/**
 * Created by Wangsw  2022/3/5 14:52.
 *
 */
class CouponCenterAdapter : BaseQuickAdapter<Coupon, BaseViewHolder>(R.layout.item_coupon_center) {


    override fun convert(holder: BaseViewHolder, item: Coupon) {

        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }


        val right_ll = holder.getView<LinearLayout>(R.id.right_ll)

        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val name_tv = holder.getView<TextView>(R.id.name_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)


        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val get_tv = holder.getView<TextView>(R.id.get_tv)
        val threshold_tv = holder.getView<TextView>(R.id.threshold_tv)

        val rule_tv = holder.getView<TextView>(R.id.rule_tv)
        val schedule_pb = holder.getView<ProgressBar>(R.id.schedule_pb)
        val schedule_tv = holder.getView<TextView>(R.id.schedule_tv)


        name_tv.text = item.name
        time_tv.text = StringUtils.getString(R.string.hotel_date_format, item.start_time, item.end_time)
        price_tv.apply {
            text = StringUtils.getString(R.string.price_unit_format, item.money)

        }
        threshold_tv.text = item.doorsill


        // style
        if (item.couponType == 1) {
            type_tv.setText(R.string.coupon_type_platform)
        }

        when (item.coupon_residue_type) {
            2 -> {
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_orange)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_red_radius_2)
                get_tv.setText(R.string.get_now)
                get_tv.setBackgroundResource(R.drawable.shape_white_radius_16)
                get_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
                price_tv.setTextColor(ColorUtils.getColor(R.color.white))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.white))

                rule_tv.visibility = View.GONE
                schedule_pb.visibility = View.VISIBLE
                schedule_tv.visibility = View.VISIBLE

                val progress = BigDecimalUtil.mul( BigDecimalUtil.div(item.get_num , item.num), BigDecimal(100))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    schedule_pb.setProgress(progress.toInt(), true)
                } else {
                    schedule_pb.progress = progress.toInt()
                }
                schedule_tv.text = StringUtils.getString(R.string.take_progress, progress.toPlainString())

            }
            else -> {
                right_ll.background = ResourceUtils.getDrawable(R.mipmap.ic_coupon_right_pink)
                type_tv.background = ResourceUtils.getDrawable(R.drawable.shape_gray_radius_2)
                get_tv.setText(R.string.robbed)
                get_tv.setBackgroundResource(R.drawable.shape_grey_radius_16_b7b7b7)
                get_tv.setTextColor(ColorUtils.getColor(R.color.white))
                price_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
                threshold_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))

                rule_tv.visibility = View.VISIBLE
                schedule_pb.visibility = View.GONE
                schedule_tv.visibility = View.GONE
            }

        }


    }


}