package com.jcs.where.features.job.company

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.CompanyInfo
import com.jcs.where.features.hotel.detail.media.MediaData

/**
 * Created by Wangsw  2022/11/1 16:16.
 *
 */
interface CompanyView : BaseMvpView {
    fun bindCompanyDetail(response: CompanyInfo, media: ArrayList<CompanyPhoto>)
}

class CompanyPresenter(var view: CompanyView) : BaseMvpPresenter(view) {

    fun getCompanyDetail(jobId: Int) {

        requestApi(mRetrofit.companyDetail(jobId), object : BaseMvpObserver<CompanyInfo>(view) {

            override fun onSuccess(response: CompanyInfo) {

                val media = ArrayList<CompanyPhoto>()
                response.images.forEach {
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
}