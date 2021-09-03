package com.jcs.where.features.map

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/8/26 14:02.
 *  机构信息
 */
class MechanismAdapter : BaseQuickAdapter<MechanismResponse, BaseViewHolder>(R.layout.item_mechanism), LoadMoreModule {

    public var showClose = false

    override fun convert(holder: BaseViewHolder, item: MechanismResponse) {

        val close_iv = holder.getView<ImageView>(R.id.close_iv)
        close_iv.visibility = if (showClose) {
            View.VISIBLE
        } else {
            View.GONE
        }

        val container_rl = holder.getView<RelativeLayout>(R.id.container_rl)
        if (holder.adapterPosition == 0) {
            val param = container_rl.layoutParams as RecyclerView.LayoutParams
            param.topMargin = SizeUtils.dp2px(15f)
            container_rl.layoutParams = param
        }


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(item, image_iv)

        // 标题
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        title_tv.text = item.title

        // 距离 地点
        holder.setText(R.id.location_tv, item.address.replace("\n", ""))
        holder.setText(R.id.distance_tv, StringUtils.getString(R.string.distance_format, item.distance))

        // tag
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        initTag(item, tag_ll)

    }

    /**
     * 图片
     */
    fun loadImage(data: MechanismResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }


    private fun initTag(data: MechanismResponse, tag_ll: LinearLayout) {
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


    public fun getSelectDataIndex(item: MechanismResponse): Int {
        val indexOf = data.indexOf(item)
        return if (indexOf == -1) {
            0
        } else {
            indexOf
        }

    }


}