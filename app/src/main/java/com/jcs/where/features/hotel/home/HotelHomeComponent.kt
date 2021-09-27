package com.jcs.where.features.hotel.home

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.hotel.HotelHomeRecommend

/**
 * Created by Wangsw  2021/9/22 10:16.
 *
 */
interface HotelHomeView : BaseMvpView {
    fun bindData(response:ArrayList<HotelHomeRecommend>)
}

class HotelDetailPresenter(private var view: HotelHomeView) : BaseMvpPresenter(view) {


    fun getData() {
        requestApi(mRetrofit.hotelHomeRecommend(), object : BaseMvpObserver<ArrayList<HotelHomeRecommend>>(view) {
            override fun onSuccess(response: ArrayList<HotelHomeRecommend>) {
                view.bindData(response)
            }
        })
    }
}