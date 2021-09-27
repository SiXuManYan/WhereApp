package com.jcs.where.features.hotel.map

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/9/27 14:07.
 *
 */

interface HotelMapView : BaseMvpView {

}

class HotelMapPresenter(private var view: HotelMapView) : BaseMvpPresenter(view) {




}