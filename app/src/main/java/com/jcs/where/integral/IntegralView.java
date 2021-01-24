package com.jcs.where.integral;

import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.response.SignListResponse;

/**
 * Created by Wangsw  2021/1/23 15:09.
 */
public interface IntegralView {


    void bindDetailData(SignListResponse response);

    void onDetailError(ErrorResponse errorResponse);
}
