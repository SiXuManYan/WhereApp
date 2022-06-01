package com.jcs.where.features.mall.shop.home.info

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.utils.Constant
import com.jcs.where.utils.GlideUtil
import kotlinx.android.synthetic.main.activity_qualification.*

/**
 * Created by Wangsw  2022/5/31 16:25.
 * 企业资质
 */
class QualificationActivity : BaseActivity() {

    companion object {

        fun navigation(context: Context, imageUrl: String?) {
            val bundle = Bundle().apply {
                putString(Constant.PARAM_IMAGE, imageUrl)
            }
            val intent = Intent(context, QualificationActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_qualification

    override fun initView() {
        val url = intent.getStringExtra(Constant.PARAM_IMAGE)
        GlideUtil.load(this, url, qualification_iv)

    }

    override fun initData() = Unit

    override fun bindListener() = Unit
}