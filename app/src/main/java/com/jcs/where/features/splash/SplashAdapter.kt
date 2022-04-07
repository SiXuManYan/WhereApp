package com.jcs.where.features.splash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.jcs.where.R
import com.jcs.where.utils.LocalLanguageUtil

/**
 * Created by Wangsw  2021/3/18 16:42.
 */
class SplashAdapter : PagerAdapter() {

    private lateinit var bgIv: ImageView


    override fun getCount() = 4

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(container.context).inflate(R.layout.item_splash, container, false)
        bgIv = view.findViewById(R.id.bg_iv)

        initView(position, container.context)
        container.addView(view)
        return view
    }

    private fun initView(position: Int, context: Context) {

        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(context)
        if (languageLocale.language == "zh") {
            when (position) {
                0 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_0_cn)
                1 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_1_cn)
                2 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_2_cn)
                3 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_3_cn)
            }
        } else {
            when (position) {
                0 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_0_en)
                1 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_1_en)
                2 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_2_en)
                3 -> bgIv.setImageResource(R.mipmap.ic_splash_bg_3_en)
            }
        }


    }
}