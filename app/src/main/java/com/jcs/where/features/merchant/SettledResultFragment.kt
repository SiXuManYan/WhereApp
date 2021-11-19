package com.jcs.where.features.merchant

import android.view.View
import com.jcs.where.api.network.BaseMvpObserver
import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.base.BaseFragment
import com.jcs.where.base.mvp.BaseMvpFragment

/**
 * Created by Wangsw  2021/11/19 18:15.
 * 商家入驻审核中、审核结果
 */
class SettledResultFragment :BaseMvpFragment<SettledResultPresenter>(),SettledResultView {
    override fun initView(view: View?) {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }

    override fun bindListener() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }


}

interface SettledResultView :BaseMvpView {

}

class SettledResultPresenter(private var view: SettledResultView):BaseMvpPresenter(view){

}