package com.jcs.where.features.integral.record

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.integral.IntegralRecord
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2022/9/26 15:26.
 *
 */
class IntegralRecordAdapter : BaseQuickAdapter<IntegralRecord, BaseViewHolder>(R.layout.item_integral_record),LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: IntegralRecord) {
        val type_iv = holder.getView<ImageView>(R.id.type_iv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val points_tv = holder.getView<TextView>(R.id.points_tv)
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)



        when (item.nativeType) {
            1 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_phone)
                type_tv.text = context.getString(R.string.prepaid_reload)
            }
            2 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_water)
                type_tv.text = context.getString(R.string.water_utilities)
            }
            3 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_electricity)
                type_tv.text = context.getString(R.string.electric_utilities)
            }
            4 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_net)
                type_tv.text = context.getString(R.string.internet_billing)
            }
        }

        GlideUtil.load(context, item.image, image_iv)
        title_tv.text = item.title
        order_status_tv.text = item.order_status .toString()
        points_tv.text = StringUtils.getString(R.string.points_format,   item.price)
    }
}