package com.jcs.where.features.mall.home.child

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallGood
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil


/**
 * Created by Wangsw  2021/12/2 17:09.
 * 新版商城推荐
 */
class MallRecommendAdapter : BaseQuickAdapter<MallGood, BaseViewHolder>(R.layout.item_mall_recommend), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: MallGood) {

        val container_ll = holder.getView<LinearLayout>(R.id.container_ll)
        val image = holder.getView<ImageView>(R.id.image_iv)
        val title = holder.getView<TextView>(R.id.title_tv)
        val nowPriceTv = holder.getView<TextView>(R.id.now_price_tv)
        val originalPriceTv = holder.getView<TextView>(R.id.original_price_tv)

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


        GlideUtil.load(context, item.main_image, image, 4)
        title.text = item.title


        container_ll.setOnClickListener {
            MallDetailActivity.navigation(context, item.id)
        }

        // 价格
        val price = item.price
        val originalCost = item.original_cost

        BusinessUtils.setNowPriceAndOldPrice(price, originalCost, nowPriceTv, originalPriceTv)
    }
}