package com.jcs.where.features.store.pay

import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.store.PayChannel

/**
 * Created by Wangsw  2021/6/23 15:31.
 *
 */
class StorePayAdapter : BaseQuickAdapter<PayChannel, BaseViewHolder>(R.layout.item_store_pay_channel) {
    override fun convert(holder: BaseViewHolder, item: PayChannel) {

        val name_tv = holder.getView<CheckedTextView>(R.id.name_tv)
        val selected_iv = holder.getView<ImageView>(R.id.selected_iv)

        name_tv.text = item.title
        if (item.nativeSelected) {
            selected_iv.visibility = View.VISIBLE
        } else {
            selected_iv.visibility = View.GONE
        }

    }
}