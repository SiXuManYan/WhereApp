package com.jcs.where.features.mall.shop.home.recommend

import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.mall.MallShopRecommend
import com.jcs.where.api.response.mall.ShopRecommend
import com.jcs.where.features.mall.detail.MallDetailActivity
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.pager.IndicatorView2
import kotlinx.android.synthetic.main.activity_hotel_detail_new_2.*

/**
 * Created by Wangsw  2022/2/24 15:12.
 *
 */
class ShopRecommendHeaderAdapter : BaseMultiItemQuickAdapter<ShopRecommend, BaseViewHolder>() {


    init {
        addItemType(ShopRecommend.TYPE_CARD, R.layout.item_mall_shop_recommend_card)
        addItemType(ShopRecommend.TYPE_BANNER, R.layout.item_mall_shop_recommend_banner)
    }

    override fun convert(holder: BaseViewHolder, item: ShopRecommend) {

        if (holder.itemViewType == ShopRecommend.TYPE_BANNER) {
            bindBanner(holder, item)

        } else {
            bindCardImage(holder, item)
        }
    }

    private fun bindBanner(holder: BaseViewHolder, item: ShopRecommend) {

        val mMediaAdapter = ShopRecommendBannerAdapter()
        setOnItemClickListener { adapter, view, position ->
            val source = mMediaAdapter.data[position]
            if (source.type == 1) {
                MallDetailActivity.navigation(context, source.goods_id)
            }
        }


        val media_rv = holder.getView<RecyclerView>(R.id.media_rv)
        val point_view = holder.getView<IndicatorView2>(R.id.point_view)


        media_rv.apply {
            layoutManager = LinearLayoutManager(this@ShopRecommendHeaderAdapter.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mMediaAdapter
        }
        media_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                val linearManager = recyclerView.layoutManager as LinearLayoutManager
                val firstItemPosition: Int = linearManager.findFirstVisibleItemPosition()

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    point_view.onPageSelected(firstItemPosition)
                }

            }

        })

        PagerSnapHelper().attachToRecyclerView(media_rv)

        mMediaAdapter.setNewInstance(item.recommend)
        point_view.setPointCount(item.recommend.size)
    }

    private fun bindCardImage(holder: BaseViewHolder, item: ShopRecommend) {
        val card_iv = holder.getView<ImageView>(R.id.card_iv)
        if (item.recommend.isNotEmpty()) {
            val imageData = item.recommend[0]
            GlideUtil.load(context, imageData.img, card_iv)

            card_iv.setOnClickListener {
                if (imageData.type == 1) {
                    MallDetailActivity.navigation(context, imageData.goods_id)
                }
            }

        }


    }

}

class ShopRecommendBannerAdapter : BaseQuickAdapter<MallShopRecommend, BaseViewHolder>(R.layout.media_item_image) {


    override fun convert(holder: BaseViewHolder, item: MallShopRecommend) {
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        Glide.with(context).load(item.img).into(image_iv)

        image_iv.setOnClickListener {
            if (item.type == 1) {
                MallDetailActivity.navigation(context, item.goods_id)
            }
        }


    }

}
