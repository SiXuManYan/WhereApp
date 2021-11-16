package com.jcs.where.features.collection.city

import android.graphics.Color
import android.view.View
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_refresh_list.*

/**
 * Created by Wangsw  2021/11/16 16:13.
 * 同城收藏
 */
class SameCityFragment:BaseMvpFragment<SameCityPresenter>(),SameCityView  {


    override fun getLayoutId() = R.layout.fragment_refresh_list

    override fun initView(view: View) {
        swipe_layout.setBackgroundColor(Color.WHITE)



    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun bindListener() {
        TODO("Not yet implemented")
    }

}