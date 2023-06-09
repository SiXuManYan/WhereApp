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

/**
 * Created by Wangsw  2021/4/8 10:41.
 */
class NumberView2 : LinearLayout {

    lateinit var cut_iv: ImageView
    lateinit var add_iv: ImageView
    lateinit var value_tv: TextView

    var goodNum = 0
    var MIN_GOOD_NUM = 0
    var MAX_GOOD_NUM = Int.MAX_VALUE

    var alwaysEnableCut = false
    var hideValueWithMin = false


    @DrawableRes
    var cutResIdCommon: Int = 0

    @DrawableRes
    var cutResIdMin: Int = 0

    @DrawableRes
    var addResIdCommon: Int = 0

    @DrawableRes
    var addResIdMax: Int = 0


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


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView()
    }

    public interface OnValueChangeListener {

        fun onNumberChange(goodNum: Int, isAdd: Boolean)
    }


    private fun initView() {

        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number_2, this, true)
        cut_iv = view.findViewById(R.id.cut_iv)
        add_iv = view.findViewById(R.id.add_iv)
        value_tv = view.findViewById(R.id.value_tv)

        cut_iv.setOnClickListener {
            goodNum -= 1
            updateCutButtonView()

            if (addResIdCommon != 0) add_iv.setImageResource(addResIdCommon)

            VibrateUtils.vibrate(10)
            value_tv.text = goodNum.toString()
            valueChangeListener?.onNumberChange(goodNum, false)
            cut_iv.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed({
                cut_iv.isClickable = true
            }, 500)
        }
        add_iv.setOnClickListener {
            goodNum += 1
            updateAddButtonView()
            if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)

            cut_iv.visibility = View.VISIBLE
            value_tv.visibility = View.VISIBLE
            VibrateUtils.vibrate(10)
            value_tv.text = goodNum.toString()
            valueChangeListener?.onNumberChange(goodNum, true)
            add_iv.isClickable = false

            Handler(Looper.myLooper()!!).postDelayed({
                add_iv.isClickable = true
            }, 500)
        }
    }

    private fun updateCutButtonView() {
        if (goodNum <= MIN_GOOD_NUM) {
            goodNum = MIN_GOOD_NUM

            if (!alwaysEnableCut) {
                cut_iv.visibility = INVISIBLE
            }
            if (hideValueWithMin) {
                value_tv.visibility = INVISIBLE
            } else {
                value_tv.visibility = VISIBLE
            }
            if (cutResIdMin != 0) cut_iv.setImageResource(cutResIdMin)
        } else {
            cut_iv.visibility = VISIBLE
            value_tv.visibility = VISIBLE

            if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)
        }
    }

    private fun updateAddButtonView() {
        if (goodNum >= MAX_GOOD_NUM) {
            goodNum = MAX_GOOD_NUM

            if (addResIdMax != 0) add_iv.setImageResource(addResIdMax)
        } else {
            if (addResIdCommon != 0) {
                add_iv.setImageResource(addResIdCommon)
            }
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

    fun updateNumberJudgeMinAndValue(count: Int) {
        updateNumber(count)
        if (goodNum > MIN_GOOD_NUM) {
            cut_iv.visibility = View.VISIBLE
            value_tv.visibility = View.VISIBLE

        } else {
            cut_iv.visibility = View.INVISIBLE
            value_tv.visibility = View.INVISIBLE
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

//        if (goodNum > MIN_GOOD_NUM) {
//            if (cutResIdCommon != 0) cut_iv.setImageResource(cutResIdCommon)
//        } else {
//            if (cutResIdMin != 0) cut_iv.setImageResource(cutResIdMin)
//        }
        updateCutButtonView()

        updateAddButtonView()


    }

    /**
     * 展示删除
     */
    fun alwaysEnableCut(tag: Boolean) {
        alwaysEnableCut = tag
        cut_iv.visibility = View.VISIBLE
    }

    fun checkMax() {
        if (MAX_GOOD_NUM <= 0) {
            cut_iv.isClickable = false
            add_iv.isClickable = false
        } else {
            cut_iv.isClickable = true
            add_iv.isClickable = true
        }
    }


}