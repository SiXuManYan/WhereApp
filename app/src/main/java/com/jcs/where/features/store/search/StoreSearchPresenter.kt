package com.jcs.where.features.store.search

import com.jcs.where.BuildConfig
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.utils.CacheUtil
import com.jcs.where.utils.Constant

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
            lat = CacheUtil.getShareDefault().getFloat(Constant.SP_LATITUDE, Constant.LAT.toFloat())
            lng = CacheUtil.getShareDefault().getFloat(Constant.SP_LONGITUDE, Constant.LAT.toFloat())
        }

        requestApi(mRetrofit.getStoreList(
                page,
                lat,
                lng,
                null,
                title,
                null,
                null),
                object : BaseMvpObserver<PageResponse<StoreRecommend>>(view) {
                    override fun onSuccess(response: PageResponse<StoreRecommend>) {
                        val isLastPage = response.lastPage == page
                        val data = response.data.toMutableList()

                        view.bindData(data, isLastPage)
                    }
                })
    }


}