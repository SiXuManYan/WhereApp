package com.jcs.where.features.bills.order

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.order.bill.BillsField

/**
 * Created by Wangsw  2022/6/13 16:14.
 *
 */
class BillOrderDetailAdapter : BaseQuickAdapter<BillsField, BaseViewHolder>(R.layout.item_bill_channel) {


    override fun convert(holder: BaseViewHolder, item: BillsField) {
        holder.getView<ImageView>(R.id.arrow_iv).visibility = View.GONE
        holder.setText(R.id.title_tv, item.key)
        holder.setText(R.id.desc_tv, item.value)

    }


}