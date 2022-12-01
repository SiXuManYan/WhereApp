package com.jcs.where.features.job.filter

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/12/1 14:58.
 *
 */
interface JobFilterView :BaseMvpView {

}

class JobFilterPresenter(private var view: BaseMvpView):BaseMvpPresenter(view){

}