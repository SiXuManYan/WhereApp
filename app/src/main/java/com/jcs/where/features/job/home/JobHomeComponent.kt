package com.jcs.where.features.job.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.job.Job

/**
 * Created by Wangsw  2022/9/27 16:04.
 *
 */

interface JobHomeView : BaseMvpView {
    fun bindData(toMutableList: MutableList<Job>, lastPage: Boolean)
}

class JobHomePresenter(private var view: JobHomeView) : BaseMvpPresenter(view) {

    fun getData(page: Int, search: String? = null) {
        requestApi(mRetrofit.jobList(page, search), object : BaseMvpObserver<PageResponse<Job>>(view) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindData(data.toMutableList(), isLastPage)
            }
        })
    }
}
