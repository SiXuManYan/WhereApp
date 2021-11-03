package com.jcs.where.api.response.gourmet.cart;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Wangsw  2021/4/7 11:47.
 */
public class GoodData implements Serializable {

    public int id = 0;
    public String title = "";
    public String image = "";
    public BigDecimal price = BigDecimal.ZERO;
    public BigDecimal original_price =  BigDecimal.ZERO;

    /**
     * 库存
     */
    public String inventory = "";

}
