package com.jiechengsheng.city.features.comment

import com.jiechengsheng.city.R
import com.jiechengsheng.city.base.BaseActivity
import kotlinx.android.synthetic.main.activity_comment_success.*

/**
 * Created by Wangsw  2021/12/23 15:33.
 * 评价成功
 */
class CommentSuccessActivity : BaseActivity() {

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_comment_success

    override fun initView() {

    }

    override fun onBackPressed() = Unit

    override fun initData() {

    }

    override fun bindListener() {
        finish_tv.setOnClickListener {
            finish()
        }
    }
}