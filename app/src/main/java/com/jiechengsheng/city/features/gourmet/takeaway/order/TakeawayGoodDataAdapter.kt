package com.jiechengsheng.city.features.gourmet.takeaway.order

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.order.TakeawayGoodData
import com.jiechengsheng.city.utils.image.GlideRoundedCornersTransform

/**
 * Created by Wangsw  2021/5/14 13:45.
 *
 */
class TakeawayGoodDataAdapter : BaseQuickAdapter<TakeawayGoodData, BaseViewHolder>(R.layout.item_dishes_order_detail_takeaway) {


    override fun convert(holder: BaseViewHolder, item: TakeawayGoodData) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val count_tv = holder.getView<TextView>(R.id.count_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)

        Glide.with(context).load(item.good_image).apply(options).into(image_iv)

        good_name_tv.text = item.good_name

        count_tv.text = StringUtils.getString(R.string.count_format, item.good_num)
        price_tv.text = StringUtils.getString(R.string.price_unit_format, item.good_price.toPlainString())
    }

}