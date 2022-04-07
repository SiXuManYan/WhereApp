package com.jcs.where.api.network

import com.blankj.utilcode.util.LogUtils
import com.jcs.where.api.BaseModel
import com.jcs.where.api.JcsResponse
import com.jcs.where.base.event.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer

/**
 * Created by Wangsw  2021/1/26 11:31.
 */
open class BaseMvpPresenter(private var mBaseMvpView: BaseMvpView?) : BaseModel() {

    protected fun <T> requestApi(observable: Observable<JcsResponse<T>>, observer: BaseMvpObserver<T>) {
        dealResponse(observable, observer)
    }

    fun detachView() {
        mBaseMvpView = null
        if (mObserver != null) {
            val compositeDisposable = mObserver.compositeDisposable
            compositeDisposable?.clear()
        }
    }

    /**
     * 添加RxBus 订阅
     */
    fun <T> addRxBusSubscribe(eventType: Class<T>, consumer: Consumer<T>) {

        val compositeDisposable = mObserver.compositeDisposable

        compositeDisposable?.add(
                RxBus.toFlowable(eventType).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumer, Consumer {
                            LogUtils.d(it)
                        })
        )
    }


}