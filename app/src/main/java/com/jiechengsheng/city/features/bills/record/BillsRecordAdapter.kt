package com.jiechengsheng.city.features.bills.record

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.bills.BillsRecord
import com.jiechengsheng.city.utils.BusinessUtils

/**
 * Created by Wangsw  2022/6/11 13:44.
 * 缴费记录
 */
class BillsRecordAdapter : BaseQuickAdapter<BillsRecord, BaseViewHolder>(R.layout.item_bills_record),LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: BillsRecord) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val billsStatusTv = holder.getView<TextView>(R.id.bills_status_tv)
        val money_tv = holder.getView<TextView>(R.id.money_tv)
        val time_tv = holder.getView<TextView>(R.id.time_tv)

        val bottom_ll = holder.getView<LinearLayout>(R.id.bottom_ll)


        when (item.order_type) {
            BillsRecord.TYPE_PHONE -> {
                image_iv.setImageResource(R.mipmap.ic_bills_record_phone)
                title_tv.text = context.getString(R.string.prepaid_reload)
            }
            BillsRecord.TYPE_WATER -> {
                image_iv.setImageResource(R.mipmap.ic_bills_record_water)
                title_tv.text = context.getString(R.string.water_utilities)
            }
            BillsRecord.TYPE_ELECTRICITY -> {
                image_iv.setImageResource(R.mipmap.ic_bills_record_electricity)
                title_tv.text = context.getString(R.string.electric_utilities)
            }
            BillsRecord.TYPE_NET -> {
                image_iv.setImageResource(R.mipmap.ic_bills_record_net)
                title_tv.text = context.getString(R.string.internet_billing)
            }
        }
        val orderStatus = item.order_status
        billsStatusTv.text = BusinessUtils.getBillsStatusText(orderStatus)
        money_tv.text = StringUtils.getString(R.string.price_unit_format, item.total_price.stripTrailingZeros().toPlainString())
        time_tv.text = item.created_at

        if (orderStatus == 3) {
            bottom_ll.visibility = View.VISIBLE
        } else {
            bottom_ll.visibility = View.GONE
        }

    }



}