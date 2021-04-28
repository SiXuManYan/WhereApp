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
import com.blankj.utilcode.util.VibrateUtils
import com.jcs.where.R

/**
 * Created by Wangsw  2021/4/8 10:41.
 */
class NumberView2 : LinearLayout {

    private lateinit var cut_iv: ImageView
    private lateinit var add_iv: ImageView
    private lateinit var value_tv: TextView
    var goodNum = 0


    var valueChangeListener: OnValueChangeListener? = null


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

    public interface OnValueChangeListener {

        fun onNumberChange(goodNum: Int)
    }

    private fun initView() {

        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number_2, this, true)
        cut_iv = view.findViewById(R.id.cut_iv)
        add_iv = view.findViewById(R.id.add_iv)
        value_tv = view.findViewById(R.id.value_tv)

        cut_iv.setOnClickListener {
            goodNum -= 1

            if (goodNum <= 0) {
                goodNum = 0
                cut_iv.visibility = View.INVISIBLE
                value_tv.visibility = View.INVISIBLE
            } else {
                cut_iv.visibility = View.VISIBLE
                value_tv.visibility = View.VISIBLE
            }
            VibrateUtils.vibrate(10)

            value_tv.text = goodNum.toString()
            valueChangeListener?.onNumberChange(goodNum)
            cut_iv.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed({
                cut_iv.isClickable = true
            }, 500)
        }
        add_iv.setOnClickListener {

            goodNum += 1
            cut_iv.visibility = View.VISIBLE
            value_tv.visibility = View.VISIBLE
            VibrateUtils.vibrate(10)
            value_tv.text = goodNum.toString()
            valueChangeListener?.onNumberChange(goodNum)
            add_iv.isClickable = false

            Handler(Looper.myLooper()!!).postDelayed({
                add_iv.isClickable = true
            }, 500)
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
        if (goodNum > 0) {
            cut_iv.visibility = View.VISIBLE
            value_tv.visibility = View.VISIBLE
        } else {
            cut_iv.visibility = View.INVISIBLE
            value_tv.visibility = View.INVISIBLE
        }
        value_tv.text = goodNum.toString()
    }


}