package com.jiechengsheng.city.features.integral.activitys.child

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.integral.IntegralGood
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2022/9/21 14:01.
 *
 */
class IntegralGoodAdapter : BaseQuickAdapter<IntegralGood, BaseViewHolder>(R.layout.item_integral_good), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: IntegralGood) {
        val container_ll = holder.getView<RelativeLayout>(R.id.container_ll)
        val image = holder.getView<ImageView>(R.id.image_iv)
        val title = holder.getView<TextView>(R.id.title_tv)
        val points_tv = holder.getView<TextView>(R.id.points_tv)
        val tag_tv = holder.getView<TextView>(R.id.tag_tv)

        val adapterPosition = holder.adapterPosition
        val layoutParams = container_ll.layoutParams as RecyclerView.LayoutParams

        layoutParams.apply {
            topMargin = if (adapterPosition == 0 ) {
                SizeUtils.dp2px(8f)
            } else {
                0
            }
        }
        container_ll.layoutParams = layoutParams

        GlideUtil.load(context, item.image, image, 4)
        title.text = item.title


        points_tv.text = StringUtils.getString(R.string.points_format, item.price)

        when (item.show_status) {
            0 -> {
                tag_tv.visibility = View.GONE
            }
            1 -> {
                tag_tv.visibility = View.VISIBLE
                tag_tv.text = StringUtils.getString(R.string.sold_out)
            }
            2 -> {
                tag_tv.visibility = View.VISIBLE
                tag_tv.text = StringUtils.getString(R.string.coming_soon)
            }
        }

    }
}