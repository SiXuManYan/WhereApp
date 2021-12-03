package com.jcs.where.features.mall.home.child

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/12/2 17:09.
 * 新版商城推荐
 */
class MallRecommendAdapter :BaseQuickAdapter<StoreRecommend, BaseViewHolder>(R.layout.item_mall_rerommend) {
    override fun convert(holder: BaseViewHolder, item: StoreRecommend) {

        val image = holder.getView<ImageView>(R.id.image_iv)
        val title = holder.getView<TextView>(R.id.title_tv)
        val nowPrice = holder.getView<TextView>(R.id.now_price_tv)
        val oldPrice = holder.getView<TextView>(R.id.old_price_tv)

        item.images.forEach {
            GlideUtil.load(context,it,image,4)
            return@forEach
        }
        title.text = item.title
        nowPrice.text = StringUtils.getString(R.string.price_unit_format,"????")

        val oldBuilder = SpanUtils().append(StringUtils.getString(R.string.price_unit_format, "????"))
            .setStrikethrough().create()
        oldPrice.text = oldBuilder



    }
}