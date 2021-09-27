package com.jcs.where.features.hotel.map.child

import com.blankj.utilcode.util.SPUtils
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.hotel.HotelHomeRecommend
import com.jcs.where.utils.SPKey

/**
 * Created by Wangsw  2021/9/27 15:48.
 *
 */
interface HotelChildView : BaseMvpView {
    fun bindList(toMutableList: MutableList<HotelHomeRecommend>, lastPage: Boolean)


}

class HotelChildPresenter(private var view: HotelChildView) : BaseMvpPresenter(view) {


    fun getData(page: Int, search_input: String?, star_level: String?, hotel_type_ids: String?, price_range: String?, grade: String?) {

        val instance = SPUtils.getInstance()
        val areaId = instance.getString(SPKey.SELECT_AREA_ID, "")




        requestApi(mRetrofit.hotelChildList(page, areaId, null, null, search_input, star_level, hotel_type_ids, price_range, grade),
            object : BaseMvpObserver<PageResponse<HotelHomeRecommend>>(view){
                override fun onSuccess(response: PageResponse<HotelHomeRecommend>) {
                    val isLastPage = response.lastPage == page
                    view.bindList( response.data.toMutableList(), isLastPage)
                }

            })
    }

}