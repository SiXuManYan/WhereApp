package com.jcs.where.features.feedback.form

import android.content.Intent
import android.graphics.Color
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.ErrorResponse
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.features.store.refund.StoreRefundAdapter
import com.jcs.where.utils.Constant
import com.jcs.where.utils.FeaturesUtil
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_feedback_post.*

/**
 * Created by Wangsw  2022/10/13 16:33.
 * 意见反馈表单
 */
class FeedBackPostActivity : BaseMvpActivity<FeedBackPostPresenter>(), FeedBackPostView {

    private lateinit var mImageAdapter: StoreRefundAdapter


    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_feedback_post

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        initImage()


    }

    private fun initImage() {
        // 相册
        mImageAdapter = StoreRefundAdapter().apply {
            addChildClickViewIds(R.id.delete_iv)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.delete_iv -> mImageAdapter.removeAt(position)
                }
            }
        }

        image_rv.apply {
            adapter = mImageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        content_et.addTextChangedListener (afterTextChanged = {
            val trim = it.toString().trim()
            if (trim.isBlank()) {
                commit_tv.isClickable = false
                commit_tv.alpha = 0.5f
            }else {
                commit_tv.isClickable = true
                commit_tv.alpha = 1.0f
            }

        })

    }

    override fun initData() {
        presenter = FeedBackPostPresenter(this)
    }

    override fun bindListener() {
        select_iv.setOnClickListener {
            val size = mImageAdapter.data.size
            if (size == 4) {
                ToastUtils.showShort(R.string.up_to_4)
                return@setOnClickListener
            }
            val max = 4 - size
            FeaturesUtil.handleMediaSelect(this, Constant.IMG, max)
        }

        commit_tv.setOnClickListener {

            val content = content_et.text.toString().trim()
            val phone = phone_et.text.toString().trim()

            if (content.isBlank()) {
                return@setOnClickListener
            }
            commit_tv.isClickable = false
            if (mImageAdapter.data.isNotEmpty()) {
                presenter.upLoadImage(content, phone, ArrayList(mImageAdapter.data))
                return@setOnClickListener
            }
            presenter.postFeedback(content, phone, null)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val elements = Matisse.obtainPathResult(data)
        elements.forEach {
            mImageAdapter.addData(it)
        }
    }


    override fun commitSuccess() {
        ToastUtils.showShort(R.string.question_click_hint)
        finish()
    }

    override fun onError(errorResponse: ErrorResponse?) {
        super.onError(errorResponse)
        commit_tv.isClickable = true
    }


}