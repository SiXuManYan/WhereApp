package com.jcs.where.features.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.jcs.where.R
import com.jcs.where.base.BaseActivity
import com.jcs.where.features.web.WebViewActivity
import com.jcs.where.utils.Constant
import com.jcs.where.view.WebLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import kotlinx.android.synthetic.main.activtiy_webview.*

class WebViewActivity : BaseActivity() {

    private var url = ""
    private var isFromLogistics = false
    private lateinit var mAgentWeb: AgentWeb
    private lateinit var mLinearLayout: FrameLayout


    override fun isStatusDark() = true


    companion object {

        fun navigation(context: Context, url: String?,isFromLogistics :Boolean? = false) {

            val bundle = Bundle().apply {
                putString(Constant.PARAM_URL, url)
                isFromLogistics?.let {
                    putBoolean(Constant.PARAM_TYPE ,it)
                }
            }

            val intent = Intent(context, WebViewActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtras(bundle)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() =  R.layout.activtiy_webview

    override fun initView() {

        intent.extras?.let {
            url = it.getString(Constant.PARAM_URL, "")
            isFromLogistics = it.getBoolean(Constant.PARAM_TYPE, false)

            if (isFromLogistics) {
                bottom_ll.visibility = View.VISIBLE
                mJcsTitle.setMiddleTitle(R.string.express_details)
            }else {
                bottom_ll.visibility = View.GONE
            }



        }

        mLinearLayout = findViewById(R.id.web_parent)

        initWeb()
        initHint()
    }


    private fun initWeb() {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(mLinearLayout, params)
            .useDefaultIndicator()
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                    if (!isFromLogistics) {
                        mJcsTitle.setMiddleTitle(title)
                    }
                }
            })
            .setWebViewClient(object : WebViewClient() {

            })
            //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebLayout(WebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(url)
    }


    private fun initHint() {


    }

    override fun initData() = Unit


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }


    override fun bindListener() {
        close_hint_tv.setOnClickListener {
            bottom_ll.visibility = View.GONE
        }

    }


}