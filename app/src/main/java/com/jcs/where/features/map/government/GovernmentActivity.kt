package com.jcs.where.features.map.government

import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.map.MechanismAdapter
import com.jcs.where.view.empty.EmptyView

/**
 * Created by Wangsw  2021/8/24 17:04.
 *
 */
class GovernmentActivity : BaseMvpActivity<GovernmentPresenter>(), GovernmentView {


    override fun getLayoutId() = R.layout.activity_government

    override fun initView() {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.white))


    }

    override fun initData() {
        presenter = GovernmentPresenter(this)
    }

    override fun bindListener() {

    }
}