package com.jcs.where.features.travel.map.child

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.travel.TravelChild
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/10/18 10:03.
 *  旅游管理，地图模式子列表
 */
class TravelChildAdapter : BaseQuickAdapter<TravelChild, BaseViewHolder>(R.layout.item_travel_map), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: TravelChild) = bindTraverView(holder, item)

    /**
     * 旅游
     */
    private fun bindTraverView(holder: BaseViewHolder, data: TravelChild) {

        // 与首页是共用布局，所以按照设计图，需要单独调整边距
        val travel_container_ll = holder.getView<LinearLayout>(R.id.travel_container_ll)
        val layoutParams = travel_container_ll.layoutParams as RecyclerView.LayoutParams
        layoutParams.apply {
            topMargin = if (holder.adapterPosition == 0) {
                SizeUtils.dp2px(15f)
            } else {
                0
            }
        }
        travel_container_ll.layoutParams = layoutParams

        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(data, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = data.name


        // 评分
        val score_tv = holder.getView<TextView>(R.id.score_tv)
        score_tv.text = data.grade.toString()

        // 距离 地点
        holder.setText(R.id.location_tv, data.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, data.distance))


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
        GlideUtil.load(context, image, image_iv, 12)
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