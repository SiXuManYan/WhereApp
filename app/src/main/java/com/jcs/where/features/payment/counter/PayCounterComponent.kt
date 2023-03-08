package com.jcs.where.features.payment.counter

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2023/3/7 16:08.
 *
 */
interface PayCounterView :BaseMvpView {

}

class PayCounterPresenter(private var view: PayCounterView):BaseMvpPresenter(view){

}