package com.jcs.where.features.refund.add.verify

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/4/26 14:48.
 *
 */
interface RefundBindVerifyView :BaseMvpView {

}

class RefundBindVerifyPresenter(private var view: RefundBindVerifyView) : BaseMvpPresenter(view){

}