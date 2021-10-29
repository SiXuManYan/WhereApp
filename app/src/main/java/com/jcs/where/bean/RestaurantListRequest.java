package com.jcs.where.bean;

/**
 * Created by Wangsw  2021/3/25 16:26.
 */
public class RestaurantListRequest {

    /**
     * 商业区ID
     */
    public String trading_area_id;

    /**
     * 人均价格
     */
    public String per_price;

    /**
     * 商家服务（1：支持外卖）
     */
    public String service;

    /**
     * （必要参数）排序（1：智能排序，2：好评优先，3：销量优先，4：距离优先）
     */
    public int sort = 1;

    /**
     * 搜索内容
     */
    public String search_input;

    /**
     * 纬度
     */
    public double lat;

    /**
     * 经度
     */
    public double lng;

    /**
     * 分类ID
     */
    public Integer category_id;
}
