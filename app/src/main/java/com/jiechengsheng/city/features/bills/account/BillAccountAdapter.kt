package com.jiechengsheng.city.features.bills.account

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.bills.BillAccount

/**
 * Created by Wangsw  2022/12/9 13:59.
 * 缴费账号
 */
class BillAccountAdapter : BaseQuickAdapter<BillAccount, BaseViewHolder>(R.layout.item_bill_account) {


    override fun convert(holder: BaseViewHolder, item: BillAccount) {
        holder.setText(R.id.first_field_tv, item.first_field)
        holder.setText(R.id.second_field_tv, item.second_field)

        val defaultTv = holder.getView<TextView>(R.id.default_tv)
        if (item.status == 1) {
            defaultTv.visibility = View.VISIBLE
        } else {
            defaultTv.visibility = View.GONE
        }


    }

}