package com.jcs.where.utils.product

import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import com.blankj.utilcode.util.VibrateUtils

/**
 * Created by Wangsw  2021/7/7 10:39.
 *
 */
object ProductUtils {

    /**
     * 非空校验，提供震动和抖动反馈
     */
    fun checkEditEmptyWithVibrate(vararg args: EditText): Boolean {

        args.forEach {
            if (it.text.toString().trim().isEmpty()) {
                VibrateUtils.vibrate(10)
                it.startAnimation(getShakeAnimation(2))
                return false
            }
        }
        return true
    }

    /**
     * 非空校验，提供震动和抖动反馈
     */
    fun checkEditEmpty(vararg args: EditText): Boolean {
        args.forEach {
            if (it.text.toString().trim().isEmpty()) {
                return false
            }
        }
        return true
    }



    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return 动画
     */
    fun getShakeAnimation(counts: Int): Animation {
        val translateAnimation: Animation = TranslateAnimation(0f, 10f, 0f, 0f)
        translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
        translateAnimation.duration = 300
        return translateAnimation
    }

}