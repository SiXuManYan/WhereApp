package com.jcs.where.features.coupon.user.child

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/3/2 15:11.
 *
 */

interface MyCouponView : BaseMvpView {

}


class MyCouponPresenter(private var view: MyCouponView) : BaseMvpPresenter(view) {

}