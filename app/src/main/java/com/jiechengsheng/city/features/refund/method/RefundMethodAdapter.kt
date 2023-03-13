package com.jiechengsheng.city.features.refund.method

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.RefundMethod

/**
 * Created by Wangsw  2022/4/25 16:34.
 *
 */
class RefundMethodAdapter :BaseQuickAdapter<RefundMethod, BaseViewHolder>(R.layout.item_refund_method)  {
    override fun convert(holder: BaseViewHolder, item: RefundMethod) {

        val address_name_tv = holder.getView<TextView>(R.id.address_name_tv)
        holder.setText(R.id.name_tv ,item.user_name)
        holder.setText(R.id.phone_tv ,item.account)
        address_name_tv.text = item.name

    }
}