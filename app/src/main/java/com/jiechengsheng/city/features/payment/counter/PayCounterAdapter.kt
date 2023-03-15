package com.jiechengsheng.city.features.payment.counter

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.pay.PayCounterChannel
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2023/3/7 16:43.
 *
 */
class PayCounterAdapter : BaseQuickAdapter<PayCounterChannel, BaseViewHolder>(R.layout.item_pay_counter) {


    override fun convert(holder: BaseViewHolder, item: PayCounterChannel) {

        val channel_title_tv = holder.getView<TextView>(R.id.channel_title_tv)
        val is_bound_tv = holder.getView<TextView>(R.id.is_bound_tv)
        val view_balance_tv = holder.getView<TextView>(R.id.view_balance_tv)
        val to_bind_tv = holder.getView<TextView>(R.id.to_bind_tv)
        val support_token_fl = holder.getView<FrameLayout>(R.id.support_token_fl)
        val check_iv = holder.getView<ImageView>(R.id.check_iv)
        val icon_iv = holder.getView<ImageView>(R.id.icon_iv)

        channel_title_tv.text = item.title

        GlideUtil.load(context, item.icon, icon_iv)

        if (item.is_tokenized_pay) {

            // 支持绑定token免密支付
            support_token_fl.visibility = View.VISIBLE
            if (item.is_auth) {
                // 已绑定
                is_bound_tv.visibility = View.VISIBLE
                view_balance_tv.visibility = View.VISIBLE
                to_bind_tv.visibility = View.GONE
            } else {
                is_bound_tv.visibility = View.GONE
                view_balance_tv.visibility = View.GONE
                to_bind_tv.visibility = View.VISIBLE
            }

        } else {
            is_bound_tv.visibility = View.GONE
            support_token_fl.visibility = View.GONE
        }


        if (item.nativeIsSelected) {
            check_iv.setImageResource(R.mipmap.ic_checked_blue)
        } else {
            check_iv.setImageResource(R.mipmap.ic_un_checked)
        }
    }
}