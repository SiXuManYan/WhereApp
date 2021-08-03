package com.jcs.where.features.hotel.order

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/8/3 14:46.
 *
 */
interface OrderDetailView : BaseMvpView {

}

class OrderDetailPresenter(private var view: OrderDetailView) : BaseMvpPresenter(view) {

}