package com.jiechengsheng.city.features.job.report

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.job.Report
import com.jiechengsheng.city.api.response.job.ReportRequest
import java.util.ArrayList

/**
 * Created by Wangsw  2022/10/27 17:38.
 *
 */
interface ReportView : BaseMvpView {
    fun submitSuccess()
    fun bindReason(response: ArrayList<Report>)

}

class ReportPresenter(private var view: ReportView) : BaseMvpPresenter(view) {

    fun getReportReason(){
        requestApi(mRetrofit.reportReason(),object :BaseMvpObserver<ArrayList<Report>>(view){
            override fun onSuccess(response: ArrayList<Report>) {
                view.bindReason(response)
            }

        })
    }


    fun report(jobId: Int, reportTitleId: Int) {
        val apply = ReportRequest().apply {
            job_id = jobId
            report_title_id = reportTitleId
        }

        requestApi(mRetrofit.reportJob(apply),object :BaseMvpObserver<JsonElement>(view){
            override fun onSuccess(response: JsonElement) {
                view.submitSuccess()
            }
        })
    }

}