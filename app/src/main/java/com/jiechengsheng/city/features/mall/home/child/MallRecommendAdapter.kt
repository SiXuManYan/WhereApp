package com.jiechengsheng.city.features.mall.home.child

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallGood
import com.jiechengsheng.city.features.mall.detail.MallDetailActivity
import com.jiechengsheng.city.features.mall.shop.home.MallShopHomeActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.GlideUtil


/**
 * Created by Wangsw  2021/12/2 17:09.
 * 新版商城推荐
 */
class MallRecommendAdapter : BaseQuickAdapter<MallGood, BaseViewHolder>(R.layout.item_mall_recommend), LoadMoreModule {

    /** 是否需要隐藏店铺名称 */
    var hideShopName = false

    override fun convert(holder: BaseViewHolder, item: MallGood) {

        val container_ll = holder.getView<LinearLayout>(R.id.container_ll)
        val image = holder.getView<ImageView>(R.id.image_iv)
        val title = holder.getView<TextView>(R.id.title_tv)
        val nowPriceTv = holder.getView<TextView>(R.id.now_price_tv)
        val originalPriceTv = holder.getView<TextView>(R.id.original_price_tv)
        val shop_name_tv = holder.getView<TextView>(R.id.shop_name_tv)

        if (hideShopName) {
            shop_name_tv.visibility = View.GONE
        }
        shop_name_tv.text = item.shop_name

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

        // 店铺入口
        shop_name_tv.setOnClickListener {
            MallShopHomeActivity.navigation(context, item.shop_id)
        }
    }
}