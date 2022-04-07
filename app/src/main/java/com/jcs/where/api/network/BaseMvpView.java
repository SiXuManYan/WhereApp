package com.jcs.where.api.network;

import com.jcs.where.api.ErrorResponse;

/**
 * Created by Wangsw  2021/1/26 11:14.
 */
public interface BaseMvpView {

    /**
     * 处理网络请求异常
     *
     * @param errorResponse 错误信息返回
     */
    void onError(ErrorResponse errorResponse);

}
