package com.jiechengsheng.city.features.gourmet.cart

import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.cart.Products
import com.jiechengsheng.city.api.response.gourmet.cart.ShoppingCartResponse
import com.jiechengsheng.city.utils.BusinessUtils
import com.jiechengsheng.city.utils.GlideUtil
import com.jiechengsheng.city.widget.NumberView

/**
 * Created by Wangsw  2021/4/8 14:59.
 *
 */
class ShoppingCartChildAdapter : BaseMultiItemQuickAdapter<Products, BaseViewHolder>() {


    init {
        addItemType(ShoppingCartResponse.CONTENT_TYPE_COMMON, R.layout.item_shopping_cart_child)
        addItemType(ShoppingCartResponse.CONTENT_TYPE_COMMIT, R.layout.item_shopping_cart_child_for_commit)
    }

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


        when (holder.itemViewType) {
            ShoppingCartResponse.CONTENT_TYPE_COMMON -> {
                bindTitleImage(holder, products)
                bindPrice(products, holder)
                handleSelected(holder, products)
                handleNumberChange(holder, products)
            }

            ShoppingCartResponse.CONTENT_TYPE_COMMIT -> {
                bindTitleImage(holder, products)
                bindPrice(products, holder)


                bindCount(holder, products)


            }
        }


    }


    /** 标题 图片 */
    private fun bindTitleImage(holder: BaseViewHolder, products: Products) {
        val goodData = products.good_data
        val image_iv = holder.getView<ImageView>(R.id.image_iv)
        val good_name = holder.getView<TextView>(R.id.good_name_tv)
        GlideUtil.load(context, goodData.image, image_iv, 4)
        good_name.text = goodData.title

    }

    /** 价格 */
    private fun bindPrice(products: Products, holder: BaseViewHolder) {

        val goodData = products.good_data
        val nowPriceTv = holder.getView<TextView>(R.id.now_price_tv)
        val oldPriceTv = holder.getView<TextView>(R.id.old_price_tv)

        nowPriceTv.text = StringUtils.getString(R.string.price_unit_format, goodData.price)

        val oldBuilder = SpanUtils().append(StringUtils.getString(R.string.price_unit_format, goodData.original_price.toPlainString()))
            .setStrikethrough().create()
        oldPriceTv.text = oldBuilder
    }

    /** 处理选中 */
    private fun handleSelected(holder: BaseViewHolder, products: Products) {

        val goodCheckedIv = holder.getView<CheckedTextView>(R.id.good_checked_tv)
        goodCheckedIv.isChecked = products.nativeIsSelect
        goodCheckedIv.setOnClickListener {
            val currentIsSelect = products.nativeIsSelect
            products.nativeIsSelect = !currentIsSelect

            if (products.nativeIsSelect) {
                VibrateUtils.vibrate(10)
            }
            goodCheckedIv.isChecked = products.nativeIsSelect
            checkedChangeListener?.onClick(products.nativeIsSelect)

        }
    }

    /** 操作商品数量增减 */
    private fun handleNumberChange(holder: BaseViewHolder, products: Products) {
        holder.getView<NumberView>(R.id.number_view).apply {

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
    }


    /** 展示商品数量 */
    private fun bindCount(holder: BaseViewHolder, products: Products) {
        val countTv = holder.getView<TextView>(R.id.count_tv)
        countTv.text = StringUtils.getString(R.string.count_format, products.good_num)
    }


}