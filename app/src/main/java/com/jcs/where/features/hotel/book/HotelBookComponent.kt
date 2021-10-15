package com.jcs.where.features.hotel.book

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.request.HotelOrderRequest
import com.jcs.where.api.response.hotel.HotelOrderCommitResponse

/**
 * Created by Wangsw  2021/10/13 15:18.
 *
 */
interface HotelBookView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)
}

class HotelBookPresenter(private var view: HotelBookView) : BaseMvpPresenter(view) {



    fun commitOrder(apply: HotelOrderRequest) {

    requestApi(mRetrofit.commitHotelOrder(apply),object :BaseMvpObserver<HotelOrderCommitResponse>(view){
        override fun onSuccess(response: HotelOrderCommitResponse) {
            view.commitSuccess(response)
        }

    })

    }


}