package com.jiechengsheng.city.features.gourmet.comment

import com.jiechengsheng.city.api.network.BaseMvpView
import com.jiechengsheng.city.api.response.hotel.HotelComment

/**
 * Created by Wangsw  2021/5/27 16:04.
 *
 */
interface FoodCommentView : BaseMvpView {
    fun bindCommentData(data: MutableList<HotelComment>, isLastPage: Boolean)


}