package com.jiechengsheng.city.features.hotel.book

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.request.HotelOrderRequest
import com.jiechengsheng.city.api.response.hotel.HotelOrderCommitResponse

/**
 * Created by Wangsw  2021/10/13 15:18.
 *
 */
interface HotelBookView : BaseMvpView {
    fun commitSuccess(response: HotelOrderCommitResponse)
}

class HotelBookPresenter(private var view: HotelBookView) : BaseMvpPresenter(view) {


    fun commitOrder(apply: HotelOrderRequest) {

        requestApi(mRetrofit.commitHotelOrder(apply), object : BaseMvpObserver<HotelOrderCommitResponse>(view) {
            override fun onSuccess(response: HotelOrderCommitResponse) {
                view.commitSuccess(response)
            }

        })

    }


}