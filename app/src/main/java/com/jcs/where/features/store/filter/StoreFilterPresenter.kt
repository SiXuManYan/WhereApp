package com.jcs.where.features.store.filter

import com.google.gson.Gson
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.PageResponse
import com.jcs.where.api.response.store.StoreRecommend
import com.jcs.where.features.store.filter.screen.ScreenFilterFragment

/**
 * Created by Wangsw  2021/6/9 10:25.
 *
 */
class StoreFilterPresenter(val view: StoreFilterView) : BaseMvpPresenter(view) {


    /**
     * @param cate_id    分类ID（数据类型为‘1’或‘[1,2,3]’）
     * @param delivery_type 商家服务/配送方式（1:自提，2:商家配送）
     * @param sort_type 排序方式(1:距离优先，2:好评优先）
     */
    fun getData(page: Int, cateIds: ArrayList<Int>, moreFilter: ScreenFilterFragment.ScreenMoreFilter?) {

        val lat: Float? = null
        val lng: Float? = null

/*        if (BuildConfig.FLAVOR == "dev") {
            lat = null
            lng = null
        } else {
            lat = CacheUtil.getShareDefault().getFloat(Constant.SP_LATITUDE, Constant.LAT.toFloat())
            lng = CacheUtil.getShareDefault().getFloat(Constant.SP_LONGITUDE, Constant.LAT.toFloat())
        }*/


        var cate_id: String? = null

        if (cateIds.isNotEmpty()) {
            cate_id = Gson().toJson(cateIds)
        }
        requestApi(mRetrofit.getStoreList(
                page,
                lat,
                lng,
                cate_id,
                null,
                moreFilter?.delivery_type,
                moreFilter?.sort_type),
                object : BaseMvpObserver<PageResponse<StoreRecommend>>(view,page) {
                    override fun onSuccess(response: PageResponse<StoreRecommend>) {
                        val isLastPage = response.lastPage == page
                        val data = response.data.toMutableList()

                        view.bindData(data, isLastPage)
                    }
                })
    }


}