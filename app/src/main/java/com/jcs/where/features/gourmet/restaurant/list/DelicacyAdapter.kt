package com.jcs.where.features.gourmet.restaurant.list

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.ratingstar.RatingStarView
import java.math.BigDecimal

/**
 * Created by Wangsw  2021/10/28 13:53.
 *
 */
class DelicacyAdapter : BaseMultiItemQuickAdapter<RestaurantResponse, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(RestaurantResponse.CONTENT_TYPE_COMMON, R.layout.item_delicacy_list)
        addItemType(RestaurantResponse.CONTENT_TYPE_CARD, R.layout.item_delicacy_list_card)
    }

    override fun convert(holder: BaseViewHolder, item: RestaurantResponse) {


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(item, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = item.title


        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = item.grade.toString()

        // 星星
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        star_view.rating = item.grade

        // 地点
        holder.setText(R.id.location_tv, item.trading_area.replace("\n", ""))

        // 人均
        val perPrice = item.per_price
        val per_price_tv = holder.getView<TextView>(R.id.per_price_tv)
        if (perPrice == BigDecimal.ZERO) {
            per_price_tv.visibility = View.GONE
        } else {
            per_price_tv.visibility = View.VISIBLE
            per_price_tv.setText(StringUtils.getString(R.string.per_price_format, perPrice))
        }


        // tag
        val home_tag_rv = holder.getView<RecyclerView>(R.id.home_tag_rv)
        BusinessUtils.initTag(item.tags, home_tag_rv)

        when (holder.itemViewType) {
            RestaurantResponse.CONTENT_TYPE_COMMON -> {

                val container = holder.getView<LinearLayout>(R.id.delicacy_container_ll)
                val adapterPosition = holder.adapterPosition
                val layoutParams = container.layoutParams as RecyclerView.LayoutParams

                layoutParams.apply {
                    topMargin = if (adapterPosition < 2) {
                        SizeUtils.dp2px(20f)
                    } else {
                        0
                    }
                }
                container.layoutParams = layoutParams

                // 外卖
                val takeaway_support_tv = holder.getView<TextView>(R.id.takeaway_support_tv)
                takeaway_support_tv.visibility = if (item.take_out_status == 2) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            }
            RestaurantResponse.CONTENT_TYPE_CARD -> {
                // 外卖
                val takeaway_ll = holder.getView<LinearLayout>(R.id.takeaway_ll)
                takeaway_ll.visibility = if (item.take_out_status == 2) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

    }


    /**
     * 图片
     */
    fun loadImage(data: RestaurantResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


}