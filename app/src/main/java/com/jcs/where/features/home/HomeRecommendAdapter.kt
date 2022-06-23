package com.jcs.where.features.home

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.jcs.where.api.response.recommend.HomeRecommendResponse.Companion.MODULE_TYPE_1_HOTEL
import com.jcs.where.api.response.recommend.HomeRecommendResponse.Companion.MODULE_TYPE_2_SERVICE
import com.jcs.where.api.response.recommend.HomeRecommendResponse.Companion.MODULE_TYPE_3_FOOD
import com.jcs.where.api.response.recommend.HomeRecommendResponse.Companion.MODULE_TYPE_4_TRAVEL
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.ratingstar.RatingStarView

/**
 * Created by Wangsw  2021/8/12 11:12.
 *
 */
open class HomeRecommendAdapter : BaseMultiItemQuickAdapter<HomeRecommendResponse, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel_3)
        addItemType(MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service_3)
        addItemType(MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_food_3)
        addItemType(MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel_3)
    }


    override fun convert(holder: BaseViewHolder, item: HomeRecommendResponse) {



        when (holder.itemViewType) {
            MODULE_TYPE_1_HOTEL -> bindHotelView(holder, item)
            MODULE_TYPE_2_SERVICE -> bindServiceView(holder, item)
            MODULE_TYPE_3_FOOD -> bindFoodView(holder, item)
            MODULE_TYPE_4_TRAVEL -> bindTraverView(holder, item)
            else -> {
            }
        }
    }


    /**
     * 酒店
     */
    private fun bindHotelView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        val container_ll = holder.getView<LinearLayout>(R.id.container_ll)
        val adapterPosition = holder.adapterPosition
        val layoutParams = container_ll.layoutParams as RecyclerView.LayoutParams

        layoutParams.apply {
            topMargin = if (adapterPosition < (2 + headerLayoutCount)) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
        container_ll.layoutParams = layoutParams


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title

        // 星级
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        star_view.rating = FeaturesUtil.getSafeStarLevel(data.star_level)

        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()


        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))


        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 价格
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val original_price_tv = holder.getView<TextView>(R.id.original_price_tv)


        val price = data.price
        val originalPrice = data.original_cost
        BusinessUtils.setNowPriceAndOldPrice(price, originalPrice, price_tv, original_price_tv)
    }


    /**
     * 综合服务
     */
    private fun bindServiceView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        val container_ll = holder.getView<LinearLayout>(R.id.container_ll)
        val adapterPosition = holder.adapterPosition
        val layoutParams = container_ll.layoutParams as RecyclerView.LayoutParams

        layoutParams.apply {
            topMargin = if (adapterPosition < (2 + headerLayoutCount)) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
        container_ll.layoutParams = layoutParams

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title

        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)
    }

    /**
     * 餐厅
     */
    private fun bindFoodView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        val container_ll = holder.getView<LinearLayout>(R.id.container_ll)
        val adapterPosition = holder.adapterPosition
        val layoutParams = container_ll.layoutParams as RecyclerView.LayoutParams

        layoutParams.apply {
            topMargin = if (adapterPosition < (2 + headerLayoutCount)) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
        container_ll.layoutParams = layoutParams

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title


        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()

        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))


        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

    }

    /**
     * 旅游
     */
    private fun bindTraverView(holder: BaseViewHolder, data: HomeRecommendResponse) {


        val container_ll = holder.getView<LinearLayout>(R.id.travel_container_ll)
        val adapterPosition = holder.adapterPosition
        val layoutParams = container_ll.layoutParams as RecyclerView.LayoutParams

        layoutParams.apply {
            topMargin = if (adapterPosition < (2 + headerLayoutCount)) {
                SizeUtils.dp2px(16f)
            } else {
                0
            }
        }
        container_ll.layoutParams = layoutParams


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title


        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()

        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))


        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

    }


    /**
     * 图片
     */
    fun loadImage(data: HomeRecommendResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: HomeRecommendResponse, tag_ll: LinearLayout) {
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