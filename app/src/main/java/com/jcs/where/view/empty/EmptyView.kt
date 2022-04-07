package com.jcs.where.view.empty

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jcs.where.R

/**
 * Created by Wangsw  2021/3/11 10:56.
 * 列表缺省页
 */
class EmptyView : LinearLayout {

    lateinit var empty_iv: ImageView
    lateinit var empty_message_tv: TextView
    lateinit var empty_hint_tv: TextView

    lateinit var action_tv: TextView
    lateinit var parent_ll: LinearLayout

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_empty, this, true)
        empty_iv = view.findViewById(R.id.empty_iv)
        empty_message_tv = view.findViewById(R.id.empty_message_tv)
        empty_hint_tv = view.findViewById(R.id.empty_hint_tv)
        action_tv = view.findViewById(R.id.action_tv)
        parent_ll = view.findViewById(R.id.parent_ll)
    }

    fun setEmptyImage(@DrawableRes imageId: Int) {
        empty_iv.setImageResource(imageId)
    }

    fun setEmptyMessage(@StringRes emptyMessage: Int) {
        empty_message_tv.setText(emptyMessage)
    }

    fun setEmptyMessage(emptyMessage: String) {
        empty_message_tv.setText(emptyMessage)
    }

    fun setEmptyHint(@StringRes hint: Int) {
        empty_hint_tv.setText(hint)
        empty_hint_tv.visibility = VISIBLE
    }


    fun setEmptyHint(hint: String) {
        empty_hint_tv.setText(hint)
        empty_hint_tv.visibility = VISIBLE
    }

    fun setEmptyActionText(@StringRes text: Int) {
        action_tv.setText(text)
        action_tv.visibility = VISIBLE
    }

    fun setEmptyActionText(text: String) {
        action_tv.setText(text)
        action_tv.visibility = VISIBLE
    }



    /**
     * 网络错误
     */
    fun showNetworkError(onClickListener: OnClickListener? = null) {
        empty_iv.setImageResource(R.mipmap.ic_empty_network_error)
        empty_message_tv.setText(R.string.empty_network_error)
        empty_hint_tv.setText(R.string.empty_network_error_hint)

        empty_message_tv.visibility = View.VISIBLE
        empty_hint_tv.visibility = View.VISIBLE

        onClickListener?.let {
            action_tv.setText(R.string.empty_network_error_action)
            action_tv.visibility = View.VISIBLE
            action_tv.setOnClickListener(it)
        }

    }




    fun setEmptyActionOnClickListener(listener: OnClickListener?) {
        action_tv.setOnClickListener(listener)
    }

    /**
     * 设置空布局
     *
     * @param imageId      空数据图片
     * @param emptyMessage 提示文案
     */
    fun showEmpty(@DrawableRes imageId: Int, @StringRes emptyMessage: Int) {
        setEmptyImage(imageId)
        setEmptyMessage(emptyMessage)
    }

    /**
     * 设置空布局
     */
    fun showEmptyDefault() {
        setEmptyImage(R.mipmap.ic_empty_earth)
        setEmptyMessage(R.string.empty_data_default)
    }

    fun showEmptyNothing() {
        empty_iv.imageAlpha = 0
        empty_message_tv.visibility = GONE
        empty_hint_tv.visibility = GONE
        action_tv.visibility = GONE
    }

    fun initEmpty(
        @DrawableRes imageId: Int,
        @StringRes emptyMessage: Int,
        @StringRes emptyHint: Int?=null,
        @StringRes actionString: Int? = null,
        listener: OnClickListener?  = null
    ) {
        parent_ll.visibility = GONE
        empty_iv.setImageResource(imageId)
        empty_message_tv.setText(emptyMessage)
        emptyHint?.let {
            empty_hint_tv.setText(it)
        }
        actionString?.let {
            action_tv.setText(it)
        }
        action_tv.setOnClickListener(listener)
    }

    fun showEmptyContainer() {
        parent_ll!!.visibility = VISIBLE
        empty_hint_tv!!.visibility = VISIBLE
    }

    fun hideEmptyContainer() {
        parent_ll!!.visibility = GONE
    }


}