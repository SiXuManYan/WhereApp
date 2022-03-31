package com.jcs.where.features.order

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
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

    /** 单个商品展示SKU */
    var showSku = false


    override fun convert(holder: BaseViewHolder, item: OrderMallGoods) {

        val child_image_iv = holder.getView<ImageView>(R.id.child_image_iv)
        val child_name_tv = holder.getView<TextView>(R.id.child_name_tv)
        val child_sku_tv = holder.getView<TextView>(R.id.child_sku_tv)
        val temp_v = holder.getView<View>(R.id.temp_v)
        val container = holder.getView<RelativeLayout>(R.id.mall_good_container_rl)


        val goodImage = item.good_image as String
        GlideUtil.load(context, goodImage, child_image_iv, 4)

        if (showSku) {
            // 重设宽度
            val layoutParams = container.layoutParams as RecyclerView.LayoutParams
            layoutParams.width = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32f)

            temp_v.visibility = View.VISIBLE


            child_name_tv.text = item.good_title

            val buffer = StringBuffer()
            item.good_specs.forEach {
                buffer.append(it.key + ":" + it.value + "; ")
            }
            child_sku_tv.text = buffer

            child_name_tv.visibility = View.VISIBLE
            child_sku_tv.visibility = View.VISIBLE

        } else {
            val layoutParams = container.layoutParams as RecyclerView.LayoutParams
            layoutParams.width = RecyclerView.LayoutParams.WRAP_CONTENT

            child_name_tv.visibility = View.GONE
            child_sku_tv.visibility = View.GONE
            temp_v.visibility = View.GONE
        }


    }
}