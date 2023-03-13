package com.jiechengsheng.city.features.job.main

import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/12/15 10:15.
 *
 */
interface  JobMainView : BaseMvpView {

}

class JobMainPresenter(private var view: JobMainView) : BaseMvpPresenter(view){

}