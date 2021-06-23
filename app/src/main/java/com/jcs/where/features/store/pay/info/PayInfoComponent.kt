package com.jcs.where.features.store.pay.info

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/6/23 17:11.
 *  支付信息
 */
interface PayInfoView : BaseMvpView {

}

class PayInfoPresenter(private var view: PayInfoView) : BaseMvpPresenter(view) {




}

