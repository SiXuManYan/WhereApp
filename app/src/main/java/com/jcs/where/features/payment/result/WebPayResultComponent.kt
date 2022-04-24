package com.jcs.where.features.payment.result

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/4/24 17:12.
 *
 */
interface WebPayResultView : BaseMvpView {

}

class WebPayResultPresenter(private var view: WebPayResultView) : BaseMvpPresenter(view) {

}