package com.jiechengsheng.city.features.footprint.child

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.footprint.Footprint
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/11/18 16:48.
 * 我的足迹
 */
class FootprintAdapter : BaseMultiItemQuickAdapter<Footprint, BaseViewHolder>(), LoadMoreModule {


    init {
        addItemType(Footprint.TYPE_HOTEL, R.layout.item_foot_print)
        addItemType(Footprint.TYPE_TRAVEL, R.layout.item_foot_print)
        addItemType(Footprint.TYPE_NEWS, R.layout.item_foot_print)
        addItemType(Footprint.TYPE_GENERAL, R.layout.item_foot_print)
        addItemType(Footprint.TYPE_RESTAURANT, R.layout.item_foot_print)
        addItemType(Footprint.TYPE_STORE, R.layout.item_foot_print)

        addItemType(Footprint.TYPE_GOOD, R.layout.item_foot_print_good)
        addItemType(Footprint.TYPE_TITLE, R.layout.item_foot_print_title)
    }


    override fun convert(holder: BaseViewHolder, item: Footprint) {
        when (holder.itemViewType) {
            Footprint.TYPE_HOTEL,
            Footprint.TYPE_TRAVEL,
            Footprint.TYPE_NEWS,
            Footprint.TYPE_GENERAL,
            Footprint.TYPE_RESTAURANT,
            Footprint.TYPE_STORE,
            -> {
                bindCommonCollection(holder, item)
            }
            Footprint.TYPE_GOOD -> {
                bindGoodCollection(holder, item)
            }
            Footprint.TYPE_TITLE -> {
                bindTitle(holder, item)
            }
            else -> {
            }
        }
    }


    private fun bindCommonCollection(holder: BaseViewHolder, item: Footprint) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(8f)
            } else {
                0
            }
        }
        container.layoutParams = layoutParams

        val module = item.module_data ?: return
        loadImage(module.images, image_iv)
        title_tv.text = module.title
    }


    private fun bindGoodCollection(holder: BaseViewHolder, item: Footprint) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val original_price_tv = holder.getView<TextView>(R.id.original_price_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val container = holder.getView<ViewGroup>(R.id.container_view)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(8f)
            } else {
                0
            }
        }
        container.layoutParams = layoutParams

        val module = item.module_data ?: return
        loadImage(module.images, image_iv)
        title_tv.text = module.title

        // 价格
        val nowPrice = module.price
        val originalPrice = module.original_cost
        BusinessUtils.setNowPriceAndOldPrice(nowPrice, originalPrice, price_tv, original_price_tv)
    }

    private fun bindTitle(holder: BaseViewHolder, item: Footprint) {
        holder.setText(R.id.title_tv, item.date)
    }


    fun loadImage(images: ArrayList<String>, image_iv: ImageView) {
        images.forEach {
            if (it.isNotBlank()) {
                GlideUtil.load(context, it, image_iv, 4)
                return@forEach
            }
        }

    }
}