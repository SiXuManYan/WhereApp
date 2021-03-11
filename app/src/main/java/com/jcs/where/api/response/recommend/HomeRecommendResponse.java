package com.jcs.where.api.response.recommend;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Wangsw  2021/3/1 17:26.
 * 推荐
 */
public class HomeRecommendResponse implements MultiItemEntity {


    public static final int MODULE_TYPE_1_HOTEL = 1;
    public static final int MODULE_TYPE_2_SERVICE = 2;
    public static final int MODULE_TYPE_3_FOOD = 3;
    public static final int MODULE_TYPE_4_TRAVEL = 4;

    /*    {
                "id": -5560003263243012,
                "module_type": -8358912549756568,
                "images": [],
                "title": "kBM^uHE",
                "grade": 7626899780435573,
                "comment_num": 4827186162277688,
                "tags": [],
                "address": ")bJj",
                "lat": -8466032590978560,
                "lng": 7850346910417496,
                "price": -1591808220071440.2,
                "remain_room_num": -7736963560453408,
                "facebook_link": "qdfHsn",
                "star_level": 3238374759773920,
                "distance": -2562233931618145,
                "per_price": -1354097720749708.5,
                "take_out_status": -4164900290038488,
                "restaurant_type": "CKb",
                "area_name": "x89M3b",
                "created_at": "uRRkr"
        }*/
    public int id = 0;

    /**
     * 模块类型（1：酒店，2：综合服务，3：餐厅，4：旅游景点）
     */
    public int module_type = 0;




    public String[] images = {};


    public String title = "";

    /**
     * 分数
     */
    public float grade = 0f;

    /**
     * 评价数量
     */
    public String comment_num = "";

    /**
     * 标签
     */
    public String[] tags = {};
    public String address = "";
    public String lat = "";
    public String lng = "";
    public String price = "0";

    /**
     * 剩余房间数
     */
    public String remain_room_num = "";

    /**
     * Facebook链接
     */
    public String facebook_link = "";

    /**
     * 星级
     */
    public String star_level = "";

    /**
     * 距离
     */
    public String distance = "0";
    /**
     * 均价
     */
    public String per_price = "";

    /**
     * 外卖状态（1：关闭，2：开启）
     */
    public int take_out_status = 0;

    /**
     * 餐厅类型
     */
    public String restaurant_type = "";

    /**
     * 地区名称
     */
    public String area_name = "";

    /**
     * 创建时间
     */
    public String created_at = "";

    @Override
    public int getItemType() {
        return module_type;
    }
}
