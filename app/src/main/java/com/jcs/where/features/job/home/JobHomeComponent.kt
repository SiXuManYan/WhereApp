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

    /** 申请列表、面试列表 */
    fun bindAppliedOrInterviews(toMutableList: MutableList<Job>, lastPage: Boolean){}
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

            if (it.minSalary.isNotBlank()) minSalary = it.minSalary
            if (it.maxSalary.isNotBlank()) maxSalary = it.maxSalary


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
                data.forEach {
                    it.nativeListType = Job.TYPE_COMMON_JOB
                }
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
                data.forEach {
                    it.nativeListType = Job.TYPE_COLLETION_JOB
                }
                view.bindJobCollectionList(data.toMutableList(), isLastPage)
            }
        })
    }


    /** 获取投递、面试列表 */
    fun getJobApplyOrInterviews(page: Int, type: Int) {

        requestApi(mRetrofit.jobCollectionList(page, type), object : BaseMvpObserver<PageResponse<Job>>(view, page) {
            override fun onSuccess(response: PageResponse<Job>) {
                val isLastPage = response.lastPage == page
                val data = response.data


                data.forEach {
                    // 投递列表
                    if (type == Job.REQUEST_APPLIED) {
                        it.nativeListType = Job.TYPE_RECORD_APPLIED
                    }

                    // 面试列表
                    if (type == Job.REQUEST_INTERVIEWS) {
                        it.nativeListType = Job.TYPE_RECORD_INTERVIEWS
                    }
                }
                if (type == Job.REQUEST_INTERVIEWS) {
                    addTitle(data)
                }
                view.bindAppliedOrInterviews(data.toMutableList(), isLastPage)
            }
        })
    }




    private fun addTitle(data: MutableList<Job>) {

        val groupBy = data.groupBy { it.created_at }

        groupBy.forEach { group ->

            val titleEntity = Job().apply {
                created_at = group.key
                this.nativeListType = Job.TYPE_TITLE
            }

            val indexOfFirst = data.indexOfFirst {
                it.created_at == group.key
            }

            data.add(indexOfFirst, titleEntity)
        }
    }


}
