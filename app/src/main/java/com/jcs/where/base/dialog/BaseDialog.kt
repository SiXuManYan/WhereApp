package com.jcs.where.base.dialog

import android.app.Dialog
import android.content.Context
import com.jcs.where.R


/**
 * 对话框基类
 */
abstract class BaseDialog @JvmOverloads constructor(context: Context, style: Int = R.style.Dialog, animal: Boolean = false) : Dialog(context, style) {


    init {
        if (animal) {
            window!!.setWindowAnimations(R.style.DialogFadeAnimation)
        }
        initView()
    }



    /**
     * 初始化控件
     */
    private fun initView() {
        val view = layoutInflater.inflate(getContentId(), null)
        setContentView(view)
    }

    abstract fun getContentId(): Int


}
