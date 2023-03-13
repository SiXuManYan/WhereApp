package com.jiechengsheng.city.features.store.recommend

import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.store.StoreRecommend
import com.jiechengsheng.city.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/1 16:09.
 *
 */
class StoreRecommendAdapter : BaseQuickAdapter<StoreRecommend, BaseViewHolder>(R.layout.item_store_recommend), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: StoreRecommend) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        val location_tv = holder.getView<TextView>(R.id.location_tv)


        item.images.forEachIndexed { index, url ->
            if (index == 0) {
                GlideUtil.load(context, url, image_iv, 5)
            }
        }
        title_tv.text = item.title
        location_tv.text = item.address

        tag_ll.removeAllViews()
        item.tags.forEach {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(20f)).apply {
                marginEnd = SizeUtils.dp2px(4f)
            }
            val textView = TextView(context).apply {
                layoutParams = params
                setBackgroundResource(R.drawable.shape_orange_stoke_radius_2)
                maxLines = 1
                gravity = Gravity.CENTER
                setPaddingRelative(SizeUtils.dp2px(4f), 0, SizeUtils.dp2px(4f), 0)
                setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
                textSize = 11f
                text = it
            }
            tag_ll.addView(textView)
        }


    }
}