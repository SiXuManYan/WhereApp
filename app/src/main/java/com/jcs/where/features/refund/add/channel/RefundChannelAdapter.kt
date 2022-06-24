package com.jcs.where.features.refund.add.channel

import android.view.View
import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundChannel

/**
 * Created by Wangsw  2022/4/25 19:22.
 *
 */
class RefundChannelAdapter : BaseMultiItemQuickAdapter<RefundChannel, BaseViewHolder>() {


    init {
        addItemType(RefundChannel.TYPE_NORMAL, R.layout.item_refund_channel)
        addItemType(RefundChannel.TYPE_TITLE, R.layout.item_refund_channel_title)
    }

    override fun convert(holder: BaseViewHolder, item: RefundChannel) {

        when (holder.itemViewType) {
            RefundChannel.TYPE_NORMAL -> {
                bindNormal(holder, item)
            }
            RefundChannel.TYPE_TITLE -> {
                bindTitle(holder, item)
            }

        }

    }

    private fun bindNormal(holder: BaseViewHolder, item: RefundChannel) {


        val line = holder.getView<View>(R.id.line_v)
        val channelTv = holder.getView<CheckedTextView>(R.id.channel_tv)
        channelTv.text = item.name

        if (item.isSelected) {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_blue, 0)
        } else {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_un_checked, 0)
        }
    }


    private fun bindTitle(holder: BaseViewHolder, item: RefundChannel) {
        holder.setText(R.id.title_tv, item.channel_category)
    }
}