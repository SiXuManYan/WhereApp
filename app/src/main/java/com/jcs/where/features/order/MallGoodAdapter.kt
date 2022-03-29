package com.jcs.where.features.order

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.order.OrderMallGoods
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2022/3/29 16:33.
 *
 */
class MallGoodAdapter : BaseQuickAdapter<OrderMallGoods, BaseViewHolder>(R.layout.item_order_list_mall_child) {

    var showSku = false


    override fun convert(holder: BaseViewHolder, item: OrderMallGoods) {

        val child_image_iv = holder.getView<ImageView>(R.id.child_image_iv)
        val child_name_tv = holder.getView<TextView>(R.id.child_name_tv)
        val child_sku_tv = holder.getView<TextView>(R.id.child_sku_tv)


        val goodImage = item.good_image as String
        GlideUtil.load(context, goodImage, child_image_iv, 4)

        if (showSku) {
            child_name_tv.text = item.good_title

            val buffer = StringBuffer()
            item.good_specs.forEach {
                buffer.append(it.key + ":" + it.value + "; ")
            }
            child_sku_tv.text = buffer

            child_name_tv.visibility = View.VISIBLE
            child_sku_tv.visibility = View.VISIBLE

        } else {
            child_name_tv.visibility = View.GONE
            child_sku_tv.visibility = View.GONE
        }


    }
}