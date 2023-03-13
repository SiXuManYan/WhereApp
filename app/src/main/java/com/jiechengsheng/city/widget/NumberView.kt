package com.jiechengsheng.city.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.VibrateUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.gourmet.cart.Products

/**
 * Created by Wangsw  2021/4/8 10:41.
 */
class NumberView : LinearLayout {

    lateinit var cut_iv: ImageView
    lateinit var add_iv: ImageView
    lateinit var value_tv: TextView
    lateinit var products: Products

    var goodNum = 0
    var MIN_GOOD_NUM = 0
    var MAX_GOOD_NUM = Int.MAX_VALUE

    var alwaysEnableCut = false


    @DrawableRes
    var cutResIdCommon: Int = 0

    @DrawableRes
    var cutResIdMin: Int = 0

    @DrawableRes
    var addResIdCommon: Int = 0

    @DrawableRes
    var addResIdMax: Int = 0


    var valueChangeListener: OnValueChangerListener? = null


    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView()
    }

    fun setData(products: Products) {
        this.products = products
    }

    private fun initView() {

        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number, this, true)
        cut_iv = view.findViewById(R.id.cut_iv)
        add_iv = view.findViewById(R.id.add_iv)
        value_tv = view.findViewById(R.id.value_tv)

        cut_iv.setOnClickListener {
            goodNum -= 1
            if (goodNum <= MIN_GOOD_NUM) {
                goodNum = MIN_GOOD_NUM

                value_tv.visibility = View.VISIBLE
                if (cutResIdMin != 0) cut_iv.setImageResource(cutResIdMin)
            } else {
                cut_iv.visibility = View.VISIBLE
                value_tv.visibility = View.VISIBLE
                if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)
            }


            if (addResIdCommon != 0) add_iv.setImageResource(addResIdCommon)
            VibrateUtils.vibrate(10)
            value_tv.text = goodNum.toString()

            products.good_num = goodNum
            valueChangeListener?.onNumberChange(products.cart_id, false, goodNum)

            cut_iv.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed({
                cut_iv.isClickable = true
            }, 500)
        }

        add_iv.setOnClickListener {
            goodNum += 1
            if (goodNum >= MAX_GOOD_NUM) {
                goodNum = MAX_GOOD_NUM

                if (addResIdMax != 0) add_iv.setImageResource(addResIdMax)
            } else {
                if (addResIdCommon != 0) {
                    add_iv.setImageResource(addResIdCommon)
                }
            }

            if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)

            cut_iv.visibility = View.VISIBLE
            value_tv.visibility = View.VISIBLE
            VibrateUtils.vibrate(10)
            value_tv.text = goodNum.toString()

            products.good_num = goodNum
            valueChangeListener?.onNumberChange(products.cart_id, true, goodNum)
            add_iv.isClickable = false

            Handler(Looper.myLooper()!!).postDelayed({
                add_iv.isClickable = true
            }, 500)
        }
    }


    /**
     * 更新数量
     */
    fun updateNumberJudgeMin(count: Int) {

        updateNumber(count)
        if (goodNum > MIN_GOOD_NUM) {
            cut_iv.visibility = View.VISIBLE

        } else {
            cut_iv.visibility = View.INVISIBLE
        }
    }

    /**
     * 更新数量
     */
    fun updateNumber(count: Int) {
        goodNum = if (count < 0) {
            0
        } else {
            count
        }
        value_tv.visibility = View.VISIBLE
        value_tv.text = goodNum.toString()

        if (goodNum > MIN_GOOD_NUM) {
            if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)
        } else {
            if (cutResIdMin != 0) cut_iv.setImageResource(cutResIdMin)
        }
    }



    /**
     * 展示删除
     */
    fun alwaysEnableCut(tag: Boolean) {
        alwaysEnableCut = tag
        cut_iv.visibility = View.VISIBLE
    }


    /**
     * 购物车数值发生改变
     */
    public interface OnValueChangerListener {

        /**
         * 购物车数值发生改变
         * @param cartId 购物车id
         * @param isAdd 是否增加
         */
        fun onNumberChange(cartId: Int, isAdd: Boolean, goodNum: Int)
    }

}