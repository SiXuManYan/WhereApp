package com.jcs.where.features.job.form

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/9/29 15:01.
 *
 */
interface CvFormView : BaseMvpView {

}


class CvFormPresenter(private var view: CvFormView) : BaseMvpPresenter(view) {

}