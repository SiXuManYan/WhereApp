package com.jcs.where.features.bills.charges

import com.jcs.where.api.network.BaseMvpPresenter
import com.jcs.where.api.network.BaseMvpView
import com.jcs.where.bean.FaceValue

/**
 * Created by Wangsw  2022/6/15 16:55.
 *
 */
interface CallChargesView : BaseMvpView {
    fun bindFakerData(data: ArrayList<FaceValue>)
}


class CallChargesPresenter(var view: CallChargesView) : BaseMvpPresenter(view) {

    fun getFakerData() {

        val data = ArrayList<FaceValue>()
        data.add(FaceValue().apply { value = 10 })
        data.add(FaceValue().apply { value = 15 })
        data.add(FaceValue().apply { value = 20 })
        data.add(FaceValue().apply { value = 30 })
        data.add(FaceValue().apply { value = 50 })
        data.add(FaceValue().apply { value = 60 })
        data.add(FaceValue().apply { value = 100 })
        data.add(FaceValue().apply { value = 150 })
        data.add(FaceValue().apply { value = 200 })
        data.add(FaceValue().apply { value = 300 })
        data.add(FaceValue().apply { value = 500 })
        data.add(FaceValue().apply { value = 1000 })
        view.bindFakerData(data)

    }

}