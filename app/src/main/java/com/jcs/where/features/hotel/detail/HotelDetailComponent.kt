package com.jcs.where.features.hotel.detail

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/10/8 11:34.
 *
 */

interface HotelDetailView : BaseMvpView {

}


class HotelDetailPresenter(private var view: HotelDetailView) : BaseMvpPresenter(view) {

}