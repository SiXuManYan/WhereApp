package com.jcs.where.features.bills.preview

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/6/9 14:56.
 *
 */
interface BillsPlaceOrderView : BaseMvpView {

}

class BillsPlaceOrderPresenter(private var view: BillsPlaceOrderView) : BaseMvpPresenter(view) {

}