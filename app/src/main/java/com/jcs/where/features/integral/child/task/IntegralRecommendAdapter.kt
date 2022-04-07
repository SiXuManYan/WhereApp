package com.jcs.where.features.integral.child.task

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.jcs.where.api.response.recommend.HomeRecommendResponse
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.chad.library.adapter.base.module.LoadMoreModule
import com.jcs.where.R
import com.jcs.where.widget.ratingstar.RatingStarView
import android.widget.TextView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.utils.FeaturesUtil
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.request.RequestOptions
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.bumptech.glide.Glide
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils

/**
 * Created by Wangsw  2021/3/2 11:39.
 * 签到推荐列表
 */
class IntegralRecommendAdapter : BaseMultiItemQuickAdapter<HomeRecommendResponse, BaseViewHolder>(), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, data: HomeRecommendResponse) {
        val itemViewType = holder.itemViewType
        when (itemViewType) {
            HomeRecommendResponse.MODULE_TYPE_1_HOTEL -> bindHotelView(holder, data)
            HomeRecommendResponse.MODULE_TYPE_2_SERVICE -> bindServiceView(holder, data)
            HomeRecommendResponse.MODULE_TYPE_3_FOOD -> bindFoodView(holder, data)
            HomeRecommendResponse.MODULE_TYPE_4_TRAVEL -> bindTraverView(holder, data)
            else -> {}
        }
    }

    /**
     * 美食
     */
    private fun bindFoodView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        holder.setText(R.id.title_tv, data.title)

        // 星级
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        val grade = data.grade
        if (grade < 3.0) {
            star_view.visibility = View.GONE
            score_tv.visibility = View.GONE
        } else {
            star_view.visibility = View.VISIBLE
            score_tv.visibility = View.VISIBLE
            star_view.rating = grade
            score_tv.text = grade.toString()
        }

        // 评论数
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format2, data.comment_num))

        // 地域 、 餐厅类型
        holder.setText(R.id.area_name_tv, data.area_name)
        holder.setText(R.id.restaurant_type_tv, data.restaurant_type)

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 外卖
        val takeaway_ll = holder.getView<LinearLayout>(R.id.take_ll)
        if (data.take_out_status == 2) {
            takeaway_ll.visibility = View.VISIBLE
        } else {
            takeaway_ll.visibility = View.GONE
        }

        // 人均
        val per_price_tv = holder.getView<TextView>(R.id.per_price_tv)
        per_price_tv.text = StringUtils.getString(R.string.per_price_format, data.per_price)
    }

    /**
     * 旅游view
     */
    private fun bindTraverView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        holder.setText(R.id.title_tv, data.title)

        // 评分
        holder.setText(R.id.score_tv, data.grade.toString())
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_num))

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 地址
        holder.setText(R.id.address_name_tv, data.address)
        holder.setText(R.id.food_distance_tv, StringUtils.getString(R.string.distance_format, data.distance))
    }

    /**
     * 综合服务
     *
     * @param holder
     * @param data
     */
    private fun bindServiceView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        holder.setText(R.id.title_tv, data.title)

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 地址
        holder.setText(R.id.address_name_tv, data.address)
    }

    /**
     * 酒店推荐
     */
    private fun bindHotelView(holder: BaseViewHolder, data: HomeRecommendResponse) {

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title
        if (!TextUtils.isEmpty(data.facebook_link)) {
            title_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_facebook, 0)
        } else {
            title_tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
        }


        // 星级
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        val score_retouch_tv = holder.getView<TextView>(R.id.score_retouch_tv)
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        val starLevel = FeaturesUtil.getSafeStarLevel(data.star_level)
        star_view.rating = starLevel
        val grade = data.grade
        if (grade < 3.0) {
            star_view.visibility = View.INVISIBLE
            score_tv.visibility = View.GONE
            score_retouch_tv.visibility = View.GONE
        } else {
            star_view.visibility = View.VISIBLE
            score_tv.visibility = View.VISIBLE
            score_tv.text = grade.toString()
            score_retouch_tv.text = FeaturesUtil.getGradeRetouchString(grade)
        }

        // 评论数量
        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, data.comment_num))

        // 距离 地点
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))
        holder.setText(R.id.location_tv, data.address)

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)

        // 价格
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        SpanUtils.with(price_tv)
            .append(StringUtils.getString(R.string.price_unit))
            .setFontSize(12, true)
            .append(data.price)
            .setFontSize(16, true)
            .create()
    }

    /**
     * 图片
     */
    private fun loadImage(data: HomeRecommendResponse, image_iv: ImageView) {
        val options = RequestOptions.bitmapTransform(
            GlideRoundedCornersTransform(10, GlideRoundedCornersTransform.CornerType.LEFT))
            .error(R.mipmap.ic_empty_gray)
            .placeholder(R.mipmap.ic_empty_gray)
        var image: String? = ""
        if (data.images != null && data.images.size > 0) {
            image = data.images[0]
            Glide.with(context).load(image).apply(options).into(image_iv)
        } else {
            Glide.with(context).load(R.mipmap.ic_empty_gray).apply(options).into(image_iv)
        }
    }

    /**
     * tag
     */
    private fun initTag(data: HomeRecommendResponse, tag_ll: LinearLayout) {
        tag_ll.removeAllViews()
        val tags = data.tags
        if (tags.size <= 0) {
            return
        }
        for (tag in tags) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginEnd = SizeUtils.dp2px(2f)
            val tv = TextView(context)
            tv.layoutParams = params
            tv.setPaddingRelative(SizeUtils.dp2px(4f), SizeUtils.dp2px(1f), SizeUtils.dp2px(4f), SizeUtils.dp2px(1f))
            tv.setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2))
            tv.textSize = 11f
            tv.text = tag
            tv.setBackgroundResource(R.drawable.shape_blue_stoke_radius_1)
            tv.isSingleLine = true
            tag_ll.addView(tv)
        }
    }

    init {
        addItemType(HomeRecommendResponse.MODULE_TYPE_1_HOTEL, R.layout.item_home_recommend_hotel)
        addItemType(HomeRecommendResponse.MODULE_TYPE_2_SERVICE, R.layout.item_home_recommend_service)
        addItemType(HomeRecommendResponse.MODULE_TYPE_3_FOOD, R.layout.item_home_recommend_food)
        addItemType(HomeRecommendResponse.MODULE_TYPE_4_TRAVEL, R.layout.item_home_recommend_travel)
    }
}