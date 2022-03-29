package com.jcs.where.features.order.refund

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.order.RefundOrder
import com.jcs.where.features.mall.refund.order.MallRefundDetailActivity
import com.jcs.where.features.mall.shop.home.MallShopHomeActivity
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2022/3/26 10:44.
 *
 */
class RefundOrderAdapter : BaseQuickAdapter<RefundOrder, BaseViewHolder>(R.layout.item_order_list_refund),LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: RefundOrder) {

        val title_rl = holder.getView<RelativeLayout>(R.id.title_rl)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val view_detail_tv = holder.getView<TextView>(R.id.view_detail_tv)

        holder.setText(R.id.name_tv, item.shop_title)

        holder.setText(R.id.order_status_tv, BusinessUtils.getMallGoodRefundStatusText(item.status))

        val goodInfo = item.good_info
        holder.setText(R.id.good_name_tv, goodInfo.good_title)

        holder.setText(R.id.good_sku_tv, goodInfo.good_specs)


        holder.setText(R.id.total_number_tv , StringUtils.getString(R.string.pieces_format , goodInfo.good_num))
        holder.setText(R.id.total_price_tv , StringUtils.getString(R.string.total_price_format , goodInfo.refund_money))


        title_rl.setOnClickListener {
            MallShopHomeActivity.navigation(context, item.shop_id)
        }
        GlideUtil.load(context,goodInfo.good_image,image_iv)

        view_detail_tv.setOnClickListener {
            MallRefundDetailActivity.navigation(context,goodInfo.order_id,goodInfo.refund_id)
        }
    }
}