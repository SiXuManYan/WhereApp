package com.jcs.where.api.response.search;

/**
 * Created by Wangsw  2021/2/25 10:19.
 * 搜索结果
 */
public class SearchResultResponse {

    public static final int TYPE_1_HOTEL = 1;
    public static final int TYPE_2_TRAVEL = 2;
    public static final int TYPE_3_SERVICE = 3;
    public static final int TYPE_4_RESTAURANT = 4;


/*    {
            "id": "ID",
            "name": "名称",
            "created_at": "创建时间",
            "type": "类型(1：酒店，2：旅游，3：综合服务)"
    }*/

    public int id ;
    public String name = "";
    public String created_at = "";
    /**
     * 类型(1：酒店，2：旅游，3：综合服务，4：餐厅)
     */
    public int type;


}
