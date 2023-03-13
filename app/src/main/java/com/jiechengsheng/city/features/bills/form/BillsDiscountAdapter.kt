package com.jiechengsheng.city.features.bills.form

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R

/**
 * Created by Wangsw  2022/9/16 17:32.
 * 折扣
 */
class BillsDiscountAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_bill_discount) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.content_desc_tv, item)
    }
}