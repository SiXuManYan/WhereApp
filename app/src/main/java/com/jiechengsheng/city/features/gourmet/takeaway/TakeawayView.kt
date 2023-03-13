package com.jiechengsheng.city.features.gourmet.takeaway

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.gourmet.takeaway.TakeawayDetailResponse

/**
 * Created by Wangsw  2021/4/25 10:04.
 *
 */
interface TakeawayView :BaseMvpView {
    fun bindData(data: TakeawayDetailResponse)
}