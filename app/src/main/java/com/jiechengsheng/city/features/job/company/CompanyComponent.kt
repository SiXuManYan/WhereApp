package com.jiechengsheng.city.features.job.company

import com.google.gson.JsonElement
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.job.CompanyAlbum
import com.jiechengsheng.city.api.response.job.CompanyCollection
import com.jiechengsheng.city.api.response.job.CompanyInfo
import com.jiechengsheng.city.api.response.job.JobCollection

/**
 * Created by Wangsw  2022/11/1 16:16.
 *
 */
interface CompanyView : BaseMvpView {
    fun bindCompanyDetail(response: CompanyInfo, media: ArrayList<CompanyPhoto>){}
    fun bindCompanyAlbum(images: java.util.ArrayList<String>){}
    fun collectionResult(result: Boolean){}
}

class CompanyPresenter(var view: CompanyView) : BaseMvpPresenter(view) {

    fun getCompanyDetail(jobId: Int) {

        requestApi(mRetrofit.companyDetail(jobId), object : BaseMvpObserver<CompanyInfo>(view) {

            override fun onSuccess(response: CompanyInfo) {

                val media = ArrayList<CompanyPhoto>()
                response.images?.forEach {
                    val apply = CompanyPhoto().apply {
                        type = CompanyPhoto.HORIZONTAL_IMAGE
                        src = it
                    }
                    media.add(apply)
                }
                media.add(media.size, CompanyPhoto().apply { type = CompanyPhoto.HORIZONTAL_IMAGE_LOOK_MORE })

                view.bindCompanyDetail(response,media)
            }

        })
    }

    fun getAlbum(companyId: Int) {

        requestApi(mRetrofit.companyAlbum(companyId), object : BaseMvpObserver<CompanyAlbum>(view) {

            override fun onSuccess(response: CompanyAlbum) {



                view.bindCompanyAlbum(response.images)
            }

        })
    }

    /**
     * 统计从招聘首页职位列表入口点击
     */
    fun handleCollection(collect: Boolean, companyId: Int) {
        val apply = CompanyCollection().apply {
            company_id = companyId
        }

        if (collect) {
            requestApi(mRetrofit.companyDelCollection(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.collectionResult(false)
                }
            })
        } else {
            requestApi(mRetrofit.companyCollection(apply), object : BaseMvpObserver<JsonElement>(view) {
                override fun onSuccess(response: JsonElement) {
                    view.collectionResult(true)
                }
            })
        }
    }


}