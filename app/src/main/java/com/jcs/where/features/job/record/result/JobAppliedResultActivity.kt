package com.jcs.where.features.job.record.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.jcs.where.R
import com.jcs.where.api.response.job.Job
import com.jcs.where.base.BaseActivity
import com.jcs.where.utils.Constant
import kotlinx.android.synthetic.main.activity_job_applied_result.*

/**
 * Created by Wangsw  2022/12/19 15:05.
 * 申请结果
 */
class JobAppliedResultActivity  :BaseActivity(){

    /**
     *  1已申请 2申请失败 3待面试 4面试成功 5面试失败
     *  @see com.jcs.where.api.response.job.Job.status
     */
    private var status = 0

    companion object {

        fun navigation(context: Context, status: Int) {

            val bundle = Bundle().apply {
                putInt(Constant.PARAM_STATUS, status)
            }
            val intent = Intent(context, JobAppliedResultActivity::class.java)
                .putExtras(bundle)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun isStatusDark() = true


    override fun getLayoutId()= R.layout.activity_job_applied_result

    override fun initView() {
        BarUtils.setStatusBarColor(this,Color.WHITE)
        status = intent.getIntExtra(Constant.PARAM_STATUS , 0)

    }

    override fun initData() {
        when (status) {
            Job.STATUS_APPLIED -> {
                status_iv.setImageResource(R.mipmap.ic_status_applied)
                message_tv.setText(R.string.status_applied_message)
                message_tv.setText(R.string.status_applied_hint)
            }
            Job.STATUS_APPLIED_FAILED -> {
                status_iv.setImageResource(R.mipmap.ic_status_applied_failed)
                message_tv.setText(R.string.status_applied_failed_message)
            }
            Job.STATUS_TO_INTERVIEWS -> {
                status_iv.setImageResource(R.mipmap.ic_status_to_interviews)
                message_tv.setText(R.string.status_to_interviews_message)
                message_tv.setText(R.string.status_to_interviews_hint)
            }
            Job.STATUS_INTERVIEWS_SUCCEED -> {
                status_iv.setImageResource(R.mipmap.ic_status_interviews_succeed)
                message_tv.setText(R.string.status_interviews_succeed_message)
                message_tv.setText(R.string.status_interviews_succeed_hint)
            }
            Job.STATUS_INTERVIEWS_FAILED -> {
                status_iv.setImageResource(R.mipmap.ic_status_interviews_failed)
                message_tv.setText(R.string.status_interviews_failed)
                message_tv.setText(R.string.status_interviews_failed_hint)
            }
        }

    }

    override fun bindListener() {

    }
}