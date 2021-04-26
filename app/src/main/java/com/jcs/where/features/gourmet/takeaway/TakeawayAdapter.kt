package com.jcs.where.features.gourmet.takeaway

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.NumberView2

/**
 * Created by Wangsw  2021/4/25 14:33.
 * 外卖菜品列表
 */
class TakeawayAdapter : BaseQuickAdapter<DishResponse, BaseViewHolder>(R.layout.item_dishes) {


    var onSelectCountChange: OnSelectCountChange? = null

    /**
     * 用户选中数量更新监听
     */
    interface OnSelectCountChange {

        /**
         * 用户选中数量更新，重新计算金额和购物车数量
         * @param goodNum 更新后的数量
         * @param id 菜品id
         */
        fun selectCountChange(goodNum: Int, id: Int)

    }

    override fun convert(holder: BaseViewHolder, item: DishResponse) {

        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name_tv = holder.getView<TextView>(R.id.good_name_tv)
        val sales_tv = holder.getView<TextView>(R.id.sales_tv)
        val now_price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)
        val tag_ll = holder.getView<LinearLayout>(R.id.tag_ll)
        val number_view = holder.getView<NumberView2>(R.id.number_view)

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)

        Glide.with(context).load(item.image).apply(options).into(image_iv);

        good_name_tv.text = item.title
        sales_tv.text = StringUtils.getString(R.string.sale_format, item.sale_num)
        initTag(item, tag_ll)

        now_price_tv.text = StringUtils.getString(R.string.price_unit_format, item.price)

        val oldPrice = StringUtils.getString(R.string.price_unit_format, item.original_price)
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder

        // 商品数量
        number_view.updateNumber(item.nativeSelectCount)
        number_view.valueChangeListener = object : NumberView2.OnValueChangeListener {
            override fun onNumberChange(goodNum: Int) {
                item.nativeSelectCount = goodNum
                onSelectCountChange?.selectCountChange(goodNum , item.id)
            }
        }

    }


    private fun initTag(data: DishResponse, tag_ll: LinearLayout) {
        tag_ll.removeAllViews()
        val tags = data.tags
        if (tags.size <= 0) {
            return
        }
        for (tag in tags) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginEnd = SizeUtils.dp2px(2f)
            val tv = TextView(context).apply {
                layoutParams = params
                setPaddingRelative(SizeUtils.dp2px(4f), SizeUtils.dp2px(1f), SizeUtils.dp2px(4f), SizeUtils.dp2px(1f))
                setTextColor(ColorUtils.getColor(R.color.blue_4C9EF2))
                textSize = 11f
                text = tag
                setBackgroundResource(R.drawable.shape_blue_stoke_radius_1)
                isSingleLine = true
            }
            tag_ll.addView(tv)
        }
    }

}