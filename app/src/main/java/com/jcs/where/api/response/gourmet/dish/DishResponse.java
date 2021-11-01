package com.jcs.where.api.response.gourmet.dish;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Wangsw  2021/4/1 15:07.
 * 堂食菜品列表
 */
public class DishResponse {

/*    {
        "id": 1,
            "title": "The March.",
            "image": "https://whereoss.oss-cn-beijing.aliyuncs.com/foods/15105118933_ca37184e2f_k-e1548756281298.jpg",
            "sale_num": 58,
            "price": 5610,
            "original_price": 8361
    }*/


    public int id = 0;
    public String title = "";
    public String image = "";

    /** 销量 */
    public String sale_num = "";

    /**  库存*/
    public String inventory = "";
    public BigDecimal price = BigDecimal.ZERO;
    public BigDecimal original_price = BigDecimal.ZERO;

    // 外卖额外
    public ArrayList<String> tags = new ArrayList<>();

    /**
     * 用户选中的数量（外卖额外）
     */
    public int nativeSelectCount = 0 ;



}
