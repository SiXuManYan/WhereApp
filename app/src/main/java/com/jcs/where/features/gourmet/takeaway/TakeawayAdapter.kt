package com.jcs.where.features.gourmet.takeaway

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.dish.DishResponse
import com.jcs.where.api.response.gourmet.dish.DishTakeawayResponse
import com.jcs.where.utils.BusinessUtils
import com.jcs.where.utils.GlideUtil
import com.jcs.where.widget.NumberView2

/**
 * Created by Wangsw  2021/4/25 14:33.
 * 外卖菜品列表
 */
class TakeawayAdapter : BaseQuickAdapter<DishResponse, BaseViewHolder>(R.layout.item_dish_takeaway) {


    var onSelectCountChange: OnSelectCountChange? = null


    var minGoodNum = 0

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

        holder.setText(R.id.dish_name_tv, item.title)
        holder.setText(R.id.sales_tv, StringUtils.getString(R.string.sale_format, item.sale_num))
        holder.setText(R.id.now_price_tv, StringUtils.getString(R.string.price_unit_format, item.price.toPlainString()))
        val old_price_tv = holder.getView<TextView>(R.id.old_price_tv)
        val dish_iv = holder.getView<ImageView>(R.id.dish_iv)

        val oldPrice = StringUtils.getString(R.string.price_unit_format, item.original_price.toPlainString())
        val builder = SpanUtils().append(oldPrice).setStrikethrough().create()
        old_price_tv.text = builder

        GlideUtil.load(context, item.image, dish_iv, 4)


        val number_view = holder.getView<NumberView2>(R.id.number_view).apply {
            alwaysEnableCut = false
            MIN_GOOD_NUM = minGoodNum
            MAX_GOOD_NUM = BusinessUtils.getSafeStock(item.inventory)
            cut_iv.setImageResource(R.mipmap.ic_cut_blue_transparent)
            add_iv.setImageResource(R.mipmap.ic_add_blue)
            updateNumberJudgeMin(item.nativeSelectCount)
            valueChangeListener = object : NumberView2.OnValueChangeListener {
                override fun onNumberChange(goodNum: Int, isAdd: Boolean) {
                    item.nativeSelectCount = goodNum
                    onSelectCountChange?.selectCountChange(goodNum , item.id)
                }

            }
        }








    }




}