package com.jcs.where.features.payment.tokenized

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.pay.PayCounterChannel
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2023/3/9 16:42.
 *
 */
class TokenizedAdapter : BaseQuickAdapter<PayCounterChannel, BaseViewHolder>(R.layout.item_pay_tokenized) {


    override fun convert(holder: BaseViewHolder, item: PayCounterChannel) {

        val channel_title_tv = holder.getView<TextView>(R.id.channel_title_tv)
        val icon_iv = holder.getView<ImageView>(R.id.icon_iv)

        channel_title_tv.text = item.title
        GlideUtil.load(context, item.icon, icon_iv)

    }
}