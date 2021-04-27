package com.jcs.where.api.response.gourmet.dish;

import java.util.ArrayList;

/**
 * Created by Wangsw  2021/4/27 17:04.
 */
public class DeliveryTime {

    /**
     * 立即配送时间
     */
    public String delivery_time = "";

    /**
     * 其他配送时间
     */
    public ArrayList<String> other_times = new ArrayList<>();

}
