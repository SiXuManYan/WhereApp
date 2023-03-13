package com.jiechengsheng.city.view.empty

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jiechengsheng.city.R

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
        hideEmptyContainer()
    }

    fun setEmptyMessage(@StringRes emptyMessage: Int) {
        empty_message_tv.setText(emptyMessage)
        empty_message_tv.visibility = View.VISIBLE
        hideEmptyContainer()
    }

    fun setEmptyMessage(emptyMessage: String) {
        empty_message_tv.text = emptyMessage
        empty_message_tv.visibility = View.VISIBLE
        hideEmptyContainer()
    }

    fun setEmptyHint(@StringRes hint: Int) {
        empty_hint_tv.setText(hint)
        empty_hint_tv.visibility = VISIBLE
        hideEmptyContainer()
    }


    fun setEmptyHint(hint: String) {
        empty_hint_tv.setText(hint)
        empty_hint_tv.visibility = VISIBLE
        hideEmptyContainer()
    }

    fun setEmptyActionText(@StringRes text: Int) {
        action_tv.setText(text)
        action_tv.visibility = VISIBLE
        hideEmptyContainer()
    }

    fun setEmptyActionText(text: String) {
        action_tv.setText(text)
        action_tv.visibility = VISIBLE
        hideEmptyContainer()
    }


    /**
     * 网络错误
     */
    fun showNetworkError(onClickListener: OnClickListener? = null) {
        showEmptyContainer()
        empty_iv.setImageResource(R.mipmap.ic_empty_net)
        empty_iv.visibility = View.VISIBLE
        empty_message_tv.setText(R.string.empty_network_error)
        empty_message_tv.visibility = View.VISIBLE

        empty_hint_tv.visibility = View.GONE

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
     */
    fun showEmptyDefault() {
        setEmptyImage(R.mipmap.ic_empty_card_coupon)
        setEmptyHint(R.string.empty_data_default)
        hideEmptyContainer()
    }

    fun showEmptyNothing() {
        showEmptyContainer()
        empty_iv.visibility = View.INVISIBLE
        empty_message_tv.visibility = GONE
        empty_hint_tv.visibility = GONE
        action_tv.visibility = GONE
    }

    fun initEmpty(
        @DrawableRes imageId: Int,
        @StringRes emptyMessage: Int? = null,
        @StringRes emptyHint: Int? = null,
        @StringRes actionString: Int? = null,
        listener: OnClickListener? = null,
    ) {
        empty_iv.setImageResource(imageId)
        emptyMessage?.let {
            empty_message_tv.setText(it)
            empty_message_tv.visibility = View.VISIBLE
        }
        emptyHint?.let {
            empty_hint_tv.setText(it)
            empty_hint_tv.visibility = View.VISIBLE
        }
        actionString?.let {
            action_tv.setText(it)
            action_tv.visibility = View.VISIBLE
        }
        action_tv.setOnClickListener(listener)
        hideEmptyContainer()
    }


    fun showEmptyContainer() {
        parent_ll.visibility = VISIBLE
    }

     fun hideEmptyContainer() {
        parent_ll.visibility = GONE
    }


     fun showEmptyLoading() {
        empty_iv.setImageResource(R.mipmap.ic_empty_card_coupon)
        empty_message_tv.visibility = View.INVISIBLE
        empty_hint_tv.visibility = View.VISIBLE
        empty_hint_tv.setText(R.string.empty_view_default_message)
        action_tv.visibility = View.GONE

    }

}