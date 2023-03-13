package com.jiechengsheng.city.api.network

import com.jiechengsheng.city.api.ErrorResponse

/**
 * Created by Wangsw  2021/1/26 11:14.
 */
interface BaseMvpView {
    /**
     * 处理网络请求异常
     *
     * @param errorResponse 错误信息返回
     */
    fun onError(errorResponse: ErrorResponse?)
    fun showLoading(){}
    fun hideLoading(){}
}