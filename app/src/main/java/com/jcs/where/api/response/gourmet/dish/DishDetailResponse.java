package com.jcs.where.api.response.gourmet.dish;

import java.math.BigDecimal;

/**
 * Created by Wangsw  2021/4/6 16:10.
 */
public class DishDetailResponse {
/*    {
        "id": "ID",
            "title": "标题",
            "image": "图片",
            "sale_num": "销量",
            "price": "价格",
            "original_price": "原价",
            "meals": "菜品",
            "rule": "规则"
    }*/

    public int id  = 0;
    public String title ;
    public String image ;
    public String sale_num ;
    public BigDecimal price = BigDecimal.ZERO ;
    public BigDecimal original_price = BigDecimal.ZERO;
    public String meals ;
    public String rule ;



}
