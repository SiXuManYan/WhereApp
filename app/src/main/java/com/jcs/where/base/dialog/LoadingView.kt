package com.jcs.where.base.dialog

import android.content.Context
import android.view.Gravity
import android.view.KeyEvent
import com.jcs.where.R
import pl.droidsonroids.gif.GifImageView


class LoadingView private constructor(context: Context) : BaseDialog(context, R.style.Dialog) {

    init {
        window!!.attributes.gravity = Gravity.CENTER
    }

    override fun getContentId() = R.layout.dialog_loading_view


    class Builder(context: Context?) {

        private var dialog: LoadingView? = null

        init {
            dialog = LoadingView(context!!)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
        }

        /**
         * 设置该对话框能否被Cancel掉，默认可以
         *
         * @param cancelable
         */
        fun setCancelable(cancelable: Boolean): Builder {
            dialog!!.setCancelable(cancelable)
            dialog!!.setCanceledOnTouchOutside(cancelable)
            dialog!!.setOnKeyListener { _, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0 }
            return this
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        fun create(): LoadingView = dialog!!
    }
}