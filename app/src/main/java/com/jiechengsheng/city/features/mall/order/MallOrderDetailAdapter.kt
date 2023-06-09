package com.jiechengsheng.city.features.mall.order

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.mall.MallOrderGood
import com.jiechengsheng.city.features.mall.refund.MallRefundEditActivity
import com.jiechengsheng.city.features.mall.refund.order.MallRefundInfoActivity
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/12/16 16:23.
 *
 */
class MallOrderDetailAdapter : BaseQuickAdapter<MallOrderGood, BaseViewHolder>(R.layout.item_dishes_for_order_detail) {
    override fun convert(holder: BaseViewHolder, item: MallOrderGood) {
        val image_iv = holder.getView<ImageView>(R.id.order_image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val good_count_tv = holder.getView<TextView>(R.id.count_tv)
        val attr_tv = holder.getView<TextView>(R.id.attr_tv)
        val price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val container = holder.getView<RelativeLayout>(R.id.child_container_rl)


        GlideUtil.load(context, item.good_image, image_iv, 4)
        good_name_tv.text = item.good_title
        good_count_tv.text = context.getString(R.string.count_format, item.good_num)
        price_tv.text = context.getString(R.string.price_unit_format, item.good_price.toString())


        val buffer = StringBuffer()
        item.good_specs.forEach {
            buffer.append(it.value + " ")
        }
        attr_tv.text = buffer

        val param = container.layoutParams as RecyclerView.LayoutParams
        if (holder.adapterPosition == 0) {
            param.topMargin = SizeUtils.dp2px(10f)
        } else {
            param.topMargin = 0
        }

        // 售后
        val refundHandle = holder.getView<TextView>(R.id.refund_handle_tv)
        if (item.order_good_status == 1) {
            refundHandle.visibility = View.VISIBLE
            refundHandle.text = BusinessUtils.getMallGoodRefundButtonText(item.status)

            if (item.status == 1 || item.status == 7) {
                refundHandle.setOnClickListener {
                    MallRefundEditActivity.navigation(context, item.order_id, item.refund_id, false)
                }
            } else {
                refundHandle.setOnClickListener {
                    MallRefundInfoActivity.navigation(context, item.order_id, item.refund_id)
                }
            }

        } else {
            refundHandle.visibility = View.GONE
        }


    }
}