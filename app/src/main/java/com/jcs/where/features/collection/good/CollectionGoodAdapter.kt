package com.jcs.where.features.collection.good

import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.collection.MallGoodCollection
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/12/28 11:13.
 *
 */
class CollectionGoodAdapter : BaseQuickAdapter<MallGoodCollection, BaseViewHolder>(R.layout.item_collection_mall_good),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: MallGoodCollection) {
        holder.setText(R.id.good_name_tv, item.title)
        holder.setText(R.id.now_price_tv, StringUtils.getString(R.string.price_unit_format, item.price))
        val container = holder.getView<RelativeLayout>(R.id.child_container_rl)
        val goodIv = holder.getView<ImageView>(R.id.good_iv)

        GlideUtil.load(context, item.images, goodIv, 4)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = if (holder.adapterPosition == 0) {
            SizeUtils.dp2px(15f)
        } else {
            0
        }


    }
}