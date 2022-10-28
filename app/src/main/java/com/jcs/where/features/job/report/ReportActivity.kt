package com.jcs.where.features.job.report

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.Report
import com.jcs.where.base.mvp.BaseMvpActivity
import com.jcs.where.utils.Constant
import com.jcs.where.widget.list.DividerDecoration
import kotlinx.android.synthetic.main.activity_report.*

/**
 * Created by Wangsw  2022/10/27 17:38.
 * 职位举报
 */
class ReportActivity : BaseMvpActivity<ReportPresenter>(), ReportView {


    private var jobId = 0
    private var reportTitleId = 0

    private lateinit var mAdapter: ReportAdapter

    override fun isStatusDark() = true

    override fun getLayoutId() = R.layout.activity_report

    override fun initView() {
        BarUtils.setStatusBarColor(this, Color.WHITE)
        jobId = intent.getIntExtra(Constant.PARAM_ID, 0)
        initContent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initContent() {
        mAdapter = ReportAdapter().apply {
            setOnItemClickListener { _, _, position ->
                val jobReport = mAdapter.data[position]
                reportTitleId = jobReport.id
                mAdapter.data.forEach {
                    it.nativeIsSelected = it.id == jobReport.id
                }
                mAdapter.notifyDataSetChanged()
                submit_tv.isClickable = true
                submit_tv.alpha = 1.0f
            }
        }
        content_rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ReportActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerDecoration(ColorUtils.getColor(R.color.colorPrimary), 1, SizeUtils.dp2px(15f), 0))
        }


    }

    override fun initData() {
        presenter = ReportPresenter(this)
        presenter.getReportReason()
    }

    override fun bindListener() {
        submit_tv.setOnClickListener {
            if (jobId == 0 || reportTitleId == 0) {
                return@setOnClickListener
            }
            presenter.report(jobId, reportTitleId)
        }
    }


    override fun submitSuccess() {
        ToastUtils.showShort(R.string.submit_success)
        finish()
    }


    override fun bindReason(response: ArrayList<Report>) {
        mAdapter.setNewInstance(response)
    }

}