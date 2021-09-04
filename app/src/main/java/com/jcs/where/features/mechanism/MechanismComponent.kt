package com.jcs.where.features.mechanism

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/9/4 15:08.
 *
 */
interface MechanismView : BaseMvpView {

}


class MechanismPresenter(private var view: MechanismView) : BaseMvpPresenter(view) {

}