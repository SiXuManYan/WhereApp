package com.jiechengsheng.city.mine.about

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.jiechengsheng.city.BuildConfig
import com.jiechengsheng.city.R
import com.jiechengsheng.city.api.response.feedback.About
import com.jiechengsheng.city.base.mvp.BaseMvpActivity
import com.jiechengsheng.city.features.web.WebViewActivity
import com.jiechengsheng.city.utils.FeaturesUtil
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by Wangsw  2021/2/3 16:48.
 */
class AboutActivity : BaseMvpActivity<AboutPresenter>(), AboutView {


    private lateinit var userAgreementTv: TextView
    private lateinit var privacyPolicyTv: TextView
    private lateinit var disclaimerTv: TextView

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_about

    override fun initView() {

        val buffer = StringBuffer(BuildConfig.VERSION_NAME)
        if (BuildConfig.FLAVOR == "dev") {
            buffer.append("_测试服务器")
        }
        version_tv.text = getString(R.string.version_format, buffer.toString())


        userAgreementTv = findViewById(R.id.user_agreement_tv)
        privacyPolicyTv = findViewById(R.id.privacy_policy_tv)
        disclaimerTv = findViewById(R.id.disclaimer_tv)

        handleTerms()


    }


    /**
     * 条款声明
     */
    private fun handleTerms() {

        // 用户协议
        val userAgreement = FeaturesUtil.getUserAgreement()




        SpanUtils.with(userAgreementTv).append(getString(R.string.office_address_home_page_1))
            .append(userAgreement)
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) =
                    WebViewActivity.navigation(this@AboutActivity, userAgreement)

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_377BFF)
                    ds.isUnderlineText = false
                }
            })
            .create()

        // 隐私政策
        val privacyPolicy = FeaturesUtil.getPrivacyPolicy()

        SpanUtils.with(privacyPolicyTv).append(getString(R.string.office_address_home_page_1))
            .append(privacyPolicy)
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) =
                    WebViewActivity.navigation(this@AboutActivity, privacyPolicy)

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_377BFF)
                    ds.isUnderlineText = false
                }
            })
            .create()


        // 免责声明
        val disclaimer = FeaturesUtil.getDisclaimer()

        SpanUtils.with(disclaimerTv).append(getString(R.string.office_address_home_page_1))
            .append(disclaimer)
            .setClickSpan(object : ClickableSpan() {
                override fun onClick(widget: View) =
                    WebViewActivity.navigation(this@AboutActivity, disclaimer)

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_377BFF)
                    ds.isUnderlineText = false
                }
            })
            .create()


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