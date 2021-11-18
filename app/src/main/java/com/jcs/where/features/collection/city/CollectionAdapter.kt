package com.jcs.where.features.collection.city

import android.view.View
import android.view.ViewGroup
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
import com.jcs.where.api.response.collection.MyCollection
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.ratingstar.RatingStarView

/**
 * Created by Wangsw  2021/11/18 14:13.
 * 我的收藏
 */
class CollectionAdapter : BaseMultiItemQuickAdapter<MyCollection, BaseViewHolder>(), LoadMoreModule {

    init {
        addItemType(MyCollection.TYPE_HOTEL, R.layout.item_collection_hotel)
        addItemType(MyCollection.TYPE_TRAVEL, R.layout.item_collection_travel_general_restaurant_store)
        addItemType(MyCollection.TYPE_GENERAL, R.layout.item_collection_travel_general_restaurant_store)
        addItemType(MyCollection.TYPE_RESTAURANT, R.layout.item_collection_travel_general_restaurant_store)
        addItemType(MyCollection.TYPE_STORE, R.layout.item_collection_travel_general_restaurant_store)
        addItemType(MyCollection.TYPE_NEWS, R.layout.item_collection_news)
    }


    override fun convert(holder: BaseViewHolder, item: MyCollection) {

        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(15f)
            } else {
                0
            }
        }
        container.layoutParams = layoutParams

        when (holder.itemViewType) {
            MyCollection.TYPE_HOTEL -> {
                bindHotel(holder, item)
            }
            MyCollection.TYPE_TRAVEL,
            MyCollection.TYPE_GENERAL,
            MyCollection.TYPE_RESTAURANT,
            MyCollection.TYPE_STORE -> {
                bindCommon(holder, item)
            }
            MyCollection.TYPE_NEWS -> {
                bindNews(holder, item)
            }

            else -> {
            }
        }
    }

    /** 新闻收藏 */
    private fun bindNews(holder: BaseViewHolder, item: MyCollection) {

        val news = item.news ?: return
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val video_ll = holder.getView<LinearLayout>(R.id.video_container_ll)
        loadImage(news.cover_images, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = news.title

        val name = news.publisher?.nickname
        holder.setText(R.id.date_tv, StringUtils.getString(R.string.common_2_format, name, news.created_at))

        if (news.content_type == 2) {
            video_ll.visibility = View.VISIBLE
            holder.setText(R.id.video_duration_tv , news.video_time)
        } else {
            video_ll.visibility = View.GONE
        }


    }

    /** 酒店收藏 */
    private fun bindHotel(holder: BaseViewHolder, item: MyCollection) {

        val hotel = item.hotel ?: return

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(hotel.images, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = hotel.name

        // 星级
        val star_view = holder.getView<RatingStarView>(R.id.star_view)
        star_view.rating = hotel.grade

        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = hotel.grade.toString()

        holder.setText(R.id.comment_count_tv, StringUtils.getString(R.string.comment_count_format, hotel.comment_num))

        // 距离 地点
        holder.setText(R.id.location_tv, hotel.address.replace("\n", ""))

    }


    /** 旅游、综合服务、餐厅、商城收藏 */
    private fun bindCommon(holder: BaseViewHolder, item: MyCollection) {

        val imageIv = holder.getView<ImageView>(R.id.image_iv)
        val titleTv = holder.getView<TextView>(R.id.title_tv)
        val locationTv = holder.getView<TextView>(R.id.location_tv)

        when (holder.itemViewType) {

            MyCollection.TYPE_TRAVEL -> {
                item.travel?.let {
                    loadImage(it.images, imageIv)
                    titleTv.text = it.name
                    locationTv.text = it.address.replace("\n", "")
                }
            }
            MyCollection.TYPE_GENERAL -> {
                item.general?.let {
                    loadImage(it.images, imageIv)
                    titleTv.text = it.name
                    locationTv.text = it.address.replace("\n", "")
                }
            }
            MyCollection.TYPE_RESTAURANT -> {
                item.restaurant?.let {
                    loadImage(it.images, imageIv)
                    titleTv.text = it.name
                    locationTv.text = it.address.replace("\n", "")
                }
            }
            MyCollection.TYPE_STORE -> {
                item.estore?.let {
                    loadImage(it.images, imageIv)
                    titleTv.text = it.name
                    locationTv.text = it.address.replace("\n", "")
                }
            }

        }


    }


    /**
     * 图片
     */
    fun loadImage(images: ArrayList<String>, image_iv: ImageView) {
        images.forEach {
            if (it.isNotBlank()) {
                GlideUtil.load(context, it, image_iv, 4)
                return@forEach
            }
        }

    }


}