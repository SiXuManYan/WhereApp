package com.jcs.where.features.hotel.detail

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.hotel.Facilities
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/10/11 14:45.
 * 酒店设施
 */
class HotelFacilitiesAdapter :BaseQuickAdapter<Facilities,BaseViewHolder>(R.layout.item_hotel_facilities) {

    override fun convert(holder: BaseViewHolder, item: Facilities) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val content_tv = holder.getView<TextView>(R.id.content_tv)

        GlideUtil.load(context,item.icon,image_iv)
        content_tv.text = item.name

    }
}