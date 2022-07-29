package com.jcs.where.features.bills.guide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.jcs.where.R
import com.jcs.where.utils.LocalLanguageUtil

/**
 * Created by Wangsw  2022/7/29 9:58.
 *
 */
class BillGuideAdapter : PagerAdapter() {

    private lateinit var bgIv: ImageView

     var itemClickListener: GuideItemClickListener? = null

    override fun getCount() = 2

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(container.context).inflate(R.layout.item_splash_bills, container, false)
        bgIv = view.findViewById(R.id.bg_iv)

        initView(position, container.context)
        container.addView(view)

        view.setOnClickListener {
            itemClickListener?.pagerItemClick(position)
        }
        return view
    }

    private fun initView(position: Int, context: Context) {

        val languageLocale = LocalLanguageUtil.getInstance().getSetLanguageLocale(context)
        if (languageLocale.language == "zh") {
            when (position) {
                0 -> bgIv.setImageResource(R.mipmap.ic_bills_guide_00_all_cn)
                1 -> bgIv.setImageResource(R.mipmap.ic_bills_guide_01_all_cn)
            }
        } else {
            when (position) {
                0 -> bgIv.setImageResource(R.mipmap.ic_bills_guide_00_all_en)
                1 -> bgIv.setImageResource(R.mipmap.ic_bills_guide_01_all_en)

            }
        }


    }

}

interface GuideItemClickListener {
    fun pagerItemClick(position: Int)

}