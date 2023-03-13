package com.jiechengsheng.city.features.store.search

import com.jiechengsheng.city.BuildConfig
import com.jiechengsheng.city.api.network.BaseMvpObserver
import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.response.PageResponse
import com.jiechengsheng.city.api.response.store.StoreRecommend
import com.jiechengsheng.city.utils.CacheUtil
import com.jiechengsheng.city.utils.Constant

/**
 * Created by Wangsw  2021/6/7 10:16.
 *
 */
class StoreSearchPresenter(val view: StoreSearchView) : BaseMvpPresenter(view) {


    fun getData(page: Int, title: String) {

        val lat: Float?
        val lng: Float?

        if (BuildConfig.FLAVOR == "dev") {
            lat = null
            lng = null
        } else {
            lat = CacheUtil.getShareDefault().getFloat(Constant.SP_MY_LATITUDE, Constant.LAT.toFloat())
            lng = CacheUtil.getShareDefault().getFloat(Constant.SP_MY_LONGITUDE, Constant.LAT.toFloat())
        }

        requestApi(mRetrofit.getStoreList(
                page,
                lat,
                lng,
                null,
                title,
                null,
                null),
                object : BaseMvpObserver<PageResponse<StoreRecommend>>(view,page) {
                    override fun onSuccess(response: PageResponse<StoreRecommend>) {
                        val isLastPage = response.lastPage == page
                        val data = response.data.toMutableList()

                        view.bindData(data, isLastPage)
                    }
                })
    }


}