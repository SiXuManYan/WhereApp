package com.jcs.where.features.refund.method

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundMethod

/**
 * Created by Wangsw  2022/4/25 16:34.
 *
 */
class RefundMethodAdapter :BaseQuickAdapter<RefundMethod, BaseViewHolder>(R.layout.item_refund_method)  {
    override fun convert(holder: BaseViewHolder, item: RefundMethod) {

        holder.setText(R.id.address_name_tv ,item.bank_name)
        holder.setText(R.id.name_tv ,item.user_name)
        holder.setText(R.id.phone_tv ,item.account)
    }
}