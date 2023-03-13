package com.jiechengsheng.city.features.hotel.home

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.FeaturesUtil
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.widget.ratingstar.RatingStarView
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/9/23 10:12.
 *  酒店的 推荐、地图 以及Maker选中弹出 通用列表
 */
class HotelHomeRecommendAdapter : BaseMultiItemQuickAdapter<HotelHomeRecommend, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(HotelHomeRecommend.CONTENT_TYPE_COMMON, R.layout.item_hotel_recommend)
        addItemType(HotelHomeRecommend.CONTENT_TYPE_CARD, R.layout.item_hotel_recommend_card)
    }

    override fun convert(holder: BaseViewHolder, item: HotelHomeRecommend) {
        if (holder.itemViewType == HotelHomeRecommend.CONTENT_TYPE_COMMON) {
            val container = holder.getView<LinearLayout>(R.id.hotel_recommend_container_ll)
            val layoutParams = container.layoutParams as RecyclerView.LayoutParams
            layoutParams.apply {
                topMargin = if (holder.adapterPosition == 0) {
                    SizeUtils.dp2px(15f)
                } else {
                    0
                }
            }
            container.layoutParams = layoutParams
        }

        bindHotelView(holder, item)
    }

    /**
     * 酒店
     */
    private fun bindHotelView(holder: BaseViewHolder, data: HotelHomeRecommend) {

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
        val price_ll = holder.getView<LinearLayout>(R.id.price_ll)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val original_price_tv = holder.getView<TextView>(R.id.original_price_tv)

        val price = data.price
        val originalPrice = data.original_cost
        BusinessUtils.setNowPriceAndOldPrice(price, originalPrice, price_tv, original_price_tv)

        // 2022-08-05 处理价格隐藏和显示
        if (price == BigDecimal.ZERO) {
            price_tv.visibility = View.GONE
        } else {
            price_tv.visibility = View.VISIBLE
        }

        if (originalPrice == BigDecimal.ZERO) {
            original_price_tv.visibility = View.GONE
        } else {
            original_price_tv.visibility = View.VISIBLE
        }

        if (price_tv.visibility == View.GONE && original_price_tv.visibility == View.GONE) {
            price_ll.visibility = View.GONE
        } else {
            price_ll.visibility = View.VISIBLE
        }

    }

    /**
     * 图片
     */
    fun loadImage(data: HotelHomeRecommend, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: HotelHomeRecommend, tag_ll: LinearLayout) {
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
                text = it
                setBackgroundResource(R.drawable.shape_blue_stoke_radius_2_98bbff);
                isSingleLine = true
            }
            tag_ll.addView(tv)
        }
    }

}