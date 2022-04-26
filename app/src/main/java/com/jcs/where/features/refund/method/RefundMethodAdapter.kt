package com.jcs.where.features.refund.method

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundMethod
import com.jcs.where.utils.BusinessUtils

/**
 * Created by Wangsw  2022/4/25 16:34.
 *
 */
class RefundMethodAdapter :BaseQuickAdapter<RefundMethod, BaseViewHolder>(R.layout.item_refund_method)  {
    override fun convert(holder: BaseViewHolder, item: RefundMethod) {

        val address_name_tv = holder.getView<TextView>(R.id.address_name_tv)
        holder.setText(R.id.name_tv ,item.user_name)
        holder.setText(R.id.phone_tv ,item.account)

        val bankChannel = BusinessUtils.isBankChannel(item.channel_name)
        if (bankChannel) {
            address_name_tv.text = item.bank_all_name
        }else {
            address_name_tv.text = item.channel_name
        }

    }
}