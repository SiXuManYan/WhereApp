package com.jcs.where.features.mall.order

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallOrderGood
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/12/16 16:23.
 *
 */
class MallOrderDetailAdapter : BaseQuickAdapter<MallOrderGood, BaseViewHolder>(R.layout.item_dishes_for_order_submit_mall) {
    override fun convert(holder: BaseViewHolder, item: MallOrderGood) {
        val image_iv = holder.getView<ImageView>(R.id.order_image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val good_count_tv = holder.getView<TextView>(R.id.count_tv)
        val attr_tv = holder.getView<TextView>(R.id.attr_tv)
        val price_tv = holder.getView<TextView>(R.id.now_price_tv)

        GlideUtil.load(context, item.good_image, image_iv, 4)
        good_name_tv.text = item.good_title
        good_count_tv.text = context.getString(R.string.count_format, item.good_num)
        price_tv.text = context.getString(R.string.price_unit_format, item.good_price.toString())


        val buffer = StringBuffer()
        item.good_specs.forEach {
            buffer.append(it.value + " ")
        }
        attr_tv.text = buffer

    }
}