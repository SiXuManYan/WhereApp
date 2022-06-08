package com.jcs.where.features.bills.channel

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.bills.BillsChannel

/**
 * Created by Wangsw  2022/6/8 15:28.
 *
 */
class BillsChannelAdapter:BaseQuickAdapter<BillsChannel,BaseViewHolder>(R.layout.item_bill_channel) {

    override fun convert(holder: BaseViewHolder, item: BillsChannel) {
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val desc_tv = holder.getView<TextView>(R.id.desc_tv)

        title_tv.text = item.BillerTag
        desc_tv.text = item.Description




    }
}