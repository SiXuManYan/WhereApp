package com.jcs.where.features.map

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.MechanismResponse
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/8/26 14:02.
 *  机构信息
 */
class MechanismAdapter : BaseQuickAdapter<MechanismResponse, BaseViewHolder>(R.layout.item_mechanism), LoadMoreModule {

    var showClose = false
    var ignoreTopMargin = false

    override fun convert(holder: BaseViewHolder, item: MechanismResponse) {

        val close_iv = holder.getView<ImageView>(R.id.close_iv)
        close_iv.visibility = if (showClose) {
            View.VISIBLE
        } else {
            View.GONE
        }

        if (!ignoreTopMargin) {
            val container_rl = holder.getView<RelativeLayout>(R.id.container_rl)
            val param = container_rl.layoutParams as RecyclerView.LayoutParams

            if (holder.adapterPosition == 0) {
                param.topMargin = SizeUtils.dp2px(15f)
            } else {
                param.topMargin = SizeUtils.dp2px(0f)
            }
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
        val home_tag_rv = holder.getView<RecyclerView>(R.id.home_tag_rv)
        BusinessUtils.initTag(item.tags, home_tag_rv)

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




}