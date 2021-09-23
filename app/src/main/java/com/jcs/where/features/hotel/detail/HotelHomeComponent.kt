package com.jcs.where.features.hotel.detail

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.hotel.HotelListResponse

/**
 * Created by Wangsw  2021/9/22 10:16.
 *
 */
interface HotelHomeView : BaseMvpView {
    fun bindData(response: java.util.ArrayList<HotelListResponse>)
}

class HotelDetailPresenter(private var view: HotelHomeView) : BaseMvpPresenter(view) {


    fun getData() {
        requestApi(mRetrofit.hotelHomeRecommend(), object : BaseMvpObserver<ArrayList<HotelListResponse>>(view) {
            override fun onSuccess(response: ArrayList<HotelListResponse>) {
                view.bindData(response)
            }
        })
    }
}