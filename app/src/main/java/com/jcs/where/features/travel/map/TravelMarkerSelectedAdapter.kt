package com.jcs.where.features.travel.map

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/10/18 10:29.
 * 旅游 marker 选中后对应的列表
 */
class TravelMarkerSelectedAdapter :BaseQuickAdapter<TravelChild,BaseViewHolder>(R.layout.item_travel_child_marker){
    override fun convert(holder: BaseViewHolder, item: TravelChild) {
        bindView(holder, item)
    }

    private fun bindView(holder: BaseViewHolder, data: TravelChild) {

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.name


        // 距离 地点
        val distance_tv = holder.getView<TextView>(R.id.distance_tv)
        val distance = data.distance
        if (distance == "0" || distance.isBlank()) {
            distance_tv.visibility = View.GONE
        } else {
            distance_tv.visibility = View.VISIBLE
            distance_tv.text = StringUtils.getString(R.string.distance_format, distance)
        }
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(data, tag_ll)


    }

    /**
     * 图片
     */
    fun loadImage(data: TravelChild, image_iv: ImageView) {
        var image = ""
        if (data.image.isNotEmpty()) {
            image = data.image[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: TravelChild, tag_ll: LinearLayout) {
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