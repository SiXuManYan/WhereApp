package com.jcs.where.features.store.order.detail

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.order.OrderStoreGoods
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/28 14:20.
 *
 */
class StoreOrderDetailAdapter :BaseQuickAdapter<OrderStoreGoods,BaseViewHolder>(R.layout.layout_child_store_commit) {

    override fun convert(holder: BaseViewHolder, item: OrderStoreGoods) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val good_count_tv = holder.getView<TextView>(R.id.good_count_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)

        if (item.good_image.isNotEmpty()) {
            GlideUtil.load(context,item.good_image[0],image_iv)
        }

        good_name_tv.text = item.good_title
        good_count_tv.text = context.getString(R.string.count_format,item.good_num)
        price_tv.text = context.getString(R.string.price_unit_format,item.price)
    }
}