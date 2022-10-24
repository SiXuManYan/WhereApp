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
class IntegralRecordAdapter : BaseQuickAdapter<IntegralRecord, BaseViewHolder>(R.layout.item_integral_record), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: IntegralRecord) {
        val type_iv = holder.getView<ImageView>(R.id.type_iv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val type_tv = holder.getView<TextView>(R.id.type_tv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val points_tv = holder.getView<TextView>(R.id.points_tv)
        val order_status_tv = holder.getView<TextView>(R.id.order_status_tv)


        /**
         * 1商品 2水 3电 4网 5手机充值
         */
        when (item.type) {
            1 -> {
                type_iv.setImageResource(R.mipmap.ic_order_store)
                type_tv.text = context.getString(R.string.exchange_good)
            }
            2 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_water)
                type_tv.text = context.getString(R.string.water_utilities_coupon)
            }
            3 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_electricity)
                type_tv.text = context.getString(R.string.electricity_utilities_coupon)
            }
            4 -> {
                type_iv.setImageResource(R.mipmap.ic_bills_record_net)
                type_tv.text = context.getString(R.string.internet_billing_coupon)
            }
            5->{
                type_iv.setImageResource(R.mipmap.ic_bills_record_phone)
                type_tv.text = context.getString(R.string.telecoms_coupon)
            }
        }

        GlideUtil.load(context, item.image, image_iv)
        title_tv.text = item.title

        points_tv.text = StringUtils.getString(R.string.points_format, item.price)

        when (item.order_status) {
            1 -> {
                order_status_tv.text = StringUtils.getString(R.string.store_status_3)

            }
            2 -> {
                order_status_tv.text = StringUtils.getString(R.string.store_status_5)
            }
            else -> {}
        }
    }
}