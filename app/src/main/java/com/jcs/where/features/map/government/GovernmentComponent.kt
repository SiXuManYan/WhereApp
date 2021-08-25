package com.jcs.where.features.map.government

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/8/24 17:04.
 *
 */
interface GovernmentView : BaseMvpView {

}

class GovernmentPresenter(private val view: GovernmentView) : BaseMvpPresenter(view) {

}
