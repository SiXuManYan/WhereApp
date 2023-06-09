package com.jiechengsheng.city.features.hotel.map.child

import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.hotel.HotelHomeRecommend
import com.jiechengsheng.city.utils.SPKey

/**
 * Created by Wangsw  2021/9/27 15:48.
 *
 */
interface HotelChildView : BaseMvpView, OnItemClickListener, OnLoadMoreListener {
    fun bindList(toMutableList: MutableList<HotelHomeRecommend>, lastPage: Boolean)
}

class HotelChildPresenter(private var view: HotelChildView) : BaseMvpPresenter(view) {

    fun getData(page: Int, search_input: String?, star_level: String?, hotel_type_ids: String?, price_range: String?, grade: String?) {

        val instance = SPUtils.getInstance()
        val areaId = instance.getString(SPKey.SELECT_AREA_ID, "")

        requestApi(mRetrofit.hotelChildList(page, areaId, null, null, search_input, star_level, hotel_type_ids, price_range, grade),
            object : BaseMvpObserver<PageResponse<HotelHomeRecommend>>(view,page) {
                override fun onSuccess(response: PageResponse<HotelHomeRecommend>) {
                    val isLastPage = response.lastPage == page
                    val data = response.data
                    data.forEach {
                        it.contentType = HotelHomeRecommend.CONTENT_TYPE_COMMON
                    }
                    view.bindList(data.toMutableList(), isLastPage)
                }

            })
    }

}