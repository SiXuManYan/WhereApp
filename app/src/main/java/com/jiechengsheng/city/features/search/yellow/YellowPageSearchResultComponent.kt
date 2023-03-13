package com.jiechengsheng.city.features.search.yellow

import com.blankj.utilcode.util.SPUtils
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.MechanismResponse
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.SPKey

/**
 * Created by Wangsw  2021/9/3 15:02.
 *
 */

interface YellowPageSearchResultView : BaseMvpView {
    fun bindData(response: MutableList<MechanismResponse>, isLastPage: Boolean)

}

class YellowPageSearchResultPresenter(private var view: YellowPageSearchResultView) : BaseMvpPresenter(view) {

    fun getData(page: Int, categoryId: String, search: String) {

        val latLng = CacheUtil.getSafeSelectLatLng()
        var areaId = SPUtils.getInstance().getString(SPKey.SELECT_AREA_ID, "")

        var lat: Double? = latLng.latitude
        var lng: Double? = latLng.longitude

        if (areaId.isNullOrBlank()) {
            areaId = null
        } else {
//            lat = null
//            lng = null
        }
        requestApi(mRetrofit.getMechanismListById3(page, categoryId, search, lat, lng, areaId,null),
            object : BaseMvpObserver<PageResponse<MechanismResponse>>(view, page) {
                override fun onSuccess(response: PageResponse<MechanismResponse>) {

                    val total = response.total
                    val isLastPage = response.lastPage == page
                    val data = response.data

                    view.bindData(data, isLastPage)
                }
            })
    }


}