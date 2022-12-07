package com.jcs.where.features.job.home

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.job.FilterData
import com.jcs.where.api.response.job.Job

/**
 * Created by Wangsw  2022/9/27 16:04.
 *
 */

interface JobHomeView : BaseMvpView {

    /** 职位列表 */
    fun bindJobList(toMutableList: MutableList<Job>, lastPage: Boolean) {}

    /** 收藏职位列表 */
    fun bindJobCollectionList(toMutableList: MutableList<Job>, lastPage: Boolean) {}
}

class JobHomePresenter(private var view: JobHomeView) : BaseMvpPresenter(view) {


    private var gson = Gson()

    /** 获取职位列表 */
    fun getJobList(page: Int, search: String? = null, selectedFilterData: FilterData? = null) {

        var salaryType: Int? = null
        var minSalary: String? = null
        var maxSalary: String? = null
        var areaList: String? = null
        var companyType: String? = null
        var educationLevel: String? = null
        var experience: String? = null

        selectedFilterData?.let {
            if (it.salaryType != 0) salaryType = it.salaryType

            if (it.minSalary .isNotBlank()) minSalary = it.minSalary
            if (it.maxSalary .isNotBlank()) maxSalary = it.maxSalary


            if (it.areas.isNotEmpty()) areaList = gson.toJson(it.areas)
            if (it.companyTypes.isNotEmpty()) companyType = gson.toJson(it.companyTypes)
            if (it.eduLevel.isNotEmpty()) educationLevel = gson.toJson(it.eduLevel)
            if (it.experienceLevel.isNotEmpty()) experience = gson.toJson(it.experienceLevel)
        }


        requestApi(mRetrofit.jobList(page, search,
            salaryType,
            minSalary,
            maxSalary,
            areaList,
            companyType,
            educationLevel,
            experience

        ), object : BaseMvpObserver<PageResponse<Job>>(view) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindJobList(data.toMutableList(), isLastPage)
            }
        })
    }


    /** 获取收藏职位列表 */
    fun getJobCollectionList(page: Int) {
        requestApi(mRetrofit.jobCollectionList(page), object : BaseMvpObserver<PageResponse<Job>>(view, page) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data

                view.bindJobCollectionList(data.toMutableList(), isLastPage)
            }
        })
    }

}
