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

    /** 职位列表 */
    fun bindJobList(toMutableList: MutableList<Job>, lastPage: Boolean){}

    /** 收藏职位列表 */
    fun bindJobCollectionList(toMutableList: MutableList<Job>, lastPage: Boolean){}
}

class JobHomePresenter(private var view: JobHomeView) : BaseMvpPresenter(view) {


    /** 获取职位列表 */
    fun getJobList(page: Int, search: String? = null) {
        requestApi(mRetrofit.jobList(page, search), object : BaseMvpObserver<PageResponse<Job>>(view) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindJobList(data.toMutableList(), isLastPage)
            }
        })
    }



    /** 获取收藏职位列表 */
    fun getJobCollectionList(page: Int) {
        requestApi(mRetrofit.jobCollectionList(page), object : BaseMvpObserver<PageResponse<Job>>(view,page) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindJobCollectionList(data.toMutableList(), isLastPage)
            }
        })
    }

}
