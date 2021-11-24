package com.jcs.where.features.integral

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/11/24 16:33.
 *
 */
interface SignInView : BaseMvpView {

}

class SignInPresenter(private var view: SignInView) : BaseMvpPresenter(view) {

}