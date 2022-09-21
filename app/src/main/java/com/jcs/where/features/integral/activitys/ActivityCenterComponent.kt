package com.jcs.where.features.integral.activitys

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/9/21 10:08.
 *
 */
interface ActivityCenterView :BaseMvpView {

}

class ActivityCenterPresenter(private var view:BaseMvpView):BaseMvpPresenter(view){
    
}