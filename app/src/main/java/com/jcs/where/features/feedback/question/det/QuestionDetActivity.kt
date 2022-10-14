package com.jcs.where.features.feedback.question.det

import android.annotation.SuppressLint
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jcs.where.R
import com.jcs.where.api.response.feedback.FeedbackQuestionTab
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.feedback.form.FeedBackPostActivity
import com.jcs.where.features.feedback.question.QuestionPresenter
import com.jcs.where.features.feedback.question.QuestionView
import com.jcs.where.utils.Constant
import com.jcs.where.view.WebLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import kotlinx.android.synthetic.main.activity_feedback_question_detail.*

/**
 * Created by Wangsw  2022/10/13 10:50.
 * 问题详情
 */
class QuestionDetActivity : BaseMvpActivity<QuestionPresenter>(), QuestionView, OnItemClickListener {

    private var url = ""

    private var data = ArrayList<FeedbackQuestionTab>()
    private lateinit var mAdapter: QuestionDetAdapter

    private lateinit var webParent: LinearLayout
    private lateinit var mAgentWeb: AgentWeb

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feedback_question_detail

    override fun initView() {
        initWeb()
        initBottom()
    }

    private fun initWeb() {
        intent.extras?.let {
            url = it.getString(Constant.PARAM_URL, "")
        }
        webParent = findViewById(R.id.web_ll)

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(webParent, params)
            .useDefaultIndicator()
            .setWebChromeClient(object : WebChromeClient() {

            })
            .setWebViewClient(object : WebViewClient() {

            })
            .setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
            .setWebLayout(WebLayout(this))
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            .createAgentWeb()
            .ready()
            .go(url)

    }

    private fun initBottom() {

        // 按钮
        mAdapter = QuestionDetAdapter().apply {
            setOnItemClickListener(this@QuestionDetActivity)
        }
        handle_rv.apply {
            val gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            layoutManager = gridLayoutManager
            adapter = mAdapter
        }
        data.add(FeedbackQuestionTab().apply { id = 0 })
        data.add(FeedbackQuestionTab().apply { id = 1 })
        mAdapter.setNewInstance(data)

        // 意见反馈
        val span = SpanUtils()
            .append(getString(R.string.feed_back_question))
            .append(getString(R.string.feed_back_bottom))
            .setClickSpan(object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = getColor(R.color.blue_4C9EF2)
                    ds.isUnderlineText = false
                }

                override fun onClick(widget: View) {
                   startActivity(FeedBackPostActivity::class.java)
                }
            }).create()
        feedback_tv.text = span

    }

    override fun initData() {
        presenter = QuestionPresenter(this)


    }

    override fun bindListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        val tab = mAdapter.data[position]
        if (!tab.nativeCanClick) {
            return
        }

        mAdapter.data.forEachIndexed { index, item ->
            item.nativeSelected = index == position
            item.nativeCanClick = false
        }
        mAdapter.notifyDataSetChanged()
        ToastUtils.showShort(R.string.question_click_hint)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
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
        //mAgentWeb.destroy();
        mAgentWeb.webLifeCycle.onDestroy()
    }


}