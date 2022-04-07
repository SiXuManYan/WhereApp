package com.jcs.where.features.gourmet.order

import android.widget.ImageView
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.utils.BigDecimalUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/4/21 14:09.
 *
 */
class OrderSubmitAdapter : BaseQuickAdapter<Products, BaseViewHolder>(R.layout.item_order_submit) {

    override fun convert(holder: BaseViewHolder, item: Products) {
        val goodData = item.good_data
        val price = goodData.price
        val goodNum = item.good_num
        val total = BigDecimalUtil.mul(price, BigDecimal(goodNum))

        holder.setText(R.id.good_name, goodData.title)
        holder.setText(R.id.now_price_tv, StringUtils.getString(R.string.price_unit_format, price.toPlainString()))
        holder.setText(R.id.count_tv, StringUtils.getString(R.string.pieces_format, goodNum))
        holder.setText(R.id.price_tv, StringUtils.getString(R.string.price_unit_format, total.toPlainString()))


        val image_iv = holder.getView<ImageView>(R.id.image_iv)

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)
        Glide.with(context).load(goodData.image).apply(options).into(image_iv)
    }
}