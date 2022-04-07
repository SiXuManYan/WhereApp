package com.jcs.where.base.event

import androidx.annotation.NonNull
import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

/**
 *
 * 代替广播发送和接收消息
 */
object RxBus {

    private val processor by lazy { PublishProcessor.create<Any>().toSerialized() }

    /**
     * 发送事件
     *
     * @param obj
     */
    fun post(@NonNull obj: Any) {
        processor.onNext(obj)
    }

    /**
     * 返回指定类型的Flowable实例
     *
     * @param type 实体类类型
     * @param <T>  实体类
     * @return 转换后的类
     */
    fun <T> toFlowable(type: Class<T>): Flowable<T> = processor.ofType(type)!!

    /**
     * 是否已有观察者订阅
     *
     * @return true:有
     */
    fun hasObservers() = processor.hasSubscribers()
}