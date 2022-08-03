package com.jcs.where.features.bills.place

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.bills.FieldDetail

/**
 * Created by Wangsw  2022/6/9 15:33.
 *
 */
class BillsPlaceOrderAdapter : BaseQuickAdapter<FieldDetail, BaseViewHolder>(R.layout.item_bills_place_order) {
    override fun convert(holder: BaseViewHolder, item: FieldDetail) {
        holder.setText(R.id.title_tv, item.Tag)
        holder.setText(R.id.content_et, item.nativeUserInput)
    }
}