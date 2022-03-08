package com.jcs.where.features.mall.buy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.jcs.where.R

/**
 * Created by Wangsw  2022/3/8 16:38.
 *
 */
class MallOrderFooter(context: Context) : LinearLayout(context) {

    init {
        initView()
    }

    private fun initView() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_number_2, this, true)
    }

}