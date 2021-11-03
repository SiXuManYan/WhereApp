package com.jcs.where.features.gourmet.cart

import android.view.View
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.NumberView

/**
 * Created by Wangsw  2021/4/8 14:59.
 *
 */
class ShoppingCartChildAdapter : BaseQuickAdapter<Products, BaseViewHolder>(R.layout.item_shopping_cart_child) {


    /**
     * 数量改变监听
     */
    var numberChangeListener: NumberView.OnValueChangerListener? = null

    /**
     * 选中状态监听
     */
    var checkedChangeListener: OnChildContainerClick? = null

    interface OnChildContainerClick {
        fun onClick(isChecked: Boolean)
    }


    override fun convert(holder: BaseViewHolder, products: Products) {


        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)

        val child_container_rl = holder.getView<RelativeLayout>(R.id.child_container_rl)
        val good_checked_iv = holder.getView<CheckedTextView>(R.id.good_checked_tv)
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name = holder.getView<TextView>(R.id.good_name)
        val now_price_tv = holder.getView<TextView>(R.id.now_price_tv)
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)

        val number_view = holder.getView<NumberView>(R.id.number_view).apply {

            val count = products.good_num

            MIN_GOOD_NUM = 1
            MAX_GOOD_NUM = BusinessUtils.getSafeStock(products.good_data.inventory)


            cut_iv.setImageResource(R.mipmap.ic_cut_black_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_black)
            cutResIdCommon = R.mipmap.ic_cut_black
            cutResIdMin = R.mipmap.ic_cut_black_transparent
            addResIdCommon = R.mipmap.ic_add_black
            addResIdMax = R.mipmap.ic_add_black_transparent

            updateNumber(count)

            setData(products)
            valueChangeListener = numberChangeListener
        }

        val goodData = products.good_data
        Glide.with(context).load(goodData.image).apply(options).into(image_iv)
        good_name.text = goodData.title

        val nowPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.price)
        now_price_tv.text = nowPrice

        val oldPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.original_price.toPlainString())
        val oldBuilder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = oldBuilder

        good_checked_iv.isChecked = products.nativeIsSelect

        good_checked_iv.setOnClickListener {
            val currentIsSelect = products.nativeIsSelect
            products.nativeIsSelect = !currentIsSelect

            if (products.nativeIsSelect) {
                VibrateUtils.vibrate(10)
            }
            good_checked_iv.isChecked = products.nativeIsSelect
            checkedChangeListener?.onClick(products.nativeIsSelect)

        }


    }
}