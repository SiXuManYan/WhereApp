package com.jcs.where.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.gourmet.cart.Products

/**
 * Created by Wangsw  2021/4/8 10:41.
 */
class NumberView: LinearLayout {

    private lateinit var cut_iv: ImageView
    private lateinit var add_iv: ImageView
    private lateinit var value_tv: TextView
    private lateinit var products: Products

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


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView()
    }

    fun setData(products: Products) {
        this.products = products
        value_tv.text = products.good_num.toString()
    }

    private fun initView() {

        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number, this, true)
        cut_iv = view.findViewById(R.id.cut_iv)
        add_iv = view.findViewById(R.id.add_iv)
        value_tv = view.findViewById(R.id.value_tv)

        cut_iv.setOnClickListener {
            var goodNum = products.good_num

            if (goodNum > 1) {
                goodNum -= 1
                value_tv.text = goodNum.toString()
                products.good_num = goodNum
            } else {
                ToastUtils.showShort("不能再减少了")
            }
            valueChangeListener?.onCutClick(products.cart_id);
            cut_iv.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed({
                cut_iv.isClickable = true
            }, 500)
        }
        add_iv.setOnClickListener {

            var goodNum = products.good_num

            goodNum += 1
            value_tv.text = goodNum.toString()
            products.good_num = goodNum

            valueChangeListener?.onAddClick(products.cart_id)

            Handler(Looper.myLooper()!!).postDelayed({
                add_iv.isClickable = true
            }, 500)
        }
    }

    /**
     * 数值发生改变
     */
    public interface OnValueChangerListener {
        fun onCutClick(dataId: Int)

        fun onAddClick(dataId: Int)

    }

}