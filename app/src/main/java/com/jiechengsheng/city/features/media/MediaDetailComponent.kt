package com.jiechengsheng.city.features.media

import com.jiechengsheng.city.api.network.BaseMvpPresenter
import com.jiechengsheng.city.api.network.BaseMvpView

/**
 * Created by Wangsw  2022/8/23 14:13.
 *
 */

interface MediaDetailView : BaseMvpView {

}

class MediaDetailPresenter(private var view: MediaDetailView) : BaseMvpPresenter(view){

}