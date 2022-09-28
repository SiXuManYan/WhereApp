package com.jcs.where.features.job.cv

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.job.ProfileDetail

/**
 * Created by Wangsw  2022/9/28 17:06.
 *
 */
interface CvHomeView : BaseMvpView {
    fun bindData(response: ProfileDetail?)

}


class CvHomePresenter(private var view: CvHomeView) : BaseMvpPresenter(view) {


    fun getData() {
        requestApi(mRetrofit.profileDetail(),object :BaseMvpObserver<ProfileDetail>(view){
            override fun onSuccess(response: ProfileDetail?) {
                view.bindData(response)
            }

        })

    }

}