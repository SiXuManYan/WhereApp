package com.jiechengsheng.city.features.hotel.home

import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend

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
                response.forEach {
                    it.contentType = HotelHomeRecommend.CONTENT_TYPE_COMMON
                }
                view.bindData(response)
            }
        })
    }





}