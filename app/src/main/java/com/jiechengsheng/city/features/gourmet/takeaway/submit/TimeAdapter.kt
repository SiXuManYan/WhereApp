package com.jiechengsheng.city.features.gourmet.takeaway.submit

import android.widget.CheckedTextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.dish.DeliveryTimeRetouch
import com.jiechengsheng.city.utils.time.TimeUtil

/**
 * Created by Wangsw  2021/4/27 17:11.
 *
 */
class TimeAdapter : BaseQuickAdapter<DeliveryTimeRetouch, BaseViewHolder>(R.layout.item_address_time) {


    /** 配送费 */
    var delivery_cost: String = "0"


    override fun convert(holder: BaseViewHolder, item: DeliveryTimeRetouch) {

        val time_tv = holder.getView<CheckedTextView>(R.id.time_tv)
        val adapterPosition = holder.adapterPosition
        val time = if (adapterPosition == 0) {
            StringUtils.getString(R.string.send_out_now)
        } else {
            TimeUtil.getFormatTimeHM(item.time)
        }
        time_tv.text = StringUtils.getString(R.string.peso_time_format, time, delivery_cost)
        time_tv.isChecked = item.nativeSelected

    }
}