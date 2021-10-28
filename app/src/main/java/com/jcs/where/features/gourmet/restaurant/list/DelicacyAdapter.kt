package com.jcs.where.features.gourmet.restaurant.list

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.restaurant.RestaurantResponse
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/10/28 13:53.
 *
 */
class DelicacyAdapter : BaseQuickAdapter<RestaurantResponse, BaseViewHolder>(R.layout.item_delicacy_list),LoadMoreModule {


    override fun convert(holder: BaseViewHolder, data: RestaurantResponse) {
        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.title


        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()

        // 地点
        holder.setText(R.id.location_tv, data.trading_area.replace("\n", ""))

        // 人均
        holder.setText(R.id.per_price_tv, StringUtils.getString(R.string.per_price_format, data.per_price))

        // 外卖
        val takeaway_support_tv = holder.getView<TextView>(R.id.takeaway_support_tv)
        takeaway_support_tv.visibility = if (data.take_out_status == 2) {
            View.VISIBLE
        } else {
            View.GONE
        }

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)
    }


    /**
     * 图片
     */
    fun loadImage(data: RestaurantResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: RestaurantResponse, tag_ll: LinearLayout) {
        tag_ll.removeAllViews()
        if (data.tags.isEmpty()) {
            return
        }

        data.tags.forEach {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginEnd = SizeUtils.dp2px(2f)
            val tv = TextView(context).apply {
                layoutParams = params
                setPaddingRelative(SizeUtils.dp2px(4f), SizeUtils.dp2px(2f), SizeUtils.dp2px(4f), SizeUtils.dp2px(2f))
                setTextColor(ColorUtils.getColor(R.color.blue_377BFF))
                textSize = 11f
                text = it
                setBackgroundResource(R.drawable.shape_blue_stoke_radius_2_98bbff);
                isSingleLine = true
            }
            tag_ll.addView(tv)
        }
    }

}