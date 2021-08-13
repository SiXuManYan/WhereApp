package com.jcs.where.features.main

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/8/12 15:25.
 *
 */
interface MainView : BaseMvpView {
}


class MainPresenter(private var view: MainView) : BaseMvpPresenter(view){
    
}

