package com.jcs.where.features.collection.shop

import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.collection.MallShopCollection
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2022/1/25 14:39.
 *
 */
class CollectionShopAdapter:BaseQuickAdapter<MallShopCollection,BaseViewHolder>(R.layout.item_collection_mall_shop) ,LoadMoreModule{

    override fun convert(holder: BaseViewHolder, item: MallShopCollection) {
        holder.setText(R.id.good_name_tv, item.title)

        val container = holder.getView<RelativeLayout>(R.id.child_container_rl)
        val goodIv = holder.getView<ImageView>(R.id.good_iv)

        GlideUtil.load(context, item.image, goodIv, 4)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = if (holder.adapterPosition == 0) {
            SizeUtils.dp2px(15f)
        } else {
            0
        }
    }
}