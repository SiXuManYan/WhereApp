package com.jcs.where.features.store.search

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.utils.FeaturesUtil
import com.jcs.where.utils.GlideUtil

/**
 * Created by Wangsw  2021/6/7 15:55.
 * 商城搜索结果
 */
class StoreSearchAdapter : BaseQuickAdapter<StoreRecommend, BaseViewHolder>(R.layout.item_store_search), LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: StoreRecommend) {

        val avatar_iv = holder.getView<ImageView>(R.id.avatar_iv)
        val title_tv = holder.getView<TextView>(R.id.title_tv)
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        val location_tv = holder.getView<TextView>(R.id.location_tv)

        // 图片
        item.images.forEachIndexed { index, url ->
            if (index == 0) {
                GlideUtil.load(context, url, avatar_iv, 4)
            }
        }
        title_tv.text = item.title

        location_tv.text = FeaturesUtil.getNoWrapString(item.address)

        // tag
        tag_ll.removeAllViews()
        item.tags.forEach {
            val params =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = SizeUtils.dp2px(2f)
                }
            val textView = TextView(context).apply {
                layoutParams = params
                setBackgroundResource(R.drawable.shape_orange_stoke_radius_2)
                maxLines = 1
                setPaddingRelative(SizeUtils.dp2px(5f), SizeUtils.dp2px(4f), SizeUtils.dp2px(5f), SizeUtils.dp2px(4f));
                setTextColor(ColorUtils.getColor(R.color.orange_FF5837))
                textSize = 11f
                text = it
            }
            tag_ll.addView(textView)
        }

        // child
        bindChild(holder, item)


    }

    private fun bindChild(holder: BaseViewHolder, item: StoreRecommend) {
        val child_container_ll = holder.getView<LinearLayout>(R.id.child_container_ll)
        child_container_ll.removeAllViews()


        item.goods.forEachIndexed { index, it ->

            val child = LayoutInflater.from(context).inflate(R.layout.item_store_search_child, null)
            child_container_ll.addView(child)


            val child_ll = child.findViewById<LinearLayout>(R.id.child_ll)
            val image_iv = child.findViewById<ImageView>(R.id.image_iv)
            val good_title_tv = child.findViewById<TextView>(R.id.good_title_tv)
            val now_price_tv = child.findViewById<TextView>(R.id.now_price_tv)
            val old_price_tv = child.findViewById<TextView>(R.id.old_price_tv)


            // 边距
            val layoutParams = child_ll.layoutParams as LinearLayout.LayoutParams
            layoutParams.apply {
                marginEnd = SizeUtils.dp2px(15f)
            }
            child_ll.layoutParams = layoutParams

            // 图片
            it.images.forEachIndexed { imageIndex, url ->
                if (imageIndex == 0) {
                    GlideUtil.load(context, url, image_iv, 4)
                }
            }

            good_title_tv.text = it.title

            // 价格
            now_price_tv.text = StringUtils.getString(R.string.price_unit_format, it.price.toPlainString())
            val oldPrice = StringUtils.getString(R.string.price_unit_format, it.original_price.toPlainString())
            val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
            old_price_tv.text = builder


            val goodId = it.id
//            child.setOnClickListener {
//
//                val intent = Intent(context, StoreGoodDetailActivity::class.java).setFlags(FLAG_ACTIVITY_NEW_TASK)
//                intent.putExtras(Bundle().apply {
//                    putInt(Constant.PARAM_ID, goodId)
//                })
//                context.startActivity(intent)
//
//            }

        }


    }
}