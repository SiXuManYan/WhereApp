package com.jcs.where.api.response;

/**
 * Created by Wangsw  2021/1/23 15:32.
 * 积分明细
 */
public class IntegralDetailResponse {

/*    {
        "id": 3,
            "type": 5,
            "integral": 20,
            "created_at": "2020-10-20 16:18:42"
    }*/


    public int id;
    /**
     * 类型（1：注册，2：邀请，3：消费，4：评论，5：签到）
     */
    public int type;

    /**
     * 积分
     */
    public int integral;
    public String created_at;

}
