package com.jcs.where.features.store.detail.good

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreGoods
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/16 16:14.
 *
 */
class StoreGoodAdapter : BaseQuickAdapter<StoreGoods, BaseViewHolder>(R.layout.item_store_good_2), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: StoreGoods) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)

        val now_price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)
        val express_tv = holder.getView<TextView>(R.id.express_tv)
        val self_tv = holder.getView<TextView>(R.id.self_tv)
        val container = holder.getView<RelativeLayout>(R.id.child_container_rl)


        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(15f)
            } else {
                0
            }
        }




        if (item.images.isNotEmpty()) {
            GlideUtil.load(context, item.images[0], image_iv, 4)
        }


        good_name_tv.text = item.title
        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, item.price.toPlainString())
        val oldPrice = StringUtils.getString(R.string.price_unit_format, item.original_price.toPlainString())
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder

        when (item.delivery_type) {

            1 -> {
                self_tv.visibility = View.VISIBLE
                express_tv.visibility = View.GONE
            }
            2 -> {
                self_tv.visibility = View.GONE
                express_tv.visibility = View.VISIBLE
            }
            3 -> {
                self_tv.visibility = View.VISIBLE
                express_tv.visibility = View.VISIBLE
            }
        }

        holder.setText(R.id.sold_tv, StringUtils.getString(R.string.sale_format, item.sale_num))


    }
}