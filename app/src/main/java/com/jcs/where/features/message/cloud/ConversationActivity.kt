package com.jcs.where.features.message.cloud

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.utils.Constant
import io.rong.imkit.conversation.ConversationFragment
import kotlinx.android.synthetic.main.activity_conversation.*


/**
 * Created by Wangsw  2021/2/24 10:49.
 * 融云会话聊天页面
 */
class ConversationActivity : BaseActivity() {

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_conversation

    override fun initView() {
        initExtra()
    }

    private fun initExtra() {
        intent.extras?.let {
            chat_title_tv.text = it.getString(Constant.PARAM_TITLE, "")

            val phone = it.getString(Constant.PARAM_PHONE, "")
            if (TextUtils.isEmpty(phone)) {
                chat_tel_iv.visibility = View.GONE
            } else {
                chat_tel_iv.visibility = View.VISIBLE
                chat_tel_iv.setOnClickListener { v: View? ->
                    val i = Intent(Intent.ACTION_DIAL)
                    i.data = Uri.parse("tel:$phone")
                    startActivity(i)
                }
            }
        }


    }


    override fun initData() {
        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()

    }

    override fun bindListener() {

    }
}