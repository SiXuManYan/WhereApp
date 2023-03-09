package com.jcs.where.features.payment.counter

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.pay.PayCounter
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2023/3/7 16:43.
 *
 */
class PayCounterAdapter : BaseQuickAdapter<PayCounter, BaseViewHolder>(R.layout.item_pay_counter) {


    override fun convert(holder: BaseViewHolder, item: PayCounter) {

        val is_bound_tv = holder.getView<TextView>(R.id.is_bound_tv)
        val view_balance_tv = holder.getView<TextView>(R.id.view_balance_tv)
        val to_bind_tv = holder.getView<TextView>(R.id.to_bind_tv)
        val support_token_fl = holder.getView<FrameLayout>(R.id.support_token_fl)
        val check_iv = holder.getView<ImageView>(R.id.check_iv)
        val icon_iv = holder.getView<ImageView>(R.id.icon_iv)


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


        view_balance_tv.setOnClickListener {

        }

        to_bind_tv.setOnClickListener {

        }


    }
}