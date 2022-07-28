package com.jcs.where.features.hotel.detail

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.HotelRoomListResponse
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/10/9 16:17.
 *
 */
class HotelRoomAdapter : BaseQuickAdapter<HotelRoomListResponse, BaseViewHolder>(R.layout.item_hotel_room) {
    

    override fun convert(holder: BaseViewHolder, item: HotelRoomListResponse) {


        // 图片
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        loadImage(item, image_iv)


        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val room_desc_tv = holder.getView<TextView>(R.id.room_desc_tv)
        val price_tv = holder.getView<TextView>(R.id.price_tv)
        val original_price_tv = holder.getView<TextView>(R.id.original_price_tv)
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        val inventory_tv = holder.getView<TextView>(R.id.inventory_tv)
        val booking_tv = holder.getView<TextView>(R.id.booking_tv)

        title_tv.text = item.name
        room_desc_tv.text = item.hotel_room_type
        BusinessUtils.setNowPriceAndOldPrice(item.price,item.original_cost,price_tv,original_price_tv)
        inventory_tv.text = context.getString(R.string.surplus_format , item.remain_room_num)


        booking_tv.visibility =   if (item.room_show_status == 1) {
            View.VISIBLE
        }else {
            View.GONE
        }


        initTag(item,tag_ll)
    }


    /**
     * 图片
     */
    fun loadImage(data: HotelRoomListResponse, image_iv: ImageView) {
        var image = ""
        if (data.images.isNotEmpty()) {
            image = data.images[0]
        }
        GlideUtil.load(context, image, image_iv, 4)
    }

    private fun initTag(data: HotelRoomListResponse, tag_ll: LinearLayout) {
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
                text = it.zh_cn_name
                setBackgroundResource(R.drawable.shape_blue_stoke_radius_2_98bbff);
                isSingleLine = true
            }
            tag_ll.addView(tv)
        }
    }


}