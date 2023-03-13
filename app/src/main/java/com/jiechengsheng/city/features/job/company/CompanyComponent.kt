package com.jiechengsheng.city.features.job.company

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.job.CompanyAlbum
import com.jiechengsheng.city.api.response.job.CompanyInfo

/**
 * Created by Wangsw  2022/11/1 16:16.
 *
 */
interface CompanyView : BaseMvpView {
    fun bindCompanyDetail(response: CompanyInfo, media: ArrayList<CompanyPhoto>){}
    fun bindCompanyAlbum(images: java.util.ArrayList<String>){}
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
}