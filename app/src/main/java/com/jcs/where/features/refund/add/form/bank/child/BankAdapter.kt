package com.jcs.where.features.refund.add.form.bank.child

import android.widget.CheckedTextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.RefundBankSelected

/**
 * Created by Wangsw  2022/4/25 19:22.
 * 选择银行
 */
@Deprecated("新版绑定退款渠道不必区分银行和第三方")
class BankAdapter : BaseQuickAdapter<RefundBankSelected, BaseViewHolder>(R.layout.item_refund_channel) {

    override fun convert(holder: BaseViewHolder, item: RefundBankSelected) {

        val channelTv = holder.getView<CheckedTextView>(R.id.channel_tv)
        channelTv.text = item.all

        if (item.isSelected) {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_blue, 0)
        } else {
            channelTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }



    }
}