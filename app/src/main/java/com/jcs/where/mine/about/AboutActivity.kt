package com.jcs.where.mine.about

import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.SpanUtils
import com.google.gson.Gson
import com.jcs.where.BuildConfig
import com.jcs.where.R
import com.jcs.where.api.response.feedback.About
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.storage.entity.User
import com.jcs.where.utils.BusinessUtils.getUmengAppChannel
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by Wangsw  2021/2/3 16:48.
 */
class AboutActivity : BaseMvpActivity<AboutPresenter>(), AboutView {

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_about

    override fun initView() {

        val buffer = StringBuffer(BuildConfig.VERSION_NAME)
        if (BuildConfig.FLAVOR == "dev") {
            buffer.append("_测试服务器")
        }
        buffer.append("For Special Tablet")
        version_tv.text = getString(R.string.version_format, buffer.toString())

        val spanUtils = SpanUtils()
        val builder = spanUtils.append(getString(R.string.office_address_home_page_1))
            .append(getString(R.string.office_address_home_page_2))
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) =
                    WebViewActivity.goTo(this@AboutActivity, getString(R.string.office_address_home_page_2))

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_377BFF)
                    ds.isUnderlineText = false
                }
            })
            .create()

        home_page_tv.movementMethod = LinkMovementMethod.getInstance()
        home_page_tv.text = builder




        if (BuildConfig.FLAVOR == "dev" && BuildConfig.VERSION_NAME.contains("alpha")) {
            findViewById<View>(R.id.debug_into_ll).visibility = View.VISIBLE
            val user_id_tv = findViewById<TextView>(R.id.user_id_tv)
            val user_phone_tv = findViewById<TextView>(R.id.user_phone_tv)
            val user_all_tv = findViewById<TextView>(R.id.user_all_tv)
            val rong_uuid_tv = findViewById<TextView>(R.id.rong_uuid_tv)
            val rong_token_tv = findViewById<TextView>(R.id.rong_token_tv)
            val push_id_tv = findViewById<TextView>(R.id.push_id_tv)
            val umeng_channel_tv = findViewById<TextView>(R.id.umeng_channel_tv)
            if (User.isLogon()) {
                val instance = User.getInstance()
                user_id_tv.append(instance.id.toString())
                user_phone_tv.append(instance.phone)
                if (instance.rongData != null) {
                    rong_uuid_tv.append(instance.rongData.uuid)
                    rong_token_tv.append(instance.rongData.token)
                }
                val gson = Gson()
                user_all_tv.append(""" ${gson.toJson(instance)} """.trimIndent())
            } else {
                user_id_tv.append("未登录")
            }
            val registrationID = JPushInterface.getRegistrationID(this)
            push_id_tv.append(registrationID)
            umeng_channel_tv.append(getUmengAppChannel())
        }


    }

    override fun initData() {
        presenter = AboutPresenter(this)
        presenter.getMessage()
    }

    override fun bindMessage(response: About) {
        call_us_tv.append("\r\n" + response.contact_tell)
        message_us_tv.append("\r\n" + response.contact_facebook)
        email_us_tv.append("\r\n" + response.contact_email)
    }


    override fun bindListener() {

    }
}