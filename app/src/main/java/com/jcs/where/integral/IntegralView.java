package com.jcs.where.integral;

import com.jcs.where.api.network.BaseMvpView;
import com.jcs.where.api.response.SignListResponse;

/**
 * Created by Wangsw  2021/1/23 15:09.
 */
public interface IntegralView extends BaseMvpView {


    void bindDetailData(SignListResponse response);

    /**
     * 设置积分
     * @param integral 积分
     */
    void bindIntegral(String integral);

    /**
     * 签到成功
     */
    void signInSuccess();
}
