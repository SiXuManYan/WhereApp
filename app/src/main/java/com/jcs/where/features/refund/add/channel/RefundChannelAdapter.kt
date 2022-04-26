package com.jcs.where.features.refund.add.channel

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundChannel

/**
 * Created by Wangsw  2022/4/25 19:22.
 *
 */
class RefundChannelAdapter : BaseQuickAdapter<RefundChannel, BaseViewHolder>(R.layout.item_refund_channel) {

    override fun convert(holder: BaseViewHolder, item: RefundChannel) {
        val channelTv = holder.getView<CheckedTextView>(R.id.channel_tv)
        channelTv.text = item.name

        if (item.isSelected) {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_blue, 0)
        } else {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_un_checked, 0)
        }


    }
}