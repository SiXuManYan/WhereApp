package com.jcs.where.features.media

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/8/23 14:13.
 *
 */

interface MediaDetailView : BaseMvpView {

}

class MediaDetailPresenter(private var view: MediaDetailView) : BaseMvpPresenter(view){

}