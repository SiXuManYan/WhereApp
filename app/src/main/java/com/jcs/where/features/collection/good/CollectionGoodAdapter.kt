package com.jcs.where.features.collection.good

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
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
        holder.setText(R.id.now_price_tv, StringUtils.getString(R.string.price_unit_format, item.price))

        val container = holder.getView<RelativeLayout>(R.id.child_container_rl)
        val goodIv = holder.getView<ImageView>(R.id.good_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val invalid_tv = holder.getView<TextView>(R.id.invalid_tv)
        val invalid_reason_tv = holder.getView<TextView>(R.id.invalid_reason_tv)
        val bottom_sw = holder.getView<ViewSwitcher>(R.id.bottom_sw)

        good_name_tv.text =  item.title

        GlideUtil.load(context, item.image, goodIv, 4)
        val layoutParams = container.layoutParams as RecyclerView.LayoutParams
        layoutParams.topMargin = if (holder.adapterPosition == 0) {
            SizeUtils.dp2px(15f)
        } else {
            0
        }

        when {
            item.delete_status == 1 -> {
                // 已删除
                invalid_tv.visibility = View.VISIBLE
                invalid_tv.text = StringUtils.getString(R.string.estore_goods_expired)
                invalid_reason_tv.text = StringUtils.getString(R.string.estore_goods_expired_reason)
                bottom_sw.displayedChild  = 1
                good_name_tv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
            }
            item.good_status == 0 -> {
                // 已下架
                invalid_tv.visibility = View.VISIBLE
                invalid_tv.text = StringUtils.getString(R.string.estore_goods_removed)
                invalid_reason_tv.text = StringUtils.getString(R.string.estore_goods_removed_reason)
                bottom_sw.displayedChild  = 1
                good_name_tv.setTextColor(ColorUtils.getColor(R.color.grey_999999))
            }
            else -> {
                invalid_tv.visibility = View.GONE
                bottom_sw.displayedChild  = 0
                good_name_tv.setTextColor(ColorUtils.getColor(R.color.black_333333))
            }
        }


    }
}