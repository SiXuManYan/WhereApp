package com.jiechengsheng.city.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.SizeUtils
import com.jiechengsheng.city.R

/**
 * Created by Wangsw  2021/2/23 10:40.
 */
class MessageView : LinearLayout {

    private var message_count_tv: TextView? = null
    private var message_iv: ImageView? = null
    private var message_container_rl: RelativeLayout? = null
    private var currentMessageCount = 0

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context,
        attrs,
        defStyleAttr,
        defStyleRes) {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.widget_message_count, this, true)
        message_count_tv = view.findViewById(R.id.message_count_tv)
        message_iv = view.findViewById(R.id.message_iv)
        message_container_rl = view.findViewById(R.id.message_container_rl)
    }

    /**
     * 设置消息数量
     *
     * @param count
     */
    fun setMessageCount(count: Int) {
        if (count > 0) {
            message_count_tv!!.visibility = VISIBLE
            if (count > 99) {
                message_count_tv!!.text = "99+"
            } else {
                message_count_tv!!.text = count.toString()
            }
        } else {
            message_count_tv!!.visibility = GONE
        }
        currentMessageCount = count
    }

    fun setMessageImageResource(@DrawableRes resId: Int) {
        message_iv!!.setImageResource(resId)
    }

    fun changeContainerSize(width: Float, height: Float) {
        val layoutParams = message_container_rl!!.layoutParams as ViewGroup.LayoutParams
        layoutParams.width = SizeUtils.dp2px(width)
        layoutParams.height = SizeUtils.dp2px(height)
    }

     fun changeMessageCountSize(width: Float, height: Float) {
        val layoutParams = message_count_tv!!.layoutParams as ViewGroup.LayoutParams
        layoutParams.width = SizeUtils.dp2px(width)
        layoutParams.height = SizeUtils.dp2px(height)
    }




}