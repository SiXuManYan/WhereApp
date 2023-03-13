package com.jiechengsheng.city.features.job.company.info

import android.graphics.Color
import com.blankj.utilcode.util.BarUtils
import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseActivity
import com.jiechengsheng.city.utils.Constant
import kotlinx.android.synthetic.main.activity_company_info.*

/**
 * Created by Wangsw  2022/11/2 17:02.
 *
 */
class CompanyInfoActivity : BaseActivity() {


    var introduce: String? = ""

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_company_info

    override fun initView() {
        BarUtils.setStatusBarColor(this,Color.WHITE)
        introduce = intent.getStringExtra(Constant.PARAM_DATA)
        company_introduce_tv.text = introduce
    }

    override fun initData() = Unit

    override fun bindListener() = Unit
}