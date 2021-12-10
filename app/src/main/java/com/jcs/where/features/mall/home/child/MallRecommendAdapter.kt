package com.jcs.where.features.mall.home.child

import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/12/2 17:09.
 * 新版商城推荐
 */
class MallRecommendAdapter : BaseQuickAdapter<MallGood, BaseViewHolder>(R.layout.item_mall_recommend) ,LoadMoreModule{
    override fun convert(holder: BaseViewHolder, item: MallGood) {

        val image = holder.getView<ImageView>(R.id.image_iv)
        val title = holder.getView<TextView>(R.id.title_tv)
        val nowPrice = holder.getView<TextView>(R.id.now_price_tv)
        val oldPrice = holder.getView<TextView>(R.id.old_price_tv)

        GlideUtil.load(context, item.main_image, image, 4)
        title.text = item.title
        nowPrice.text = StringUtils.getString(R.string.price_unit_format, item.price.toPlainString())

        val oldBuilder = SpanUtils().append(StringUtils.getString(R.string.price_unit_format, item.oldPrice.toPlainString()))
            .setStrikethrough().create()
        oldPrice.text = oldBuilder


    }
}