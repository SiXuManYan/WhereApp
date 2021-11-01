package com.jcs.where.features.gourmet.restaurant.detail

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/1 15:47.
 * 堂食菜品
 */
class DishAdapter : BaseQuickAdapter<DishResponse, BaseViewHolder>(R.layout.item_dish) {

    override fun convert(holder: BaseViewHolder, item: DishResponse) {
        holder.setText(R.id.dish_name_tv, item.title)
        holder.setText(R.id.sales_tv, StringUtils.getString(R.string.sale_format, item.sale_num))
        holder.setText(R.id.now_price_tv, StringUtils.getString(R.string.price_unit_format, item.price.toPlainString()))
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)
        val dish_iv = holder.getView<ImageView>(R.id.dish_iv)

        val oldPrice = StringUtils.getString(R.string.price_unit_format, item.original_price.toPlainString())
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder

        GlideUtil.load(context, item.image, dish_iv, 4)
    }
}