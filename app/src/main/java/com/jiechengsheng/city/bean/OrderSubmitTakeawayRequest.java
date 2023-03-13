package com.jiechengsheng.city.bean;

/**
 * Created by Wangsw  2021/4/22 16:15.
 */
public class OrderSubmitTakeawayRequest {

    /**
     * 商品信息
     * [{"good_id": 1, "good_num": 1}, {"good_id": 2, "good_num": 2}]
     */
    public String goods = "";

    /**
     * 地址ID
     */
    public String address_id = "";

    /**
     * 餐厅ID
     */
    public String restaurant_id = "";

    /**
     * 配送时间类型（1：立即配送，2：选定时间）
     */
    public int delivery_time_type = 0;

    /**
     * 配送时间
     * 2020-12-21 15:26:00
     */
    public String delivery_time = "";

    /**
     * 备注
     */
    public String remark = "";
}
