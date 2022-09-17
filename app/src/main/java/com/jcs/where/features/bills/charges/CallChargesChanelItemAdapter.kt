package com.jcs.where.features.bills.charges

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.bills.CallChargeChannelItem

/**
 * Created by Wangsw  2022/7/6 14:27.
 *
 */
class CallChargesChanelItemAdapter : BaseQuickAdapter<CallChargeChannelItem, BaseViewHolder>(R.layout.item_call_charges_money) {


    override fun convert(holder: BaseViewHolder, item: CallChargeChannelItem) {
        holder.setText(R.id.money_tv, item.Denomination)
        val check = holder.getView<ImageView>(R.id.check_iv)

        if (item.isChecked) {
            check.setImageResource(R.mipmap.ic_checked_blue)
        } else {
            check.setImageResource(R.mipmap.ic_un_checked)
        }

        val item_name_tv = holder.getView<TextView>(R.id.item_name_tv)
        val spanUtils = SpanUtils().append(item.TelcoTag)

        val discount = item.discount
        if (discount.isNotBlank()) {
            spanUtils.append(" $discount").setForegroundColor(ColorUtils.getColor(R.color.red_FF4934)).setFontSize(12, true)
        }
        item_name_tv.text = spanUtils.create()


    }

}