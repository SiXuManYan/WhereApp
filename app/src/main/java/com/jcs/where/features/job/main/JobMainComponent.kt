package com.jcs.where.features.job.main

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/12/15 10:15.
 *
 */
interface  JobMainView : BaseMvpView {

}

class JobMainPresenter(private var view: JobMainView) : BaseMvpPresenter(view){

}