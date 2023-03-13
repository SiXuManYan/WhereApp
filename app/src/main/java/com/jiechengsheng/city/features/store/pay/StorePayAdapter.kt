package com.jiechengsheng.city.features.store.pay

import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.store.PayChannel

/**
 * Created by Wangsw  2021/6/23 15:31.
 *
 */
class StorePayAdapter : BaseQuickAdapter<PayChannel, BaseViewHolder>(R.layout.item_store_pay_channel) {
    override fun convert(holder: BaseViewHolder, item: PayChannel) {

        val name_tv = holder.getView<CheckedTextView>(R.id.name_tv)
        val selected_iv = holder.getView<ImageView>(R.id.selected_iv)
        val container_rl = holder.getView<RelativeLayout>(R.id.container_rl)

        name_tv.text = item.title
        if (item.nativeSelected) {
            selected_iv.visibility = View.VISIBLE
            container_rl.setBackgroundResource(R.drawable.shape_white_radius_4_stroke_blue_1)
        } else {
            selected_iv.visibility = View.GONE
            container_rl.setBackgroundResource(R.drawable.shape_white_radius_4)
        }


    }


}