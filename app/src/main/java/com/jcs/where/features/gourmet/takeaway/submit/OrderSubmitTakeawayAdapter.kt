package com.jcs.where.features.gourmet.takeaway.submit

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform

/**
 * Created by Wangsw  2021/4/27 14:58.
 * 待提交订单的外卖菜品列表
 */
class OrderSubmitTakeawayAdapter : BaseQuickAdapter<DishResponse, BaseViewHolder>(R.layout.item_dishes_for_order_submit) {


    override fun convert(holder: BaseViewHolder, item: DishResponse) {

        val imageIv = holder.getView<ImageView>(R.id.order_image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)

        val now_price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val count_tv = holder.getView<TextView>(R.id.count_tv)


        GlideUtil.load(context,item.image,imageIv,4)
        good_name_tv.text = item.title

        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, item.price)

        // 商品数量
        count_tv.text = StringUtils.getString(R.string.count_format, item.nativeSelectCount)

    }


}