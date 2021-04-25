package com.jcs.where.features.gourmet.takeaway

import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.response.gourmet.takeaway.TakeawayDetailResponse

/**
 * Created by Wangsw  2021/4/25 10:05.
 *
 */
class TakeawayPresenter(val view: TakeawayView) : BaseMvpPresenter(view) {


    /**
     * 获取详情页数据
     */
    fun getDetailData(restaurantId: String) {

        requestApi(mRetrofit.takeawayDetail(restaurantId),object :BaseMvpObserver<TakeawayDetailResponse>(view){
            override fun onSuccess(response: TakeawayDetailResponse?) {
                response?.let {
                    view.bindData(it)

                }
            }

        })

    }

}