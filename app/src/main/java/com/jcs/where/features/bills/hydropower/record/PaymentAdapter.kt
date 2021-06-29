package com.jcs.where.features.bills.hydropower.record

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.hydropower.PaymentRecord

/**
 * Created by Wangsw  2021/6/29 9:48.
 *
 */
class PaymentAdapter : BaseQuickAdapter<PaymentRecord, BaseViewHolder>(R.layout.item_payment_record), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: PaymentRecord) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val status_tv = holder.getView<TextView>(R.id.status_tv)
        val account_number_tv = holder.getView<TextView>(R.id.account_number_tv)
        val date_tv = holder.getView<TextView>(R.id.date_tv)

        if (item.bill_type == 1) {
            image_iv.setImageResource(R.mipmap.ic_bills_record_water)
        } else {
            image_iv.setImageResource(R.mipmap.ic_bills_record_electricity)
        }

        price_tv.text = context.getString(R.string.price_unit_format, item.price.toPlainString())
        status_tv.text = getStatusText(item.order_status)
        account_number_tv.text = item.account_number
        date_tv.text = item.created_at
    }

    fun getStatusText(orderStatus: Int): String {


        return when (orderStatus) {
            1 -> {
                context.getString(R.string.bills_status_1)
            }
            2 -> {
                context.getString(R.string.bills_status_2)
            }
            3 -> {
                context.getString(R.string.bills_status_3)
            }
            4 -> {
                context.getString(R.string.bills_status_4)
            }
            5 -> {
                context.getString(R.string.bills_status_5)
            }
            6 -> {
                context.getString(R.string.bills_status_6)
            }
            else -> ""
        }


    }

}