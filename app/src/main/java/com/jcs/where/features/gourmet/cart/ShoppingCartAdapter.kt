package com.jcs.where.features.gourmet.cart

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.ShoppingCartResponse
import com.jcs.where.utils.image.GlideRoundedCornersTransform
import com.jcs.where.widget.NumberView

/**
 * Created by Wangsw  2021/4/7 16:34.
 */
class ShoppingCartAdapter : BaseQuickAdapter<ShoppingCartResponse, BaseViewHolder>(R.layout.item_shopping_cart) {


    private lateinit var restaurant_cb: MaterialCheckBox
    private lateinit var container_ll: LinearLayout

    /**
     * 数量改变监听
     */
    var numberChangeListener: NumberView.OnValueChangerListener? = null


    override fun convert(holder: BaseViewHolder, data: ShoppingCartResponse) {

        restaurant_cb = holder.getView(R.id.restaurant_cb)
        container_ll = holder.getView(R.id.container_ll)
        restaurant_cb.text = data.restaurant_name

        setChildData(data)
        setSelect()
    }

    private fun setSelect() {
        restaurant_cb.setOnCheckedChangeListener { buttonView, isChecked ->
            for (i in 0 until container_ll.childCount) {
                val good_cb = container_ll.getChildAt(i).findViewById<MaterialCheckBox>(R.id.good_cb)
                good_cb.isChecked = isChecked
            }
        }
    }

    private fun setChildData(data: ShoppingCartResponse) {

        val options = RequestOptions.bitmapTransform(
                GlideRoundedCornersTransform(4, GlideRoundedCornersTransform.CornerType.ALL))
                .error(R.mipmap.ic_empty_gray)
                .placeholder(R.mipmap.ic_empty_gray)

        val products = data.products


        if (!products.isNullOrEmpty()) {

            container_ll.removeAllViews()
            products.forEach {
                val goodData = it.good_data

                val view: View = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_child, null)

                val good_cb = view.findViewById<MaterialCheckBox>(R.id.good_cb)
                val image_iv = view.findViewById<ImageView>(R.id.image_iv)
                val good_name = view.findViewById<TextView>(R.id.good_name)
                val now_price_tv = view.findViewById<TextView>(R.id.now_price_tv)
                val old_price_tv = view.findViewById<TextView>(R.id.old_price_tv)
                val number_view = view.findViewById<NumberView>(R.id.number_view).apply {
                    valueChangeListener = numberChangeListener
                    setValue(it.good_num)
                    setDataId(it.cart_id)
                }

                Glide.with(context).load(goodData.image).apply(options).into(image_iv)
                good_cb.text = goodData.title
                good_name.text = goodData.title

                val nowPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.price)
                now_price_tv.text = nowPrice

                val oldPrice: String = StringUtils.getString(R.string.price_unit_format, goodData.original_price)
                val oldBuilder = SpanUtils().append(oldPrice).setStrikethrough().create()
                old_price_tv.text = oldBuilder
                container_ll.addView(view)
            }

        }
    }
}