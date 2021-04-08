package com.jcs.where.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R

/**
 * Created by Wangsw  2021/4/8 10:41.
 */
class NumberView(context: Context) : LinearLayout(context) {

    private lateinit var cut_iv: ImageView
    private lateinit var add_iv: ImageView
    private lateinit var value_tv: TextView
    var currentValue = 1
    private var dataId = 0

    var valueChangeListener: OnValueChangerListener? = null

    init {
        initView()
    }

    fun setValue(value: Int) {
        currentValue = value
        value_tv.text = value.toString()
    }

    fun setDataId(id: Int) {
        dataId = id
    }


    private fun initView() {

        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number, this, true)
        cut_iv = view.findViewById(R.id.cut_iv)
        add_iv = view.findViewById(R.id.add_iv)
        value_tv = view.findViewById(R.id.value_tv)

        cut_iv.setOnClickListener {
            if (currentValue > 1) {
                currentValue--
                value_tv.text = currentValue.toString()
            } else {
                ToastUtils.showShort("不能再减少了")
            }
            valueChangeListener?.onCutClick(dataId);
            cut_iv.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed({
                cut_iv.isClickable = true
            }, 500)
        }
        add_iv.setOnClickListener {
            currentValue++
            value_tv.text = currentValue.toString()
            valueChangeListener?.onAddClick(dataId)

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