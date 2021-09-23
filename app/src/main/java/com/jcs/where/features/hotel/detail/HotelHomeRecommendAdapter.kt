package com.jcs.where.features.hotel.detail

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.hotel.HotelListResponse
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.ratingstar.RatingStarView

/**
 * Created by Wangsw  2021/9/23 10:12.
 *
 */
class HotelHomeRecommendAdapter : BaseQuickAdapter<HotelListResponse, BaseViewHolder>(R.layout.item_hotel_recommend) {

    override fun convert(holder: BaseViewHolder, item: HotelListResponse) {
        bindHotelView(holder, item)
    }


    /**
     * 酒店
     */
    private fun bindHotelView(holder: BaseViewHolder, data: HotelListResponse) {


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.name

        // 星级
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        star_view.rating = FeaturesUtil.getSafeStarLevel(data.star_level)

        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()


        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 价格
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        SpanUtils.with(price_tv)
            .append(StringUtils.getString(R.string.price_unit))
            .setFontSize(12, true)
            .append(data.price)
            .setFontSize(14, true)
            .create()
    }

    /**
     * 图片
     */
    fun loadImage(data: HotelListResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: HotelListResponse, tag_ll: LinearLayout) {
        tag_ll.removeAllViews()
        if (data.tags.isEmpty()) {
            return
        }

        data.tags.forEach {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginEnd = SizeUtils.dp2px(2f)
            val tv = TextView(context).apply {
                layoutParams = params
                setPaddingRelative(SizeUtils.dp2px(4f), SizeUtils.dp2px(2f), SizeUtils.dp2px(4f), SizeUtils.dp2px(2f))
                setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
                textSize = 11f
                text = it.name
                setBackgroundResource(R.drawable.shape_blue_stoke_radius_2_98bbff);
                isSingleLine = true
            }
            tag_ll.addView(tv)
        }
    }

}