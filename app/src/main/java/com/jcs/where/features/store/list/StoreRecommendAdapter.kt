package com.jcs.where.features.store.list

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.utils.GlideUtil
import com.jcs.where.utils.image.GlideRoundedCornersTransform

/**
 * Created by Wangsw  2021/6/1 16:09.
 *
 */
class StoreRecommendAdapter : BaseQuickAdapter<StoreRecommend, BaseViewHolder>(R.layout.item_store_recommend) {


    override fun convert(holder: BaseViewHolder, item: StoreRecommend) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        val location_tv = holder.getView<TextView>(R.id.location_tv)


        item.images.forEachIndexed { index, url ->
            if (index == 0) {
                GlideUtil.load(context, url, image_iv, 5, GlideRoundedCornersTransform.CornerType.ALL)
            }
        }
        title_tv.text = item.title
        location_tv.text = item.address

        tag_ll.removeAllViews()
        item.tags.forEach {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                marginEnd = SizeUtils.dp2px(2f)
            }
            val textView = TextView(context).apply {
                layoutParams = params
                setBackgroundResource(R.drawable.shape_orange_stoke_radius_2)
                maxLines = 1
                setPaddingRelative(SizeUtils.dp2px(5f), SizeUtils.dp2px(4f), SizeUtils.dp2px(5f), SizeUtils.dp2px(4f));
                setTextColor(ColorUtils.getColor(R.color.orange_FF5B1B))
                textSize = 11f
            }
            tag_ll.addView(textView)
        }


    }
}