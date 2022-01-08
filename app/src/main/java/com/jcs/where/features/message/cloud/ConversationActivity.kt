package com.jcs.where.features.message.cloud

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.jcs.where.R
import com.jcs.where.utils.Constant
import io.rong.imkit.fragment.ConversationFragment
import kotlinx.android.synthetic.main.activity_conversation.*

/**
 * Created by Wangsw  2021/2/24 10:49.
 * 融云会话页面
 */
class ConversationActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_conversation)

        // title
        val title = intent.data!!.getQueryParameter("title")

        chat_title_tv.text = title

        intent.extras?.let {
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

        BarUtils.setStatusBarColor(this,ColorUtils.getColor(R.color.white))
        BarUtils.setStatusBarLightMode(this,true)

        // 添加会话界面
        val conversationFragment = ConversationFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, conversationFragment)
        transaction.commit()
    }
}