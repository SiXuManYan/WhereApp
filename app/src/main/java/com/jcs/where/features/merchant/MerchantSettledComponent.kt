package com.jcs.where.features.merchant

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView

/**
 * Created by Wangsw  2021/11/19 14:17.
 *
 */
interface MerchantSettledView : BaseMvpView {

}

class MerchantSettledPresenter(var view: MerchantSettledView) : BaseMvpPresenter(view) {


}