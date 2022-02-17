package com.jcs.where.features.gourmet.restaurant.detail

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/1 15:47.
 * 堂食菜品
 */
class DishAdapter : BaseQuickAdapter<DishResponse, BaseViewHolder>(R.layout.item_dish) {

    override fun convert(holder: BaseViewHolder, item: DishResponse) {
        val dish_name_tv = holder.getView<TextView>(R.id.dish_name_tv)
        val sales_tv = holder.getView<TextView>(R.id.sales_tv)
        val now_price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)
        val buy_tv = holder.getView<TextView>(R.id.buy_tv)
        val dish_iv = holder.getView<ImageView>(R.id.dish_iv)

        dish_name_tv.text = item.title
        sales_tv.text = StringUtils.getString(R.string.sale_format, item.sale_num)
        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, item.price.toPlainString())

        val oldPrice = StringUtils.getString(R.string.price_unit_format, item.original_price.toPlainString())
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder

        GlideUtil.load(context, item.image, dish_iv, 4)

        if (BusinessUtils.getSafeStock(item.inventory) > 0) {
            dish_name_tv.setTextColor(ColorUtils.getColor(R.color.black_333333))
            sales_tv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
            now_price_tv.setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
            buy_tv.setBackgroundResource(R.drawable.shape_orange_radius_15)
        } else {
            dish_name_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            sales_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            now_price_tv.setTextColor(ColorUtils.getColor(R.color.grey_b7b7b7))
            buy_tv.setBackgroundResource(R.drawable.shape_orange_radius_15_gray)
        }

    }
}